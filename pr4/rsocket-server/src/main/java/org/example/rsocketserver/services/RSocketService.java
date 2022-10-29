package org.example.rsocketserver.services;

import lombok.RequiredArgsConstructor;
import org.example.rsocketserver.dto.BookArgsDTO;
import org.example.rsocketserver.dto.FlightDTO;
import org.example.rsocketserver.dto.TicketDTO;
import org.example.rsocketserver.mappers.MapperFlightDTO;
import org.example.rsocketserver.mappers.MapperTicketDTO;
import org.example.rsocketserver.model.Flight;
import org.example.rsocketserver.model.Ticket;
import org.example.rsocketserver.model.TicketStatus;
import org.example.rsocketserver.repositories.FlightRepository;
import org.example.rsocketserver.repositories.TicketRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RSocketService {
    private final FlightRepository flightRepository;
    private final TicketRepository ticketRepository;

    public Mono<String> book(final BookArgsDTO bookArgsDTO) {
        Ticket ticket = ticketRepository.findById(bookArgsDTO.getTicketId()).get();

        if (ticket.getStatus().equals(TicketStatus.BOOKED)) {
            return Mono.just("The place is occupied");
        }

        ticket.setStatus(TicketStatus.BOOKED);
        ticket.setPassengerId(bookArgsDTO.getPassengerId());

        ticketRepository.save(ticket);

        return Mono.just("Success");
    }

    public Mono<Void> deleteReservation(final int ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).get();

        ticket.setStatus(TicketStatus.FREE);
        ticket.setPassengerId(null);

        ticketRepository.save(ticket);

        return Mono.empty();
    }

    public Flux<FlightDTO> getAllFlights() {
        return Flux
                .fromIterable(flightRepository.findAll())
                .map(MapperFlightDTO::mapFlightDTO);
    }

    public Flux<TicketDTO> getTickets(final Flux<Integer> fluxFlightsId) {
        return fluxFlightsId
                .switchMap(flightId -> {
                    Flight flight = flightRepository.findById(flightId).get();

                    return Flux.fromIterable(flight.getTickets())
                            .map(MapperTicketDTO::mapTicketDTO);
                });
    }
}
