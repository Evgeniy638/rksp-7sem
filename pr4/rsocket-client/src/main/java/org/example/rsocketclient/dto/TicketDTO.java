package org.example.rsocketclient.dto;

import lombok.Data;

@Data
public class TicketDTO {
    private int id;
    private String seat;
    private TicketStatus status;
    private String passengerId;
    private int flightId;
}
