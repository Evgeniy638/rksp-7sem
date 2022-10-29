package org.example.rsocketserver.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.rsocketserver.dto.BookArgsDTO;
import org.example.rsocketserver.dto.FlightDTO;
import org.example.rsocketserver.dto.TicketDTO;
import org.example.rsocketserver.services.RSocketService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RSocketController {
    private final RSocketService rSocketService;

    public static final String SERVER = "Server";
    public static final String STREAM = "Stream";
    public static final String CHANNEL = "Channel";

    private final List<RSocketRequester> CLIENTS = new ArrayList<>();

    @PreDestroy
    void shutdown() {
        log.info("Detaching all remaining clients...");
        CLIENTS.stream().forEach(requester -> requester.rsocket().dispose());
        log.info("Shutting down.");
    }

    @ConnectMapping("shell-client")
    void connectShellClientAndAskForTelemetry(RSocketRequester requester,
                                              @Payload String client) {

        requester.rsocket()
                .onClose()
                .doFirst(() -> {
                    // Add all new clients to a client list
                    log.info("Client: {} CONNECTED.", client);
                    CLIENTS.add(requester);
                })
                .doOnError(error -> {
                    // Warn when channels are closed by clients
                    log.warn("Channel to client {} CLOSED", client);
                })
                .doFinally(consumer -> {
                    // Remove disconnected clients from the client list
                    CLIENTS.remove(requester);
                    log.info("Client {} DISCONNECTED", client);
                })
                .subscribe();

        // Callback to client, confirming connection
        requester.route("client-status")
                .data("OPEN")
                .retrieveFlux(String.class)
                .doOnNext(s -> log.info("Client: {} Free Memory: {}.", client, s))
                .subscribe();
    }

    /**
     * Забронировать место
     */
    @MessageMapping("request-response")
    Mono<String> book(final BookArgsDTO bookArgsDTO) {
        log.info("Received request-response ticketId: {}", bookArgsDTO.getTicketId());

        return rSocketService.book(bookArgsDTO);
    }

    /**
     * Удалить бронь с места в рейсе по id места
     */
    @MessageMapping("fire-and-forget")
    public Mono<Void> deleteReservation(final int ticketId) {
        log.info("Received fire-and-forget ticketId: {}", ticketId);

        return rSocketService.deleteReservation(ticketId);
    }

    /**
     * Получить список всех рейсов
     */
    @MessageMapping("request-stream")
    Flux<FlightDTO> getAllFlights() {
        return rSocketService.getAllFlights();
    }

    /**
     * По id рейсов узнать их места
     */
    @MessageMapping("channel")
    Flux<TicketDTO> getTickets(final Flux<Integer> fluxFlightsId) {
        Flux<Integer> loggedFluxFlightsId = fluxFlightsId
                .doOnCancel(() -> log.warn("The client cancelled the channel."))
                .doOnNext(flightId -> log.info("Channel flightId: {}", flightId));

        return rSocketService.getTickets(loggedFluxFlightsId);
    }
}
