package com.satso.assessment;

public final class UserDto {
    private static User user = new User();

    public UserDto(User user) {
        this.user = user;
    }

    public String getUsername() {
        return user.getUsername();
    }

    public String getPassword() {
        return user.getPassword();
    }

    public String getRole() {
        return user.getRole();
    }

    public boolean isLocked(){
        return user.isLocked();
    }
}
