package com.satso.assessment;

public class UserLockedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UserLockedException() {
    }

    public UserLockedException(String arg0) {
        super(arg0);
    }

    public UserLockedException(Throwable arg0) {
        super(arg0);
    }

    public UserLockedException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

}
