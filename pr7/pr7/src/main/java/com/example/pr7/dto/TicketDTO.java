package com.example.pr7.dto;

import lombok.Data;
import com.example.pr7.model.TicketStatus;

@Data
public class TicketDTO {
    private int id;
    private String seat;
    private TicketStatus status;
    private String passengerId;
    private int flightId;
}
