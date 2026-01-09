package com.BookMySeat.tickets.exceptions;

public class QrCodeNotfoundException extends EventTicketException{
    public QrCodeNotfoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public QrCodeNotfoundException(Throwable cause) {
        super(cause);
    }

    public QrCodeNotfoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public QrCodeNotfoundException(String message) {
        super(message);
    }

    public QrCodeNotfoundException() {
    }
}
