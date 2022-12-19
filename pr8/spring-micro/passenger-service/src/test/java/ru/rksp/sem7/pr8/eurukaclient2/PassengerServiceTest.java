package ru.rksp.sem7.pr8.eurukaclient2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.rksp.sem7.pr8.eurukaclient2.client.FlightFeignClient;
import ru.rksp.sem7.pr8.eurukaclient2.dto.PassengerDTO;
import ru.rksp.sem7.pr8.eurukaclient2.dto.TicketDTO;
import ru.rksp.sem7.pr8.eurukaclient2.model.Passenger;
import ru.rksp.sem7.pr8.eurukaclient2.repository.PassengerRepository;
import ru.rksp.sem7.pr8.eurukaclient2.service.PassengerService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class PassengerServiceTest {
    @Mock
    private FlightFeignClient flightFeignClient;

    @Mock
    private PassengerRepository passengerRepository;

    @Test
    public void getPassengerByTicket() {
        PassengerDTO passengerDTO = new PassengerDTO();
        passengerDTO.setId("1");

        Passenger passenger = new Passenger();
        passenger.setId(passengerDTO.getId());

        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setId(1);
        ticketDTO.setPassengerId(passengerDTO.getId());

        Mockito.when(flightFeignClient.getTicketById(1)).thenReturn(ticketDTO);
        Mockito.when(passengerRepository.findById("1")).thenReturn(Optional.of(passenger));

        PassengerService passengerService = new PassengerService(flightFeignClient, passengerRepository);

        PassengerDTO resultPassenger = passengerService.getPassengerByTicket(ticketDTO.getId());

        assertThat(resultPassenger.getId()).isEqualTo(passengerDTO.getId());
    }
}
