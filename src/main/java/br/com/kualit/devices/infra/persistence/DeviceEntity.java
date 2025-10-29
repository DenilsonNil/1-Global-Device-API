package br.com.kualit.devices.infra.persistence;

import br.com.kualit.devices.domain.entities.device.DeviceStateEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "devices")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeviceEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "brand", length = 100)
    private String brand;

    @Column(name = "state", length = 80)
    @Enumerated(EnumType.STRING)
    private DeviceStateEnum state;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
