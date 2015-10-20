package com.satso.assessment;

public final class ConfigService {
    private static ConfigService configService = new ConfigService( );
    public static ConfigService getInstance() {
        return configService;
    }

    private static int loginRetries;

    public static int getLoginRetries() {
        return loginRetries;
    }

    public static void setLoginRetries(int logins) {
        loginRetries = logins;
    }
}
