package com.triagechat.service.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.triagechat.domain.config.TriageChatDomainConfig;

@Configuration
@ComponentScan(basePackages = "com.triagechat.service")
@Import({ TriageChatDomainConfig.class })
public class TriageChatServiceConfig {

}
