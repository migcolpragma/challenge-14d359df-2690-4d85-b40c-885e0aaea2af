package com.pragma;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = com.example.reto.Application.class)
class AppTest {

    @Test
    void contextLoads() {
        // This test ensures that the Spring context loads successfully
        // and is required for Cucumber to initialize the Spring context
    }
}