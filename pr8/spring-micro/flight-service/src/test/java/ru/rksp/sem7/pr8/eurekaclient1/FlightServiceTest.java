package ru.rksp.sem7.pr8.eurekaclient1;

import ru.rksp.sem7.pr8.eurekaclient1.dto.BookArgsDTO;
import ru.rksp.sem7.pr8.eurekaclient1.dto.FlightCreateDTO;
import ru.rksp.sem7.pr8.eurekaclient1.dto.FlightDTO;
import ru.rksp.sem7.pr8.eurekaclient1.dto.TicketDTO;
import ru.rksp.sem7.pr8.eurekaclient1.mappers.MapperFlight;
import ru.rksp.sem7.pr8.eurekaclient1.model.Flight;
import ru.rksp.sem7.pr8.eurekaclient1.model.Ticket;
import ru.rksp.sem7.pr8.eurekaclient1.model.TicketStatus;
import ru.rksp.sem7.pr8.eurekaclient1.repositories.FlightRepository;
import ru.rksp.sem7.pr8.eurekaclient1.repositories.TicketRepository;
import ru.rksp.sem7.pr8.eurekaclient1.services.FlightService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class FlightServiceTest {
    @Mock
    private FlightRepository flightRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Captor
    ArgumentCaptor<Ticket> ticketArgumentCaptor;

    @Captor
    ArgumentCaptor<Flight> flightArgumentCaptor;

    @Test
    public void getAllFlights() {
        Flight flight1 = new Flight();
        flight1.setId(1);
        Flight flight2 = new Flight();
        flight2.setId(2);
        Flight flight3 = new Flight();
        flight3.setId(3);

        Mockito.when(flightRepository.findAll()).thenReturn(Arrays.asList(flight1, flight2, flight3));

        FlightService flightService = new FlightService(flightRepository,
                ticketRepository);

        List<FlightDTO> flightFlux = flightService.getAllFlights();

        assertThat(flightFlux.get(0).getId()).isEqualTo(1);
        assertThat(flightFlux.get(1).getId()).isEqualTo(2);
        assertThat(flightFlux.get(2).getId()).isEqualTo(3);
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

        FlightService flightService = new FlightService(flightRepository,
                ticketRepository);

        List<TicketDTO> ticketDTOFlux = flightService.getTickets(List.of(new Integer[]{ 1, 2 }));


        assertThat(ticketDTOFlux.get(0).getId()).isIn(1, 2, 3);
        assertThat(ticketDTOFlux.get(0).getFlightId()).isEqualTo(1);

        assertThat(ticketDTOFlux.get(1).getId()).isIn(1, 2, 3);
        assertThat(ticketDTOFlux.get(1).getFlightId()).isEqualTo(1);

        assertThat(ticketDTOFlux.get(2).getId()).isIn(1, 2, 3);
        assertThat(ticketDTOFlux.get(2).getFlightId()).isEqualTo(1);

        assertThat(ticketDTOFlux.get(3).getId()).isIn(4, 5);
        assertThat(ticketDTOFlux.get(3).getFlightId()).isEqualTo(2);

        assertThat(ticketDTOFlux.get(4).getId()).isIn(4, 5);
        assertThat(ticketDTOFlux.get(4).getFlightId()).isEqualTo(2);
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

        FlightService flightService = new FlightService(flightRepository, ticketRepository);
        String status = flightService.book(bookArgsDTO);

        Mockito.verify(ticketRepository).save(ticketArgumentCaptor.capture());

        Ticket ticketArgumentCaptorValue = ticketArgumentCaptor.getValue();

        Assertions.assertEquals(1, ticketArgumentCaptorValue.getId());
        Assertions.assertEquals("2", ticketArgumentCaptorValue.getPassengerId());
        Assertions.assertEquals(TicketStatus.BOOKED, ticketArgumentCaptorValue.getStatus());

        assertThat(status).isEqualTo("Success");
    }

    @Test
    public void deleteReservation() {
        Ticket ticket = new Ticket();
        ticket.setId(1);
        ticket.setStatus(TicketStatus.BOOKED);
        ticket.setPassengerId("2");

        Mockito.when(ticketRepository.findById(1)).thenReturn(Optional.of(ticket));

        FlightService flightService = new FlightService(flightRepository, ticketRepository);
        flightService.deleteReservation(1);

        Mockito.verify(ticketRepository).save(ticketArgumentCaptor.capture());

        Ticket ticketArgumentCaptorValue = ticketArgumentCaptor.getValue();

        Assertions.assertEquals(1, ticketArgumentCaptorValue.getId());
        Assertions.assertNull(ticketArgumentCaptorValue.getPassengerId());
        Assertions.assertEquals(TicketStatus.FREE, ticketArgumentCaptorValue.getStatus());
    }

    @Test
    public void createFlightTest() throws ParseException {
        FlightCreateDTO flightCreateDTO = new FlightCreateDTO();

        flightCreateDTO.setTo("Москва");
        flightCreateDTO.setFrom("Минск");
        flightCreateDTO.setDepartureFormattedTime("17:30 29-11-2022");
        flightCreateDTO.setArrivalFormattedTime("21:00 29-11-2022");

        Flight flight = new Flight();

        flight.setId(1);
        flight.setTo("Москва");
        flight.setFrom("Минск");
        flight.setDepartureTime(MapperFlight.simpleDateFormat.parse("17:30 29-11-2022"));
        flight.setArrivalTime(MapperFlight.simpleDateFormat.parse("21:00 29-11-2022"));

        Mockito.when(flightRepository.save(Mockito.any(Flight.class))).thenReturn(flight);

        FlightService flightService = new FlightService(flightRepository, ticketRepository);
        flightService.createFlight(flightCreateDTO);

        Mockito.verify(flightRepository).save(flightArgumentCaptor.capture());

        Flight flightArgumentCaptorValue = flightArgumentCaptor.getValue();

        Assertions.assertEquals("Москва", flightArgumentCaptorValue.getTo());
        Assertions.assertEquals("Минск", flightArgumentCaptorValue.getFrom());
        Assertions.assertEquals(
                "17:30 29-11-2022",
                MapperFlight.simpleDateFormat.format(flightArgumentCaptorValue.getDepartureTime())
        );
        Assertions.assertEquals(
                "21:00 29-11-2022",
                MapperFlight.simpleDateFormat.format(flightArgumentCaptorValue.getArrivalTime())
        );
    }
}

