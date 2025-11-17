package com.linkedreams.flowmind;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
public class FlowMindApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlowMindApplication.class, args);
    }

}
