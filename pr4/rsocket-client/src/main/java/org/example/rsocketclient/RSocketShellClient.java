package org.example.rsocketclient;

import io.rsocket.SocketAcceptor;
import lombok.extern.slf4j.Slf4j;
import org.example.rsocketclient.dto.BookDataDTO;
import org.example.rsocketclient.dto.FlightDTO;
import org.example.rsocketclient.dto.TicketDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import javax.annotation.PreDestroy;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@ShellComponent
public class RSocketShellClient {

    private static final String CLIENT_ID = UUID.randomUUID().toString();
    private static Disposable disposable;

    private final RSocketRequester rsocketRequester;

    @Autowired
    public RSocketShellClient(RSocketRequester.Builder builder,
                              @Qualifier("rSocketStrategies") RSocketStrategies strategies) {
        SocketAcceptor responder = RSocketMessageHandler.responder(strategies, new ClientHandler());

        this.rsocketRequester = builder
                .setupRoute("shell-client")
                .setupData(CLIENT_ID)
                .rsocketConnector(connector -> connector.acceptor(responder))
                .connectTcp("localhost", 7000)
                .block();

        this.rsocketRequester.rsocket()
                .onClose()
                .doOnError(error -> log.warn("Connection CLOSED"))
                .doFinally(consumer -> log.info("Client DISCONNECTED"))
                .subscribe();
    }

    @PreDestroy
    @ShellMethod("Logout and close your connection")
    public void logout() {
        if (isConnection()) {
            this.s();
            this.rsocketRequester.rsocket().dispose();
            log.info("Logged out.");
        }
    }

    private boolean isConnection() {
        if (null == this.rsocketRequester || this.rsocketRequester.rsocket().isDisposed()) {
            log.info("No connection.");
            return false;
        }
        return true;
    }

    @ShellMethod("Book")
    public void book(int ticketId, String passengerId) throws InterruptedException {
        if (isConnection()) {
            log.info("\nSending one request. Waiting for one response...");
            String status = this.rsocketRequester
                    .route("request-response")
                    .data(new BookDataDTO(ticketId, passengerId))
                    .retrieveMono(String.class)
                    .block();
            log.info("\nResponse was: {}", status);
        }
    }

    @ShellMethod("Delete a reservation")
    public void deleteReservation(int ticketId) throws InterruptedException {
        if (isConnection()) {
            log.info("\nFire-And-Forget. Sending one request. Expect no response (check server console log)...");
            this.rsocketRequester
                    .route("fire-and-forget")
                    .data(ticketId)
                    .send()
                    .block();
            log.info("Delete a reservation by ticket id: {}", ticketId);
        }
    }

    @ShellMethod("Get all flights")
    public void flights() {
        if (isConnection()) {
            log.info("\n\n**** Request-Stream\n**** Send one request.\n**** Log responses.\n**** Type 's' to stop.");
            disposable = this.rsocketRequester
                    .route("request-stream")
                    .retrieveFlux(FlightDTO.class)
                    .doOnComplete(() -> log.info("Finish get all flights"))
                    .subscribe(flightDTO -> log.info("\nResponse: {} \n(Type 's' to stop.)", flightDTO.toString()));
        }
    }

    @ShellMethod("Get tickets by flight id")
    public void tickets(String flightIds) {
        if (isConnection()) {
            log.info("\n\n***** Channel (bi-directional streams)\n***** Asking for a stream of messages.\n***** Type 's' to stop.\n\n");

            List<String> arrFlightId = Arrays.asList(flightIds.split(" "));

            if (arrFlightId.isEmpty()) {
                log.info("Empty flight-ids parameter");
                return;
            }

            Flux<Integer> fluxFlightIds = Flux.fromIterable(arrFlightId).map(Integer::valueOf);

            disposable = this.rsocketRequester
                    .route("channel")
                    .data(fluxFlightIds)
                    .retrieveFlux(TicketDTO.class)
                    .subscribe(ticketDTO -> log.info("\nReceived: {} \n(Type 's' to stop.)", ticketDTO));
        }
    }

    @ShellMethod("Stops Streams or Channels.")
    public void s() {
        if (isConnection() && null != disposable) {
            log.info("Stopping the current stream.");
            disposable.dispose();
            log.info("Stream stopped.");
        }
    }
}

@Slf4j
class ClientHandler {

    @MessageMapping("client-status")
    public Flux<String> statusUpdate(String status) {
        log.info("Connection {}", status);
        return Flux.interval(Duration.ofSeconds(5)).map(index -> String.valueOf(Runtime.getRuntime().freeMemory()));
    }
}
