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
    }

    public UserDto loadUser(String username) {
        UserDto userDto = null;
        if (username != null) {
            userDto = new UserDto(userRepo.load(username));
        }
        return userDto;
    }

    public UserDto login(LoginRequest loginRequest) throws InvalidUserCredentialsException {
        UserDto userDto = null;
        if (loginRequest.getPassword() != null && loginRequest.getUsername() != null) {
            ConfigService.getInstance().setLoginRetries(++loginTries);
            try {
                lock();
            } catch (UserLockedException e) {
                e.printStackTrace();
            }

            userDto = load(loginRequest.getUsername());
            if (userDto != null && !userDto.isLocked()) {
                String password = userDto.getPassword();
                if (password != null) {
                    if (loginRequest.getPassword().equals(password)) {
                        return userDto;
                    }
                } else {
                    throw new InvalidUserCredentialsException("Password not found");
                }
            } else {
                throw new InvalidUserCredentialsException("User not found");
            }
        }
        return userDto;
    }

    public void changePassword(ChangePasswordRequest request) throws InvalidUserCredentialsException {
        if (request.getPassword() != null && request.getUsername() != null) {
            UserDto userDto = load(request.getUsername());
            if (userDto != null) {
                if (this.password.equals(request.getPassword())) {
                    this.password = request.getNewPassword();
                    this.username = request.getUsername();

                    userRepo.save(this);
                } else {
                    throw new InvalidUserCredentialsException("Invalid user password used");
                }
            } else {
                throw new InvalidUserCredentialsException("Invalid user username used");
            }
        }
    }

    public boolean hasRole(HasRoleRequest request) {
        boolean hasRole = false;
        if (request.getUsername() != null) {
            User user = userRepo.load(request.getUsername());
            if (user != null) {
                if (user.getRole() != null && !user.getRole().isEmpty()) {
                    hasRole = true;
                } else {
                    hasRole = false;
                }
            }
        }
        return hasRole;
    }

    private void lock() throws  UserLockedException {
        int maximumTries = 3;
        int loginRetries = ConfigService.getInstance().getLoginRetries();
        if(loginRetries == maximumTries){
            this.locked = true;
            userRepo.save(this);
            throw  new UserLockedException("Maximum loginTries reached for user");
        }
    }

    public void unlockUser(String username) {
        if (username != null) {
            UserDto userDto = load(username);
            if (userDto != null && userDto.isLocked()) {
                this.password = userDto.getPassword();
                this.username = userDto.getUsername();
                this.locked = false;

                userRepo.save(this);
                ConfigService.getInstance().setLoginRetries(0);
            }
        }
    }

    public UserDto load(String username) {
        UserDto userDto = null;
        if (username != null) {
            userDto = new UserDto(userRepo.load(username));
        }
        return userDto;
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
