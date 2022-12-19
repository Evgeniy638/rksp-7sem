package ru.rksp.sem7.pr8.eurekaclient1.services;

import ru.rksp.sem7.pr8.eurekaclient1.dto.BookArgsDTO;
import ru.rksp.sem7.pr8.eurekaclient1.dto.FlightCreateDTO;
import ru.rksp.sem7.pr8.eurekaclient1.dto.FlightDTO;
import ru.rksp.sem7.pr8.eurekaclient1.dto.TicketDTO;
import ru.rksp.sem7.pr8.eurekaclient1.mappers.MapperFlight;
import ru.rksp.sem7.pr8.eurekaclient1.mappers.MapperTicket;
import ru.rksp.sem7.pr8.eurekaclient1.model.Flight;
import ru.rksp.sem7.pr8.eurekaclient1.model.Ticket;
import ru.rksp.sem7.pr8.eurekaclient1.model.TicketStatus;
import ru.rksp.sem7.pr8.eurekaclient1.repositories.FlightRepository;
import ru.rksp.sem7.pr8.eurekaclient1.repositories.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightService {
    private final FlightRepository flightRepository;
    private final TicketRepository ticketRepository;

    public String book(final BookArgsDTO bookArgsDTO) {
        Ticket ticket = ticketRepository.findById(bookArgsDTO.getTicketId()).get();

        if (ticket.getStatus().equals(TicketStatus.BOOKED)) {
            return "The place is occupied";
        }

        ticket.setStatus(TicketStatus.BOOKED);
        ticket.setPassengerId(bookArgsDTO.getPassengerId());

        ticketRepository.save(ticket);

        return "Success";
    }

    public void deleteReservation(final int ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).get();

        ticket.setStatus(TicketStatus.FREE);
        ticket.setPassengerId(null);

        ticketRepository.save(ticket);
    }

    public List<FlightDTO> getAllFlights() {
        return flightRepository.findAll().stream().map(MapperFlight::mapFlightDTO).toList();
    }

    public List<TicketDTO> getTickets(final List<Integer> listFlightsId) {
        List<TicketDTO> ticketDTOList = new ArrayList<>();

        for (Integer flightId: listFlightsId) {
            Flight flight = flightRepository.findById(flightId).orElse(null);

            if (flight != null) {
                flight.getTickets().stream()
                        .map(MapperTicket::mapTicketDTO)
                        .forEach(ticketDTOList::add);
            }
        }


        return ticketDTOList;
    }

    public FlightDTO createFlight(FlightCreateDTO flightCreateDTO) {
        try {
            Flight flight = flightRepository.save(MapperFlight.mapFlightCreateDTOToFlight(flightCreateDTO));

            return MapperFlight.mapFlightDTO(flight);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public TicketDTO getTicketById(int ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElse(null);

        if (ticket == null) {
            return null;
        }

        return MapperTicket.mapTicketDTO(ticket);
    }
}
