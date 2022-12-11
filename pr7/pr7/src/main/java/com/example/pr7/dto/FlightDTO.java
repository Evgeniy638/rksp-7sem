package com.example.pr7.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class FlightDTO {
    private int id;
    private String to;
    private String from;
    private String departureFormattedTime;
    private String arrivalFormattedTime;
}
