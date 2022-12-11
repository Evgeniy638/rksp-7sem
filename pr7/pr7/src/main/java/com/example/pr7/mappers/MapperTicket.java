package com.example.pr7.mappers;

import com.example.pr7.dto.FlightCreateDTO;
import com.example.pr7.dto.TicketCreateDTO;
import com.example.pr7.dto.TicketDTO;
import com.example.pr7.model.Flight;
import com.example.pr7.model.Ticket;
import com.example.pr7.model.TicketStatus;

import java.text.ParseException;

public class MapperTicket {
    public static TicketDTO mapTicketDTO(Ticket ticket) {
        TicketDTO ticketDTO = new TicketDTO();

        ticketDTO.setId(ticket.getId());
        ticketDTO.setSeat(ticket.getSeat());
        ticketDTO.setStatus(ticket.getStatus());
        ticketDTO.setPassengerId(ticket.getPassengerId());
        ticketDTO.setFlightId(ticket.getFlight().getId());

        return ticketDTO;
    }

    public static Ticket mapTicketCreateDTOToTicket(TicketCreateDTO ticketCreateDTO) {
        Ticket ticket = new Ticket();

        ticket.setSeat(ticketCreateDTO.getSeat());
        ticket.setStatus(TicketStatus.FREE);

        return ticket;
    }
}
