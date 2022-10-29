package org.example.rsocketserver.mappers;

import org.example.rsocketserver.dto.TicketDTO;
import org.example.rsocketserver.model.Ticket;

public class MapperTicketDTO {
    public static TicketDTO mapTicketDTO(Ticket ticket) {
        TicketDTO ticketDTO = new TicketDTO();

        ticketDTO.setId(ticket.getId());
        ticketDTO.setSeat(ticket.getSeat());
        ticketDTO.setStatus(ticket.getStatus());
        ticketDTO.setPassengerId(ticket.getPassengerId());
        ticketDTO.setFlightId(ticket.getFlight().getId());

        return ticketDTO;
    }
}
