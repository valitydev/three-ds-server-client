package com.rbkmoney.three.ds.server.client.exception;

public class Mpi3DsClientException extends RuntimeException {

    public Mpi3DsClientException() {
        super();
    }

    public Mpi3DsClientException(String message) {
        super(message);
    }

    public Mpi3DsClientException(Throwable cause) {
        super(cause);
    }

    public Mpi3DsClientException(String message, Throwable cause) {
        super(message, cause);
    }

}