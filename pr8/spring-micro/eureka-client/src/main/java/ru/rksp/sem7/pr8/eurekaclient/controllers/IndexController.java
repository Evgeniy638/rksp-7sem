package ru.rksp.sem7.pr8.eurekaclient.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
    @Value("${eureka.instance.instance-id}")
    private String moduleId;

    @Value("${author.name}")
    private String authorName;

    @GetMapping("/ping")
    public String ping() {
        return "pong from " + moduleId;
    }


    @GetMapping("/author")
    public String getAuthor() {
        return authorName;
    }
}
