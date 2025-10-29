package br.com.kualit.devices.infra.controllers;

import br.com.kualit.devices.domain.entities.device.DeviceStateEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link br.com.kualit.devices.infra.persistence.DeviceEntity}
 */
public record DeviceCreationRequestDTO(
        @JsonProperty("name")
        @NotBlank(message = "The name is mandatory")
        String name,

        @JsonProperty("brand")
        @NotBlank(message = "The brand is mandatory")
        String brand,

        @JsonProperty("state")
        @NotNull(message = "The state is mandatory")
        DeviceStateEnum state
) implements Serializable {}