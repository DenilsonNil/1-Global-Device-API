package br.com.kualit.devices.infra.controllers;

import br.com.kualit.devices.domain.entities.device.DeviceStateEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link br.com.kualit.devices.infra.persistence.DeviceEntity}
 */
public record DeviceResponseDTO (
        @JsonProperty("id") Long id,
        @JsonProperty("name") String name,
        @JsonProperty("brand") String brand,
        @JsonProperty("state") DeviceStateEnum state,
        @JsonProperty("createdAt") LocalDateTime createdAt
) implements Serializable {}