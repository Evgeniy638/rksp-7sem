package ru.rksp.sem7.pr8.eurukaclient2.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.rksp.sem7.pr8.eurukaclient2.dto.TicketDTO;

@FeignClient(name = "eureka-client-1")
public interface FlightFeignClient {
    @GetMapping("/ticket/{ticketId}")
    TicketDTO getTicketById(@PathVariable Integer ticketId);
}
