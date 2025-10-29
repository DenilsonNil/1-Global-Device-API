package br.com.kualit.devices.infra.gateways;

import br.com.kualit.devices.domain.entities.device.Device;
import br.com.kualit.devices.domain.entities.device.DeviceStateEnum;
import br.com.kualit.devices.infra.persistence.DeviceEntity;
import br.com.kualit.devices.infra.persistence.DeviceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static br.com.kualit.devices.domain.entities.device.DeviceStateEnum.AVAILABLE;
import static br.com.kualit.devices.domain.entities.device.DeviceStateEnum.INACTIVE;
import static br.com.kualit.devices.domain.entities.device.DeviceStateEnum.IN_USE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeviceAdapterTest {

    @Mock
    private DeviceRepository deviceRepository;

    @InjectMocks
    private DeviceAdapter adapter;

    private DeviceEntity entity(Long id, String name, String brand, DeviceStateEnum state, LocalDateTime createdAt) {
        DeviceEntity e = new DeviceEntity();
        e.setId(id);
        e.setName(name);
        e.setBrand(brand);
        e.setState(state);
        e.setCreatedAt(createdAt);
        return e;
    }

    private Device device(Long id, String name, String brand, DeviceStateEnum state, LocalDateTime createdAt) {
        return Device.builder()
                .id(id)
                .name(name)
                .brand(brand)
                .state(state)
                .createdAt(createdAt)
                .build();
    }

    @Test
    void save_shouldMapToEntity_setCreatedAt_callRepository_andReturnDomain() {
        Device input = device(null, "Sensor", "Sony", AVAILABLE, null);
        DeviceEntity persisted = entity(1L, "Sensor", "Sony", AVAILABLE, LocalDateTime.now());

        when(deviceRepository.save(any(DeviceEntity.class)))
                .thenReturn(persisted);

        Device out = adapter.save(input);

        ArgumentCaptor<DeviceEntity> captor = ArgumentCaptor.forClass(DeviceEntity.class);
        verify(deviceRepository).save(captor.capture());
        DeviceEntity savedArg = captor.getValue();

        assertThat(savedArg.getId(), nullValue());
        assertThat(savedArg.getName(), equalTo("Sensor"));
        assertThat(savedArg.getBrand(), equalTo("Sony"));
        assertThat(savedArg.getState(), equalTo(AVAILABLE));
        assertThat(savedArg.getCreatedAt(), notNullValue());
        assertThat(
                Duration.between(savedArg.getCreatedAt(), LocalDateTime.now()).abs().getSeconds(),
                lessThan(5L) // margem de segurança
        );

        assertThat(out.getId(), equalTo(1L));
        assertThat(out.getName(), equalTo("Sensor"));
        assertThat(out.getBrand(), equalTo("Sony"));
        assertThat(out.getState(), equalTo(AVAILABLE));
        assertThat(out.getCreatedAt(), notNullValue());
    }

    @Test
    void update_shouldThrow_whenDeviceNotFound() {
        when(deviceRepository.findById(99L)).thenReturn(Optional.empty());

        NoSuchElementException ex = assertThrows(NoSuchElementException.class,
                () -> adapter.update(device(null, "X", "Y", AVAILABLE, null), 99L));

        assertThat(ex.getMessage(), notNullValue()); // DEVICE_NOT_FOUND
        verify(deviceRepository).findById(99L);
        verify(deviceRepository, never()).save(any());
    }

    @Test
    void update_shouldThrow_whenDeviceIsInUse() {
        DeviceEntity existing = entity(5L, "Cam", "LG", IN_USE, LocalDateTime.now().minusDays(1));
        when(deviceRepository.findById(5L)).thenReturn(Optional.of(existing));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> adapter.update(device(null, "NewCam", "LG2", AVAILABLE, null), 5L));

        assertThat(ex.getMessage(), notNullValue());
        verify(deviceRepository, never()).save(any());
    }

    @Test
    void update_shouldPatchOnlyNonNullFields_andReturnMapped() {
        DeviceEntity existing = entity(7L, "Old", "OldBrand", INACTIVE, LocalDateTime.now().minusDays(1));
        when(deviceRepository.findById(7L)).thenReturn(Optional.of(existing));

        Device patch = device(null, null, "NewBrand", AVAILABLE, null);

        DeviceEntity afterSave = entity(7L, "Old", "NewBrand", AVAILABLE, existing.getCreatedAt());
        when(deviceRepository.save(any(DeviceEntity.class)))
                .thenReturn(afterSave);

        Device out = adapter.update(patch, 7L);

        ArgumentCaptor<DeviceEntity> toSaveCaptor = ArgumentCaptor.forClass(DeviceEntity.class);
        verify(deviceRepository).save(toSaveCaptor.capture());
        DeviceEntity sent = toSaveCaptor.getValue();

        assertThat(sent.getId(), equalTo(7L));
        assertThat(sent.getName(), equalTo("Old"));        // não mudou (null no patch)
        assertThat(sent.getBrand(), equalTo("NewBrand"));  // mudou
        assertThat(sent.getState(), equalTo(AVAILABLE));   // mudou

        assertThat(out.getId(), equalTo(7L));
        assertThat(out.getName(), equalTo("Old"));
        assertThat(out.getBrand(), equalTo("NewBrand"));
        assertThat(out.getState(), equalTo(AVAILABLE));
    }

    @Test
    void findById_shouldReturnMappedDomain() {
        DeviceEntity e = entity(3L, "Printer", "Epson", AVAILABLE, LocalDateTime.now());
        when(deviceRepository.findById(3L)).thenReturn(Optional.of(e));

        Device out = adapter.findById(3L);

        assertThat(out.getId(), equalTo(3L));
        assertThat(out.getName(), equalTo("Printer"));
        assertThat(out.getBrand(), equalTo("Epson"));
        assertThat(out.getState(), equalTo(AVAILABLE));
        verify(deviceRepository).findById(3L);
    }

    @Test
    void findById_shouldThrow_whenNotFound() {
        when(deviceRepository.findById(404L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> adapter.findById(404L));
        verify(deviceRepository).findById(404L);
    }

    @Test
    void filterDevices_shouldPassBrandAndStateAndPaging_andMapResults() {
        DeviceEntity e1 = entity(1L, "A", "Sony", AVAILABLE, LocalDateTime.now());
        DeviceEntity e2 = entity(2L, "B", "Sony", INACTIVE, LocalDateTime.now());
        Page<DeviceEntity> page = new PageImpl<>(List.of(e1, e2));

        when(deviceRepository.filterDevices(eq("Sony"), eq(AVAILABLE), any(PageRequest.class)))
                .thenReturn(page);

        List<Device> out = adapter.filterDevices(0, 10, "Sony", "AVAILABLE");

        assertThat(out, hasSize(2));
        assertThat(out.get(0).getId(), equalTo(1L));
        assertThat(out.get(1).getId(), equalTo(2L));

        ArgumentCaptor<PageRequest> pr = ArgumentCaptor.forClass(PageRequest.class);
        verify(deviceRepository).filterDevices(eq("Sony"), eq(AVAILABLE), pr.capture());
        assertThat(pr.getValue().getPageNumber(), equalTo(0));
        assertThat(pr.getValue().getPageSize(), equalTo(10));
    }

    @Test
    void findAll_shouldQueryWithPaging_andMapResults() {
        DeviceEntity e1 = entity(10L, "S1", "ABC", AVAILABLE, LocalDateTime.now());
        DeviceEntity e2 = entity(11L, "S2", "DEF", INACTIVE, LocalDateTime.now());
        Page<DeviceEntity> page = new PageImpl<>(List.of(e1, e2));

        when(deviceRepository.findAll(any(PageRequest.class)))
                .thenReturn(page);

        List<Device> out = adapter.findAll(2, 5);

        assertThat(out, hasSize(2));
        assertThat(out.get(0).getId(), equalTo(10L));
        assertThat(out.get(1).getId(), equalTo(11L));

        ArgumentCaptor<PageRequest> pr = ArgumentCaptor.forClass(PageRequest.class);
        verify(deviceRepository).findAll(pr.capture());
        assertThat(pr.getValue().getPageNumber(), equalTo(2));
        assertThat(pr.getValue().getPageSize(), equalTo(5));
    }

    @Test
    void delete_shouldThrow_whenInUse() {
        DeviceEntity existing = entity(50L, "Dev", "Brand", IN_USE, LocalDateTime.now());
        when(deviceRepository.findById(50L)).thenReturn(Optional.of(existing));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> adapter.delete(50L));
        assertThat(ex.getMessage(), notNullValue()); // IN_USE_DEVICES_CANNOT_BE_DELETED
        verify(deviceRepository, never()).deleteById(any(Long.class));
    }

    @Test
    void delete_shouldDelete_whenNotInUse() {
        DeviceEntity existing = entity(51L, "Dev", "Brand", AVAILABLE, LocalDateTime.now());
        when(deviceRepository.findById(51L)).thenReturn(Optional.of(existing));
        adapter.delete(51L);
        verify(deviceRepository).findById(51L);
        verify(deviceRepository).deleteById(51L);
    }
}
