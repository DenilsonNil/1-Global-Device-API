package br.com.kualit.devices.application.usecases;

import br.com.kualit.devices.application.gateways.DevicePort;
import br.com.kualit.devices.domain.entities.device.Device;

public class UpdateDevice {

    private final DevicePort devicePort;

    public UpdateDevice(DevicePort devicePort) {
        this.devicePort = devicePort;
    }

    public Device update(Device device, Long deviceId) {
        return devicePort.update(device, deviceId);
    }
}
