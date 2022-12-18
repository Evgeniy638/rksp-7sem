package ru.rksp.sem7.pr8.eurekaclient1.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.rksp.sem7.pr8.eurekaclient1.dto.MessageDTO;

@RestController
public class MessageController {
    @Value("${eureka.instance.instance-id}")
    private String moduleId;

    @GetMapping("/message/{name}")
    public MessageDTO getMessage(@PathVariable String name) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setText("Привет " + name);
        messageDTO.setModuleId(moduleId);

        return messageDTO;
    }
}
