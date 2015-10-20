package com.satso.assessment;

public final class HasRoleRequest {
    private String username;
    private String role;

    public HasRoleRequest(String username, String role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

}
