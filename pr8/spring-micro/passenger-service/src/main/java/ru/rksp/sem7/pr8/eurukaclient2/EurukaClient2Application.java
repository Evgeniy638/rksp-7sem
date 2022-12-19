package ru.rksp.sem7.pr8.eurukaclient2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class EurukaClient2Application {

    public static void main(String[] args) {
        SpringApplication.run(EurukaClient2Application.class, args);
    }

}
