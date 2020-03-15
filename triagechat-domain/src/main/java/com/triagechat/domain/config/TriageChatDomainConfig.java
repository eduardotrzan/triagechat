package com.triagechat.domain.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = { "com.triagechat.domain.entity"})
@EnableJpaRepositories("com.triagechat.domain.repo")
public class TriageChatDomainConfig {

}
