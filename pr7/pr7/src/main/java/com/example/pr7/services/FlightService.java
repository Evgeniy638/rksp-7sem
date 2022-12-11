package com.example.pr7.services;

import com.example.pr7.dto.FlightCreateDTO;
import lombok.RequiredArgsConstructor;
import com.example.pr7.dto.BookArgsDTO;
import com.example.pr7.dto.FlightDTO;
import com.example.pr7.dto.TicketDTO;
import com.example.pr7.mappers.MapperFlight;
import com.example.pr7.mappers.MapperTicket;
import com.example.pr7.model.Flight;
import com.example.pr7.model.Ticket;
import com.example.pr7.model.TicketStatus;
import com.example.pr7.repositories.FlightRepository;
import com.example.pr7.repositories.TicketRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.ParseException;

@Service
@RequiredArgsConstructor
public class FlightService {
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
                .map(MapperFlight::mapFlightDTO);
    }

    public Flux<TicketDTO> getTickets(final Flux<Integer> fluxFlightsId) {
        return fluxFlightsId
                .switchMap(flightId -> {
                    Flight flight = flightRepository.findById(flightId).orElse(null);

                    if (flight == null) {
                        return Flux.just();
                    }

                    return Flux.fromIterable(flight.getTickets())
                            .map(MapperTicket::mapTicketDTO);
                })
                .doOnError((err) -> {
                    System.err.println("Error: " + err.getMessage());
                })
                .onErrorComplete();
    }

    public Mono<FlightDTO> createFlight(FlightCreateDTO flightCreateDTO) {
        try {
            Flight flight = flightRepository.save(MapperFlight.mapFlightCreateDTOToFlight(flightCreateDTO));

            return Mono.just(MapperFlight.mapFlightDTO(flight));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}
