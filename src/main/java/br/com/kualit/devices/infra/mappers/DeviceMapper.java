package br.com.kualit.devices.infra.mappers;

import br.com.kualit.devices.domain.entities.device.Device;
import br.com.kualit.devices.infra.controllers.DeviceCreationRequestDTO;
import br.com.kualit.devices.infra.controllers.DeviceResponseDTO;
import br.com.kualit.devices.infra.controllers.DeviceUpdatingRequestDTO;
import br.com.kualit.devices.infra.persistence.DeviceEntity;

public final class DeviceMapper {

    private DeviceMapper() {}

    public static Device toDomain(DeviceEntity entity) {
        return Device.builder()
                .id(entity.getId())
                .name(entity.getName())
                .brand(entity.getBrand())
                .state(entity.getState())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public static Device toDomain(DeviceCreationRequestDTO dto) {
        return Device.builder()
                .name(dto.name())
                .brand(dto.brand())
                .state(dto.state())
                .build();
    }

    public static Device toDomain(DeviceUpdatingRequestDTO dto) {
        return Device.builder()
                .name(dto.name())
                .brand(dto.brand())
                .state(dto.state())
                .build();
    }

    public static DeviceResponseDTO toResponseDTO(Device device) {
        return new DeviceResponseDTO(
                device.getId(),
                device.getName(),
                device.getBrand(),
                device.getState(),
                device.getCreatedAt()
        );
    }

    public static DeviceEntity toEntity(Device device) {
        return new DeviceEntity(device.getId(),
                device.getName(),
                device.getBrand(),
                device.getState(),
                device.getCreatedAt());

    }
}
