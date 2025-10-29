package br.com.kualit.devices.infra.gateways;

import br.com.kualit.devices.domain.entities.device.Device;
import br.com.kualit.devices.domain.entities.device.DeviceStateEnum;
import br.com.kualit.devices.infra.persistence.DeviceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;


import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@Import(DeviceAdapter.class)
class DeviceAdapterIntegrationTest {

    @Autowired
    private DeviceAdapter adapter;

    @Autowired
    private DeviceRepository repository;

    @Test
    void save_shouldPersistToH2_andReturnDomain() {
        Device d = Device.builder()
                .name("Camera")
                .brand("LG")
                .state(DeviceStateEnum.AVAILABLE)
                .build();

        Device out = adapter.save(d);

        assertThat(out.getId()).isNotNull();
        assertThat(out.getCreatedAt()).isNotNull();

        var db = repository.findById(out.getId()).orElseThrow();
        assertThat(db.getName()).isEqualTo("Camera");
        assertThat(db.getState()).isEqualTo(DeviceStateEnum.AVAILABLE);
    }

    @Test
    void update_shouldPatchOnlyNonNullFields() {
        var created = adapter.save(Device.builder()
                .name("Old")
                .brand("Brand")
                .state(DeviceStateEnum.INACTIVE)
                .build());

        var updated = adapter.update(
                Device.builder().brand("BrandB").state(DeviceStateEnum.AVAILABLE).build(),
                created.getId()
        );

        assertThat(updated.getName()).isEqualTo("Old");
        assertThat(updated.getBrand()).isEqualTo("BrandB");
        assertThat(updated.getState()).isEqualTo(DeviceStateEnum.AVAILABLE);
    }
}
