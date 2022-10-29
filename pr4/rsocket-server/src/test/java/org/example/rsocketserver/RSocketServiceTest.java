package org.example.rsocketserver;

import org.example.rsocketserver.dto.BookArgsDTO;
import org.example.rsocketserver.dto.FlightDTO;
import org.example.rsocketserver.dto.TicketDTO;
import org.example.rsocketserver.model.Flight;
import org.example.rsocketserver.model.Ticket;
import org.example.rsocketserver.model.TicketStatus;
import org.example.rsocketserver.repositories.FlightRepository;
import org.example.rsocketserver.repositories.TicketRepository;
import org.example.rsocketserver.services.RSocketService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class RSocketServiceTest {
    @Mock
    private FlightRepository flightRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Captor
    ArgumentCaptor<Ticket> ticketArgumentCaptor;

    @Test
    public void getAllFlights() {
        Flight flight1 = new Flight();
        flight1.setId(1);
        Flight flight2 = new Flight();
        flight2.setId(2);
        Flight flight3 = new Flight();
        flight3.setId(3);

        Mockito.when(flightRepository.findAll()).thenReturn(Arrays.asList(flight1, flight2, flight3));

        RSocketService studentService = new RSocketService(flightRepository,
                ticketRepository);

        Flux<FlightDTO> flightFlux = studentService.getAllFlights();

        StepVerifier
                .create(flightFlux)
                .consumeNextWith(flightDTO -> {
                    assertThat(flightDTO.getId()).isEqualTo(1);
                })
                .consumeNextWith(flightDTO -> {
                    assertThat(flightDTO.getId()).isEqualTo(2);
                })
                .consumeNextWith(flightDTO -> {
                    assertThat(flightDTO.getId()).isEqualTo(3);
                })
                .verifyComplete();
    }

    @Test
    public void getTickets() {
        Ticket ticket1 = new Ticket();
        ticket1.setId(1);
        Ticket ticket2 = new Ticket();
        ticket2.setId(2);
        Ticket ticket3 = new Ticket();
        ticket3.setId(3);
        Ticket ticket4 = new Ticket();
        ticket4.setId(4);
        Ticket ticket5 = new Ticket();
        ticket5.setId(5);


        Flight flight1 = new Flight();
        flight1.setId(1);
        flight1.setTickets(Set.of(ticket1, ticket2, ticket3));
        ticket1.setFlight(flight1);
        ticket2.setFlight(flight1);
        ticket3.setFlight(flight1);

        Flight flight2 = new Flight();
        flight2.setId(2);
        flight2.setTickets(Set.of(ticket4, ticket5));
        ticket4.setFlight(flight2);
        ticket5.setFlight(flight2);

        Mockito.when(flightRepository.findById(1)).thenReturn(Optional.of(flight1));
        Mockito.when(flightRepository.findById(2)).thenReturn(Optional.of(flight2));

        RSocketService studentService = new RSocketService(flightRepository,
                ticketRepository);

        Flux<TicketDTO> ticketDTOFlux = studentService.getTickets(Flux.fromArray(new Integer[]{ 1, 2}));

        StepVerifier
                .create(ticketDTOFlux)
                .consumeNextWith(ticketDTO -> {
                    assertThat(ticketDTO.getId()).isIn(1, 2, 3);
                    assertThat(ticketDTO.getFlightId()).isEqualTo(1);
                })
                .consumeNextWith(ticketDTO -> {
                    assertThat(ticketDTO.getId()).isIn(1, 2, 3);
                    assertThat(ticketDTO.getFlightId()).isEqualTo(1);
                })
                .consumeNextWith(ticketDTO -> {
                    assertThat(ticketDTO.getId()).isIn(1, 2, 3);
                    assertThat(ticketDTO.getFlightId()).isEqualTo(1);
                })
                .consumeNextWith(ticketDTO -> {
                    assertThat(ticketDTO.getId()).isIn(4, 5);
                    assertThat(ticketDTO.getFlightId()).isEqualTo(2);
                })
                .consumeNextWith(ticketDTO -> {
                    assertThat(ticketDTO.getId()).isIn(4, 5);
                    assertThat(ticketDTO.getFlightId()).isEqualTo(2);
                })
                .verifyComplete();
    }

    @Test
    public void book() {
        Ticket ticket = new Ticket();
        ticket.setId(1);
        ticket.setStatus(TicketStatus.FREE);

        BookArgsDTO bookArgsDTO = new BookArgsDTO();
        bookArgsDTO.setPassengerId("2");
        bookArgsDTO.setTicketId(1);

        Mockito.when(ticketRepository.findById(1)).thenReturn(Optional.of(ticket));

        RSocketService rSocketService = new RSocketService(flightRepository, ticketRepository);
        Mono<String> result = rSocketService.book(bookArgsDTO);

        Mockito.verify(ticketRepository).save(ticketArgumentCaptor.capture());

        Ticket ticketArgumentCaptorValue = ticketArgumentCaptor.getValue();

        Assertions.assertEquals(1, ticketArgumentCaptorValue.getId());
        Assertions.assertEquals("2", ticketArgumentCaptorValue.getPassengerId());
        Assertions.assertEquals(TicketStatus.BOOKED, ticketArgumentCaptorValue.getStatus());

        StepVerifier
                .create(result)
                .consumeNextWith(status -> assertThat(status).isEqualTo("Success"))
                .verifyComplete();
    }

    @Test
    public void deleteReservation() {
        Ticket ticket = new Ticket();
        ticket.setId(1);
        ticket.setStatus(TicketStatus.BOOKED);
        ticket.setPassengerId("2");

        Mockito.when(ticketRepository.findById(1)).thenReturn(Optional.of(ticket));

        RSocketService rSocketService = new RSocketService(flightRepository, ticketRepository);
        Mono<Void> result = rSocketService.deleteReservation(1);

        Mockito.verify(ticketRepository).save(ticketArgumentCaptor.capture());

        Ticket ticketArgumentCaptorValue = ticketArgumentCaptor.getValue();

        Assertions.assertEquals(1, ticketArgumentCaptorValue.getId());
        Assertions.assertNull(ticketArgumentCaptorValue.getPassengerId());
        Assertions.assertEquals(TicketStatus.FREE, ticketArgumentCaptorValue.getStatus());

        StepVerifier
                .create(result)
                .verifyComplete();
    }
}
