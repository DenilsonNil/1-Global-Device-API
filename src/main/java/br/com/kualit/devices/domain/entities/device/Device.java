package br.com.kualit.devices.domain.entities.device;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class Device {
    private Long id;
    private String name;
    private String brand;
    private DeviceStateEnum state;
    private LocalDateTime createdAt;
}
