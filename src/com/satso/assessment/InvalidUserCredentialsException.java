package com.satso.assessment;

public class InvalidUserCredentialsException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvalidUserCredentialsException() {
    }

    public InvalidUserCredentialsException(String arg0) {
        super(arg0);
    }

    public InvalidUserCredentialsException(Throwable arg0) {
        super(arg0);
    }

    public InvalidUserCredentialsException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }
}
