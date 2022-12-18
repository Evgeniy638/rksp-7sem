package ru.rksp.sem7.pr8.apigateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
public class IndexController {

    @GetMapping("/user")
    public Mono<String> index(Principal principal) {
        return Mono.just(principal.getName());
    }
}
