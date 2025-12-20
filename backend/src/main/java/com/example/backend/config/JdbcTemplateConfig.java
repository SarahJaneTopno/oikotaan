package com.example.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class JdbcTemplateConfig {
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        if(dataSource == null) {
            throw new IllegalStateException("DataSource bean is not configured properly.");
        }
        return new JdbcTemplate(dataSource);
    }
}
