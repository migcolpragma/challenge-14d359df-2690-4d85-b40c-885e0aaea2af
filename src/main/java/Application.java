package com.example.reto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import domain.port.AccountRepository;
import domain.service.AccountValidationService;

@SpringBootApplication(scanBasePackages = {
    "com.example.reto",
    "domain",
    "infrastructure",
    "bdd",
    "application"
})
@EntityScan(basePackages = {"domain.model", "infrastructure.repository"})
@EnableJpaRepositories(basePackages = {"infrastructure.repository"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public AccountValidationService accountValidationService(AccountRepository accountRepository) {
        return new AccountValidationService(accountRepository);
    }

    @Bean
    public application.usecase.AccountUseCase accountUseCase(AccountRepository accountRepository) {
        return new application.usecase.AccountUseCase(accountRepository);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}