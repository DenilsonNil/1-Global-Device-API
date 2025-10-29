package br.com.kualit.devices.infra.controllers;

import br.com.kualit.devices.domain.entities.device.DeviceStateEnum;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * DTO for {@link br.com.kualit.devices.infra.persistence.DeviceEntity}
 */
public record DeviceUpdatingRequestDTO(
        @JsonProperty("name")
        String name,

        @JsonProperty("brand")
        String brand,

        @JsonProperty("state")
        DeviceStateEnum state
) implements Serializable {}