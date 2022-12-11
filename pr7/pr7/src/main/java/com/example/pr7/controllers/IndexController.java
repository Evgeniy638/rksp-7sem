package com.example.pr7.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class IndexController {

    @GetMapping("/ping")
    public Mono<String> getMessage() {
        return Mono.just("pong");
    }
}
