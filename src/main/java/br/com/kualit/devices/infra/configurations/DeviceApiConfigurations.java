package br.com.kualit.devices.infra.configurations;

import br.com.kualit.devices.application.usecases.*;
import br.com.kualit.devices.infra.gateways.DeviceAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeviceApiConfigurations {

    @Bean
    public CreateNewDevice createNewDevice(DeviceAdapter deviceAdapter) {
        return new CreateNewDevice(deviceAdapter);
    }

    @Bean
    public FindDeviceById findDeviceById(DeviceAdapter deviceAdapter) {
        return new FindDeviceById(deviceAdapter);
    }

    @Bean
    public FilterDevices findDeviceByBrand(DeviceAdapter deviceAdapter) {
        return new FilterDevices(deviceAdapter);
    }

    @Bean
    public DeleteDeviceById deleteDeviceById(DeviceAdapter deviceAdapter) {
        return new DeleteDeviceById(deviceAdapter);
    }

    @Bean
    public FindAllDevices findAllDevices(DeviceAdapter deviceAdapter) {
        return new FindAllDevices(deviceAdapter);
    }

    @Bean
    public UpdateDevice updateDevice(DeviceAdapter deviceAdapter) {
        return new UpdateDevice(deviceAdapter);
    }
}
