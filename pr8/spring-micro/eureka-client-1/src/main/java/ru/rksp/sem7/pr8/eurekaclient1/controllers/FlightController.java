package ru.rksp.sem7.pr8.eurekaclient1.controllers;

import org.springframework.beans.factory.annotation.Value;
import ru.rksp.sem7.pr8.eurekaclient1.dto.BookArgsDTO;
import ru.rksp.sem7.pr8.eurekaclient1.dto.FlightCreateDTO;
import ru.rksp.sem7.pr8.eurekaclient1.dto.FlightDTO;
import ru.rksp.sem7.pr8.eurekaclient1.dto.TicketDTO;
import ru.rksp.sem7.pr8.eurekaclient1.services.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FlightController {
    private final FlightService flightService;

    @Value("${author.name}")
    private String authorName;


    @GetMapping("/author")
    public String getAuthor() {
        return authorName;
    }

    /**
     * Забронировать место
     */
    @PutMapping("/book")
    public String book(@RequestBody BookArgsDTO bookArgsDTO) {
        return flightService.book(bookArgsDTO);
    }

    /**
     * Удалить бронь с места в рейсе по id места
     */
    @DeleteMapping("/delete-reservation/{ticketId}")
    public void deleteReservation(@PathVariable int ticketId) {
        flightService.deleteReservation(ticketId);
    }

    /**
     * Получить список всех рейсов
     */
    @GetMapping("/flights")
    public List<FlightDTO> getAllFlights() {
        return flightService.getAllFlights();
    }

    /**
     * По id рейсов узнать их места
     */
    @GetMapping("/tickets/{fluxFlightsId}")
    public List<TicketDTO>
    getTickets(@PathVariable Integer[] fluxFlightsId) {
        return flightService.getTickets(List.of(fluxFlightsId));
    }

    /**
     * Создаём рейс с билетами
     */
    @PostMapping("/flight")
    public FlightDTO createFlight(@RequestBody FlightCreateDTO flightCreateDTO) {
        return flightService.createFlight(flightCreateDTO);
    }

    /**
     * По id рейсов узнать их места
     */
    @GetMapping("/ticket/{ticketId}")
    public TicketDTO getTicketById(@PathVariable Integer ticketId) {
        return flightService.getTicketById(ticketId);
    }
}
