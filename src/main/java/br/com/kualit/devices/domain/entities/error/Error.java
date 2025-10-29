package br.com.kualit.devices.domain.entities.error;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Error(
        String message,
        String code,
        String field
){}
