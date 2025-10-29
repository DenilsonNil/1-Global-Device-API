package br.com.kualit.devices.application.gateways;

import br.com.kualit.devices.domain.entities.device.Device;

import java.util.List;

public interface DevicePort {
    Device save(Device device);

    Device findById(Long deviceId);

    List<Device> findAll(Integer page, Integer size);

    List<Device> filterDevices(Integer page, Integer size, String ... params);

    Device update(Device device, Long deviceId);

    void delete(Long deviceId);
}
