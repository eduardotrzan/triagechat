package com.triagechat.server;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import com.triagechat.controller.config.SwaggerConfig;
import com.triagechat.controller.config.TriageChatControllerConfig;
import com.triagechat.server.config.ApplicationConfig;

@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties({
                                       ApplicationConfig.class
                               })
@Import({
                SwaggerConfig.class,

                // Controller Modules
                TriageChatControllerConfig.class

        })
@SpringBootApplication
public class Application implements CommandLineRunner {

    private final ApplicationConfig config;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    private void started() {
        TimeZone.setDefault(TimeZone.getTimeZone(this.config.getTimeZone()));
    }

    @Override
    public void run(String... strings) {
        log.info("Running system with config={}.", this.config);
    }
}
