package com.satso.assessment;

public final class CreateUserRequest extends LoginRequest{
    private String username;
    private String password;
    private String role;

    public CreateUserRequest(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getRole() {
        return this.role;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
