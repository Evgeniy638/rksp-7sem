package ru.rksp.sem7.pr8.eurukaclient2.dto;

import lombok.Data;

@Data
public class PassengerCreateDTO {
    private String fullName;
    private String passportNumber;
    private String passportSeries;
}
