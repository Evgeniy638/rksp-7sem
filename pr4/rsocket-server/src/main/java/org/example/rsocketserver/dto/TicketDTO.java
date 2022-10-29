package org.example.rsocketserver.dto;

import lombok.Data;
import org.example.rsocketserver.model.TicketStatus;

@Data
public class TicketDTO {
    private int id;
    private String seat;
    private TicketStatus status;
    private String passengerId;
    private int flightId;
}
