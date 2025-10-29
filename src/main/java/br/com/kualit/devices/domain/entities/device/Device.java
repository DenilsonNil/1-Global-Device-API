package br.com.kualit.devices.domain.entities.device;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Device {
    private Long id;
    private String name;
    private String brand;
    private DeviceStateEnum state;
    private LocalDateTime createdAt;
}
