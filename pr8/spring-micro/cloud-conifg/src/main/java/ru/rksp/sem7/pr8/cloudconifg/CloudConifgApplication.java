package ru.rksp.sem7.pr8.cloudconifg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigServer
public class CloudConifgApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudConifgApplication.class, args);
    }

}
