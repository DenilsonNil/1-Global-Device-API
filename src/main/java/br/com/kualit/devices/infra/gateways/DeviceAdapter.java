package br.com.kualit.devices.infra.gateways;

import br.com.kualit.devices.application.gateways.DevicePort;
import br.com.kualit.devices.domain.entities.device.Device;
import br.com.kualit.devices.domain.entities.device.DeviceStateEnum;
import br.com.kualit.devices.infra.mappers.DeviceMapper;
import br.com.kualit.devices.infra.persistence.DeviceEntity;
import br.com.kualit.devices.infra.persistence.DeviceRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static br.com.kualit.devices.domain.entities.device.DeviceStateEnum.IN_USE;
import static br.com.kualit.devices.domain.entities.error.ErrorMessages.*;
import static br.com.kualit.devices.infra.mappers.DeviceMapper.toDomain;
import static br.com.kualit.devices.infra.mappers.DeviceMapper.toEntity;
import static java.util.stream.Collectors.toList;

@Service
public class DeviceAdapter implements DevicePort {

    private final DeviceRepository deviceRepository;

    public DeviceAdapter(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public Device save(Device device) {
        DeviceEntity deviceEntity = toEntity(device);
        deviceEntity.setCreatedAt(LocalDateTime.now());
        DeviceEntity savedDevice = deviceRepository.save(deviceEntity);
        return toDomain(savedDevice);
    }

    @Override
    public Device update(Device device, Long deviceId) {
        DeviceEntity deviceEntity = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new NoSuchElementException(DEVICE_NOT_FOUND));

        boolean isTryingToUpdateInUseDevice = deviceEntity.getState() == IN_USE;

        boolean b = device.getName() != null || device.getBrand() != null;
        if(isTryingToUpdateInUseDevice && b) {
            throw new IllegalArgumentException(IN_USE_DEVICES_CANNOT_BE_UPDATED);
        }

        deviceEntity.setName(device.getName() == null ? deviceEntity.getName() : device.getName());
        deviceEntity.setBrand(device.getBrand() == null ? deviceEntity.getBrand() : device.getBrand());
        deviceEntity.setState(device.getState() == null ? deviceEntity.getState() : device.getState());
        deviceEntity = deviceRepository.save(deviceEntity);
        return toDomain(deviceEntity);
    }

    @Override
    public Device findById(Long deviceId) {
        DeviceEntity deviceEntity = deviceRepository.findById(deviceId)
                .orElseThrow();
        return toDomain(deviceEntity);
    }

    @Override
    public List<Device> filterDevices(Integer page, Integer size, String... params) {
        var brand = params[0] == null ? null : params[0];
        var state = params[1] == null ? null : DeviceStateEnum.valueOf(params[1]);

        var pageRequest = PageRequest.of(page, size);
        var devices = deviceRepository.filterDevices(brand, state, pageRequest);
        return devices.getContent().stream().map(DeviceMapper::toDomain).collect(toList());
    }

    @Override
    public List<Device> findAll(Integer page, Integer size) {
        var pageRequest = PageRequest.of(page, size);
        var devices = deviceRepository.findAll(pageRequest);
        return devices.stream().map(DeviceMapper::toDomain)
                .collect(toList());
    }

    @Override
    public void delete(Long deviceId) {
        DeviceEntity deviceEntity = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new NoSuchElementException(DEVICE_NOT_FOUND));

        if(deviceEntity.getState() == IN_USE) {
            throw new IllegalArgumentException(IN_USE_DEVICES_CANNOT_BE_DELETED);
        }

        deviceRepository.deleteById(deviceId);
    }
}
