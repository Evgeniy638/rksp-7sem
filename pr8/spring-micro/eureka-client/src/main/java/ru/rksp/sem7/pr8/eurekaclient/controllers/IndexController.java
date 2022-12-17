package ru.rksp.sem7.pr8.eurekaclient.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
