package com.vino.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitialConfig {

    @Value("${application.name}")
    private String applicationName;

    public InitialConfig() {
        String param = null;
        System.out.println("Param: " + param.toString());
    }
}
