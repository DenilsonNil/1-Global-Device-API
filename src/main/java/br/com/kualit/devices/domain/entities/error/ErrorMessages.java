package br.com.kualit.devices.domain.entities.error;

public abstract class ErrorMessages {

    private ErrorMessages() {}

    public static final String DEVICE_NOT_FOUND = "Device not found";
    public static final String IN_USE_DEVICES_CANNOT_BE_UPDATED = "In use devices cannot be updated";
    public static final String IN_USE_DEVICES_CANNOT_BE_DELETED = "In use devices cannot be deleted";
}
