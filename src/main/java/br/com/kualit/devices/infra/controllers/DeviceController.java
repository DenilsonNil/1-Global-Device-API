package br.com.kualit.devices.infra.controllers;

import br.com.kualit.devices.application.usecases.*;
import br.com.kualit.devices.domain.entities.device.DeviceStateEnum;
import br.com.kualit.devices.infra.mappers.DeviceMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static br.com.kualit.devices.infra.mappers.DeviceMapper.toDomain;
import static br.com.kualit.devices.infra.mappers.DeviceMapper.toResponseDTO;

@RestController
@RequestMapping("/devices")
public class DeviceController {

    private final CreateNewDevice createNewDevice;
    private final FindDeviceById findDeviceById;
    private final FilterDevices findDeviceByBrand;
    private final DeleteDeviceById deleteDeviceById;
    private final FindAllDevices findAllDevices;
    private final UpdateDevice updateDevice;

    public DeviceController(CreateNewDevice createNewDevice, FindDeviceById findDeviceById, FilterDevices findDeviceByBrand, DeleteDeviceById deleteDeviceById, FindAllDevices findAllDevices, UpdateDevice updateDevice) {
        this.createNewDevice = createNewDevice;
        this.findDeviceById = findDeviceById;
        this.findDeviceByBrand = findDeviceByBrand;
        this.deleteDeviceById = deleteDeviceById;
        this.findAllDevices = findAllDevices;
        this.updateDevice = updateDevice;
    }

    @PostMapping
    public ResponseEntity<DeviceResponseDTO> create(@RequestBody @Valid DeviceCreationRequestDTO requestDTO) {
        var device = toDomain(requestDTO);
        var savedDevice = createNewDevice.save(device);
        var response = toResponseDTO(savedDevice);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<DeviceResponseDTO> findById(@PathVariable Long id) {
        var device = findDeviceById.findById(id);
        var response = toResponseDTO(device);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<DeviceResponseDTO>> filterDevices(
            @RequestParam(required = false)  String brand,
            @RequestParam(required = false) DeviceStateEnum state,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size

    ) {
        String[] params = {brand, state == null ? null : state.name()};

        if(page == null || page < 0) page = 0;
        if(size == null || size < 0) size = 10;

        /**
         * If no params are passed, then it will return all devices**/
        var devices = params[0] == null && params[1] == null ?
                findAllDevices.findAll(page, size) :
                findDeviceByBrand.filterDevices(page, size, params);

        //Http response headers
        var responseHeaders = new HttpHeaders();
        responseHeaders.add("X-Total-Count", String.valueOf(devices.size()));
        responseHeaders.add("X-Page-Number", String.valueOf(page));
        responseHeaders.add("X-Page-Size", String.valueOf(size));

        var response = devices.stream()
                .map(DeviceMapper::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok().headers(responseHeaders).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long id) {
        deleteDeviceById.deleteDeviceById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{deviceId}")
    public ResponseEntity<DeviceResponseDTO> updateDevice(@RequestBody DeviceCreationRequestDTO requestDTO,
                                                          @PathVariable Long deviceId) {
        var device = toDomain(requestDTO);
        var updatedDevice = updateDevice.update(device, deviceId);
        var response = toResponseDTO(updatedDevice);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{deviceId}")
    public ResponseEntity<DeviceResponseDTO> updateDevicePartially(@RequestBody DeviceUpdatingRequestDTO requestDTO,
                                                          @PathVariable Long deviceId) {
        var device = toDomain(requestDTO);
        var updatedDevice = updateDevice.update(device, deviceId);
        var response = toResponseDTO(updatedDevice);
        return ResponseEntity.ok(response);
    }
}
