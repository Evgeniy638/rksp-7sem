package ru.rksp.sem7.pr8.eurekaclient1.dto;

import ru.rksp.sem7.pr8.eurekaclient1.model.TicketStatus;
import lombok.Data;

@Data
public class TicketDTO {
    private int id;
    private String seat;
    private TicketStatus status;
    private String passengerId;
    private int flightId;
}
