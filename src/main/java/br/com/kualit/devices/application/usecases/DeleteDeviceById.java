package br.com.kualit.devices.application.usecases;

import br.com.kualit.devices.application.gateways.DevicePort;

public class DeleteDeviceById {

    private final DevicePort devicePort;

    public DeleteDeviceById(DevicePort devicePort) {
        this.devicePort = devicePort;
    }

    public void deleteDeviceById(Long deviceId) {
        devicePort.delete(deviceId);
    }
}
