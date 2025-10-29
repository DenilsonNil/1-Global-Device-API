package br.com.kualit.devices.application.usecases;

import br.com.kualit.devices.application.gateways.DevicePort;
import br.com.kualit.devices.domain.entities.device.Device;

public class CreateNewDevice {

    private final DevicePort devicePort;

    public CreateNewDevice(DevicePort devicePort) {
        this.devicePort = devicePort;
    }

    public Device save(Device device) {
        return devicePort.save(device);
    }
}
