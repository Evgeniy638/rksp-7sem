package com.example.pr7.dto;

import lombok.Data;

import java.util.List;

@Data
public class FlightCreateDTO {
    private String to;
    private String from;
    private String departureFormattedTime;
    private String arrivalFormattedTime;
    private List<TicketCreateDTO> ticketDTOList;
}
