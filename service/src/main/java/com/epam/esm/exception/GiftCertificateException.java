package com.epam.esm.exception;

public class GiftCertificateException extends RuntimeException {

    private final int errorCode;

    public GiftCertificateException(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
