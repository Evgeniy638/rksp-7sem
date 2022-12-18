package ru.rksp.sem7.pr8.eurekaclient1.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.rksp.sem7.pr8.eurekaclient1.dto.MessageDTO;
import ru.rksp.sem7.pr8.eurekaclient1.repositories.FlightRepository;

@RestController
@RequiredArgsConstructor
public class MessageController {
    private final FlightRepository flightRepository;

    @Value("${eureka.instance.instance-id}")
    private String moduleId;

    @GetMapping("/ping")
    public String ping() {
        return "pong from " + moduleId;
    }

    @GetMapping("/private")
    public String getPrivate() {
        return "private from " + moduleId;
    }

    @GetMapping("/message/{name}")
    public MessageDTO getMessage(@PathVariable String name) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setText("Привет " + name);
        messageDTO.setModuleId(moduleId);

        return messageDTO;
    }

    @GetMapping("/first-flight-id")
    public Integer getAllFlights() {
        return flightRepository.findAll().get(0).getId();
    }
}
