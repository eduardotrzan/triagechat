package com.triagechat.controller.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.triagechat.service.config.TriageChatServiceConfig;

@Configuration
@ComponentScan(basePackages = "com.triagechat.controller")
@Import({ TriageChatServiceConfig.class, SwaggerConfig.class })
public class TriageChatControllerConfig {


}
