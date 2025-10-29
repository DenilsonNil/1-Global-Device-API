package br.com.kualit.devices.domain.entities.devices;

import br.com.kualit.devices.domain.entities.device.Device;
import br.com.kualit.devices.domain.entities.device.DeviceStateEnum;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class DeviceTest {

    @Test
    void shouldBuildDeviceUsingBuilder() {
        LocalDateTime now = LocalDateTime.now();

        Device device = Device.builder()
                .id(1L)
                .name("Sensor de Temperatura")
                .brand("Sony")
                .state(DeviceStateEnum.AVAILABLE)
                .createdAt(now)
                .build();

        assertThat(device).isNotNull();
        assertThat(device.getId()).isEqualTo(1L);
        assertThat(device.getName()).isEqualTo("Sensor de Temperatura");
        assertThat(device.getBrand()).isEqualTo("Sony");
        assertThat(device.getState()).isEqualTo(DeviceStateEnum.AVAILABLE);
        assertThat(device.getCreatedAt()).isEqualTo(now);
    }

    @Test
    void shouldAllowModificationOfFields() {
        Device device = new Device();
        LocalDateTime created = LocalDateTime.now();

        device.setId(2L);
        device.setName("Câmera");
        device.setBrand("Samsung");
        device.setState(DeviceStateEnum.AVAILABLE);
        device.setCreatedAt(created);

        assertThat(device.getId()).isEqualTo(2L);
        assertThat(device.getName()).isEqualTo("Câmera");
        assertThat(device.getBrand()).isEqualTo("Samsung");
        assertThat(device.getState()).isEqualTo(DeviceStateEnum.AVAILABLE);
        assertThat(device.getCreatedAt()).isEqualTo(created);
    }

    @Test
    void shouldCompareTwoDevicesWithSameValuesAsEqual() {
        LocalDateTime createdAt = LocalDateTime.now();

        Device d1 = Device.builder()
                .id(10L)
                .name("Scanner")
                .brand("HP")
                .state(DeviceStateEnum.AVAILABLE)
                .createdAt(createdAt)
                .build();

        Device d2 = Device.builder()
                .id(10L)
                .name("Scanner")
                .brand("HP")
                .state(DeviceStateEnum.AVAILABLE)
                .createdAt(createdAt)
                .build();

        assertThat(d1).isEqualTo(d2);
        assertThat(d1.hashCode()).isEqualTo(d2.hashCode());
    }

    @Test
    void shouldReturnReadableToString() {
        Device device = Device.builder()
                .id(3L)
                .name("Impressora")
                .brand("Epson")
                .state(DeviceStateEnum.AVAILABLE)
                .build();

        String text = device.toString();

        assertThat(text)
                .contains("id=3")
                .contains("Impressora")
                .contains("Epson")
                .contains("AVAILABLE");
    }
}
