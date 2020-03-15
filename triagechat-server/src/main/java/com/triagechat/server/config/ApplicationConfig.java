package com.triagechat.server.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.context.properties.ConfigurationProperties;

@Slf4j
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@ConfigurationProperties(prefix = "config")
public class ApplicationConfig {

    @ToString.Include
    private String serviceName;

    @ToString.Include
    private String timeZone;

}
