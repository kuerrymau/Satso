package com.satso.assessment;

import java.util.Objects;

public class User {
    private UserDto userDto;
    private String username;
    private String password;
    private String role;
    private boolean locked = false;
    private int loginTries = ConfigService.getInstance().getLoginRetries();

    private UserRepository userRepo = new UserRepository();

    public User() {

    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getRole() {
        return this.role;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public User(CreateUserRequest request) {
        this.username = request.getUsername();
        this.password = request.getPassword();
        this.role = request.getRole();
        this.locked = locked;
    }

    public UserDto createDto() {
        this.userDto = new UserDto(this);
        return userDto;
    }// done

    public UserDto loadUser(String username) {
        return new UserDto(userRepo.load(username)); // done

    }

    public UserDto login(LoginRequest loginRequest) throws  InvalidUserCredentialsException {
        ConfigService.getInstance().setLoginRetries(loginTries++);
        try {
            lock();
        } catch (UserLockedException e) {
            e.printStackTrace();
        }

        UserDto userDto = load(loginRequest.getUsername());
        if (userDto != null) {
            String password = userDto.getPassword();
            if (password != null) {
                if(loginRequest.getPassword().equals(password)){
                    return userDto;
                }
            }
        }

        return userDto;
    }// done

    public void changePassword(ChangePasswordRequest request) throws InvalidUserCredentialsException {// done
        UserDto userDto = load(request.getUsername());
        if (userDto != null) {
            if (this.password.equals(request.getPassword())) {
                this.password = request.getNewPassword();
                this.username = request.getUsername();
                userRepo.save(this);
            } else {
                throw new InvalidUserCredentialsException("Invalid use password used");
            }
        } else {
            throw new InvalidUserCredentialsException("Invalid use username used");
        }
    }

    public boolean hasRole(HasRoleRequest request) {// done
    boolean hasRole = false;
        User user = userRepo.load(request.getUsername());
        if (user != null) {
            if (user.getRole() != null) {
                hasRole = true;
            } else {
                hasRole = false;
            }
        }
        return hasRole;
    }

    private void lock() throws  UserLockedException { // done
        int maximumTries = 3;
        int loginRetries = ConfigService.getInstance().getLoginRetries();
        if(loginRetries == maximumTries){
            this.locked = true;
            throw  new UserLockedException("Maximum loginTries reached for user");
        }
    }

    public void unlockUser() { // done
        this.locked = false;
        ConfigService.getInstance().setLoginRetries(0);
    }

    public UserDto load(String username) {
        return new UserDto(userRepo.load(username)); // done
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(getUsername(), user.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername());
    }
}
