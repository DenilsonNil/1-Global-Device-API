package br.com.kualit.devices.application.usecases;

import br.com.kualit.devices.application.gateways.DevicePort;
import br.com.kualit.devices.domain.entities.device.Device;

import java.util.List;

public class FindAllDevices {

    private final DevicePort devicePort;

    public FindAllDevices(DevicePort devicePort) {
        this.devicePort = devicePort;
    }

    public List<Device> findAll(Integer page, Integer size){
        return devicePort.findAll(page, size);
    }
}
