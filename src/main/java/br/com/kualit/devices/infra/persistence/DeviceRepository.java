package br.com.kualit.devices.infra.persistence;

import br.com.kualit.devices.domain.entities.device.DeviceStateEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<DeviceEntity, Long> {

    @Query("SELECT d FROM DeviceEntity d WHERE d.brand = ?1 OR d.state = ?2")
    Page<DeviceEntity> filterDevices(String brand, DeviceStateEnum state, Pageable pageable);

    @Query("SELECT d FROM DeviceEntity d")
    Page<DeviceEntity> findAll(Pageable pageable);
}
