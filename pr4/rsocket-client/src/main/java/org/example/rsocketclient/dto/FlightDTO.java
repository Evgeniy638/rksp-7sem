package org.example.rsocketclient.dto;

import lombok.Data;

@Data
public class FlightDTO {
    private int id;
    private String to;
    private String from;
    private String departureFormattedTime;
    private String arrivalFormattedTime;
}
