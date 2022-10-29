package org.example.rsocketserver.mappers;

import org.example.rsocketserver.dto.FlightDTO;
import org.example.rsocketserver.model.Flight;

import java.text.SimpleDateFormat;

public class MapperFlightDTO {
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm dd-mm-yyyyy");

    public static FlightDTO mapFlightDTO(Flight flight) {
        FlightDTO flightDTO = new FlightDTO();

        flightDTO.setId(flight.getId());
        flightDTO.setFrom(flight.getFrom());
        flightDTO.setTo(flight.getTo());

        if (flight.getDepartureTime() != null) {
            flightDTO.setDepartureFormattedTime(simpleDateFormat.format(flight.getDepartureTime()));
        }

        if (flight.getArrivalTime() != null) {
            flightDTO.setArrivalFormattedTime(simpleDateFormat.format(flight.getArrivalTime()));
        }

        return  flightDTO;
    }
}
