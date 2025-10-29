package br.com.kualit.devices.application.usecases;

import br.com.kualit.devices.application.gateways.DevicePort;
import br.com.kualit.devices.domain.entities.device.Device;

public class FindDeviceById {

    private final DevicePort devicePort;

    public FindDeviceById(DevicePort devicePort) {
        this.devicePort = devicePort;
    }

    public Device findById(Long deviceId){
        return devicePort.findById(deviceId);
    }
}
