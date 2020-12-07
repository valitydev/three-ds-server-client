package com.rbkmoney.threeds.server.client.exception;

public class UnsupportedDeviceChannelException extends UnsupportedOperationException {

    public UnsupportedDeviceChannelException() {
    }

    public UnsupportedDeviceChannelException(String message) {
        super(message);
    }

    public UnsupportedDeviceChannelException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedDeviceChannelException(Throwable cause) {
        super(cause);
    }
}
