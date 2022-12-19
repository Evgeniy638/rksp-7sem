package ru.rksp.sem7.pr8.eurekaclient1.mappers;

import ru.rksp.sem7.pr8.eurekaclient1.dto.FlightCreateDTO;
import ru.rksp.sem7.pr8.eurekaclient1.dto.FlightDTO;
import ru.rksp.sem7.pr8.eurekaclient1.model.Flight;
import ru.rksp.sem7.pr8.eurekaclient1.model.Ticket;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Set;
import java.util.stream.Collectors;

public class MapperFlight {
    public final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm dd-MM-yyyy");

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

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return  flightDTO;
    }

    public static Flight mapFlightCreateDTOToFlight(FlightCreateDTO flightCreateDTO) throws ParseException {
        Flight flight = new Flight();

        flight.setFrom(flightCreateDTO.getFrom());
        flight.setTo(flightCreateDTO.getTo());

        if (flightCreateDTO.getDepartureFormattedTime() != null) {
            flight.setDepartureTime(simpleDateFormat.parse(flightCreateDTO.getDepartureFormattedTime()));
        }

        if (flightCreateDTO.getArrivalFormattedTime() != null) {
            flight.setArrivalTime(simpleDateFormat.parse(flightCreateDTO.getArrivalFormattedTime()));
        }

        if (flightCreateDTO.getTicketDTOList() != null) {
            Set<Ticket> ticketSet = flightCreateDTO
                    .getTicketDTOList()
                    .stream()
                    .map((MapperTicket::mapTicketCreateDTOToTicket))
                    .collect(Collectors.toSet());

            ticketSet.forEach(flight::addTicket);
        }

        return flight;
    }
}
