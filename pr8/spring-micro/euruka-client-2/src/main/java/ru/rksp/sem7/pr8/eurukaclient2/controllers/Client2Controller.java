package ru.rksp.sem7.pr8.eurukaclient2.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rksp.sem7.pr8.eurukaclient2.dto.MessageDTO;

@RestController
@RequiredArgsConstructor
public class Client2Controller {

    @Value("${eureka.instance.instance-id}")
    private String moduleId;

    @GetMapping("/ping")
    public String ping() {
        return "pong pong from " + moduleId;
    }
}
