package ru.rksp.sem7.pr8.eurukaclient2.dto;

import lombok.Data;
import ru.rksp.sem7.pr8.eurukaclient2.model.TicketStatus;

@Data
public class TicketDTO {
    private int id;
    private String seat;
    private TicketStatus status;
    private String passengerId;
    private int flightId;
}
