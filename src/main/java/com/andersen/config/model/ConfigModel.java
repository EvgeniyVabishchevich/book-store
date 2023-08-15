package com.andersen.config.model;

public record ConfigModel(
        String contextPath,
        Integer port,
        String savePath
) {
}