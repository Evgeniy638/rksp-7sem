package ru.rksp.sem7.pr8.eurukaclient2.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.rksp.sem7.pr8.eurukaclient2.dto.PassengerCreateDTO;
import ru.rksp.sem7.pr8.eurukaclient2.dto.PassengerDTO;
import ru.rksp.sem7.pr8.eurukaclient2.model.Passenger;
import ru.rksp.sem7.pr8.eurukaclient2.service.PassengerService;

@RestController
@RequiredArgsConstructor
public class PassengerController {
    private final PassengerService passengerService;

    @PostMapping("/passenger")
    public Passenger createPassenger(@RequestBody PassengerCreateDTO passengerCreateDTO) {
        return passengerService.createPassenger(passengerCreateDTO);
    }

    @GetMapping("/passenger/ticket/{ticketId}")
    public PassengerDTO getPassengerByTicket(@PathVariable int ticketId) {
        return passengerService.getPassengerByTicket(ticketId);
    }
}
