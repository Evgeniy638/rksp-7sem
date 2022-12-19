package ru.rksp.sem7.pr8.eurekaclient1.mappers;

import ru.rksp.sem7.pr8.eurekaclient1.dto.TicketCreateDTO;
import ru.rksp.sem7.pr8.eurekaclient1.dto.TicketDTO;
import ru.rksp.sem7.pr8.eurekaclient1.model.Ticket;
import ru.rksp.sem7.pr8.eurekaclient1.model.TicketStatus;

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
