package ru.rksp.sem7.pr8.eurukaclient2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.rksp.sem7.pr8.eurukaclient2.client.MessageFeignClient;
import ru.rksp.sem7.pr8.eurukaclient2.dto.MessageDTO;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageFeignClient messageFeignClient;

    @Value("${eureka.instance.instance-id}")
    private String moduleId;

    public MessageDTO getMessageFromOther() {
        return messageFeignClient.getMessage(moduleId);
    }
}
