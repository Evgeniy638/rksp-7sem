package ru.rksp.sem7.pr8.eurukaclient2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rksp.sem7.pr8.eurukaclient2.client.FlightFeignClient;
import ru.rksp.sem7.pr8.eurukaclient2.dto.PassengerCreateDTO;
import ru.rksp.sem7.pr8.eurukaclient2.dto.PassengerDTO;
import ru.rksp.sem7.pr8.eurukaclient2.dto.TicketDTO;
import ru.rksp.sem7.pr8.eurukaclient2.model.Passenger;
import ru.rksp.sem7.pr8.eurukaclient2.repository.PassengerRepository;

@Service
@RequiredArgsConstructor
public class PassengerService {
    private final FlightFeignClient flightFeignClient;
    private final PassengerRepository passengerRepository;

    public Passenger createPassenger(PassengerCreateDTO passengerCreateDTO) {
        Passenger passenger = new Passenger();

        passenger.setFullName(passengerCreateDTO.getFullName());
        passenger.setPassportNumber(passengerCreateDTO.getPassportNumber());
        passenger.setPassportSeries(passengerCreateDTO.getPassportSeries());

        return passengerRepository.save(passenger);
    }

    public PassengerDTO getPassengerByTicket(int ticketId) {
        TicketDTO ticketDTO = flightFeignClient.getTicketById(ticketId);

        if (ticketDTO == null) {
            return null;
        }

        Passenger passenger = passengerRepository.findById(ticketDTO.getPassengerId()).orElse(null);

        if (passenger == null) {
            return null;
        }

        PassengerDTO passengerDTO = new PassengerDTO();

        passengerDTO.setId(passenger.getId());
        passengerDTO.setFullName(passenger.getId());
        passengerDTO.setPassportNumber(passenger.getPassportNumber());
        passengerDTO.setPassportSeries(passenger.getPassportSeries());

        return passengerDTO;
    }
}
