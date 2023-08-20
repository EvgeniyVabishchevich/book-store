package com.andersen.config.model;

public record JdbcConfigModel(
        String contextPath,
        Integer port,
        String jdbcUrl,
        String jdbcUsername,
        String jdbcPassword
) {
}