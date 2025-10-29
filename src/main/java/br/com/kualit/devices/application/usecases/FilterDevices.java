package br.com.kualit.devices.application.usecases;

import br.com.kualit.devices.application.gateways.DevicePort;
import br.com.kualit.devices.domain.entities.device.Device;

import java.util.List;

public class FilterDevices {

    private final DevicePort devicePort;

    public FilterDevices(DevicePort devicePort) {
        this.devicePort = devicePort;
    }

    public List<Device> filterDevices(Integer page, Integer size, String ... params){
        return devicePort.filterDevices(page, size, params);
    }
}
