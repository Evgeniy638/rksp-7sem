package ru.rksp.sem7.pr8.eurekaclient1.dto;

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
