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

    /**
     * save or update User
     * @param user
     */
    public void saveOrUpdateUser(User user){ // util method used for testing
        userRepo.save(user);
    }

    /**
     * fetch User from repo
     * @param username
     * @return
     */
    public User loadUser(String username) {
        User user = null;
        if (username != null) {
            user = userRepo.load(username);
        }
        return user;
    }

    /**
     * returns user details only after successful login
     * @param loginRequest
     * @return
     * @throws InvalidUserCredentialsException
     */
    public UserDto login(LoginRequest loginRequest) throws InvalidUserCredentialsException {
        UserDto userDto = null;
        if (loginRequest.getPassword() != null && loginRequest.getUsername() != null) {

            this.password = loginRequest.getPassword();
            this.username = loginRequest.getUsername();

            ConfigService.getInstance().setLoginRetries(++loginTries);

            try {
                lock();
            } catch (UserLockedException e) {
                e.printStackTrace();
            }

            userDto = load(loginRequest.getUsername());
            if (userDto != null) {
                if (!userDto.isLocked()) {
                    String password = userDto.getPassword();
                    if (password != null) {
                        if (loginRequest.getPassword().equals(password)) {
                            return userDto;
                        } else {
                            throw new InvalidUserCredentialsException("Password not correct");
                        }
                    } else {
                        throw new InvalidUserCredentialsException("Password not found");
                    }
                }
            } else {
                throw new InvalidUserCredentialsException("User not found");
            }
        }
        return userDto;
    }

    /**
     * checks if original password is valid and changes the passord
     * @param request
     * @throws InvalidUserCredentialsException
     */
    public void changePassword(ChangePasswordRequest request) throws InvalidUserCredentialsException {
        if (request.getPassword() != null) {
            UserDto userDto = load(request.getUsername());
            if (userDto != null) {
                if (userDto.getPassword().equals(request.getPassword())) {
                    this.password = request.getNewPassword();
                    this.username = request.getUsername();

                    saveOrUpdateUser(this);
                } else {
                    throw new InvalidUserCredentialsException("Invalid user password used");
                }
            } else {
                throw new InvalidUserCredentialsException("Invalid user username used");
            }
        }
    }

    /**
     * return true if User has role
     * @param request
     * @return
     */
    public boolean hasRole(HasRoleRequest request) {
        boolean hasRole = false;
        if (request.getUsername() != null) {
            User user = loadUser(request.getUsername());
            if (user != null) {
                if ((user.getRole() != null && !user.getRole().isEmpty()) && user.getRole() == request.getRole()) {
                    hasRole = true;
                } else {
                    hasRole = false;
                }
            }
        }
        return hasRole;
    }

    /**
     * Keeps track of number of failed logins and locks after three failures
     * @throws UserLockedException
     */
    private void lock() throws  UserLockedException {
        int maximumTries = 3;
        int loginRetries = ConfigService.getInstance().getLoginRetries();
        if(loginRetries == maximumTries){
            this.locked = true;
            saveOrUpdateUser(this);
            throw  new UserLockedException("Maximum loginTries reached for user");
        }
    }

    /**
     * Unlocks a User and resets the login retries
     * @param username
     */
    public void unlockUser(String username) {
        if (username != null) {
            UserDto userDto = load(username);
            if (userDto != null && userDto.isLocked()) {
                this.password = userDto.getPassword();
                this.username = userDto.getUsername();
                this.locked = false;

                saveOrUpdateUser(this);
                ConfigService.getInstance().setLoginRetries(0);
            }
        }
    }

    /**
     * fetches user from repo
     * @param username
     * @return
     */
    public UserDto load(String username) {
        UserDto userDto = null;
        if (username != null) {
            User user = loadUser(username);
            if(user != null){
//                this.user = user; // FIXME
                userDto = createDto();
            }
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
