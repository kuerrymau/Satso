package com.satso.assessment;

public final class ConfigService {
    public static ConfigService getInstance() {
        return new ConfigService();
    }

    private static int loginRetries;

    public int getLoginRetries() {
        return this.loginRetries;
    }

    public static void setLoginRetries(int loginRetries) {
        loginRetries = loginRetries;
    }
}
