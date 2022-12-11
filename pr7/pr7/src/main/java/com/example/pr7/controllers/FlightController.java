package com.example.pr7.controllers;

import com.example.pr7.dto.BookArgsDTO;
import com.example.pr7.dto.FlightCreateDTO;
import com.example.pr7.dto.FlightDTO;
import com.example.pr7.dto.TicketDTO;
import com.example.pr7.model.Flight;
import com.example.pr7.services.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class FlightController {
    private final FlightService flightService;

    /**
     * Забронировать место
     */
    @PutMapping("/book")
    public Mono<String> book(@RequestBody BookArgsDTO bookArgsDTO) {
        return flightService.book(bookArgsDTO);
    }

    /**
     * Удалить бронь с места в рейсе по id места
     */
    @DeleteMapping("/delete-reservation/{ticketId}")
    public Mono<Void> deleteReservation(@PathVariable int ticketId) {
        return flightService.deleteReservation(ticketId);
    }

    /**
     * Получить список всех рейсов
     */
    @GetMapping("/flights")
    public Flux<FlightDTO> getAllFlights() {
        return flightService.getAllFlights().limitRate(5);
    }

    /**
     * По id рейсов узнать их места
     */
    @GetMapping("/tickets/{fluxFlightsId}")
    public Flux<TicketDTO> getTickets(@PathVariable Integer[] fluxFlightsId) {
        return flightService.getTickets(
                Flux.fromIterable(new HashSet<>(List.of(fluxFlightsId))).limitRate(1)
        );
    }

    /**
     * Создаём рейс с билетами
     */
    @PostMapping("/flight")
    public Mono<FlightDTO> createFlight(@RequestBody FlightCreateDTO flightCreateDTO) {
        return flightService.createFlight(flightCreateDTO);
    }
}
