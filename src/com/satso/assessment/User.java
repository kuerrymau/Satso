package com.satso.assessment;

import java.util.Objects;

public class User {
    private UserDto userDto;
    private String username;
    private String password;
    private String role;
    private boolean locked = false;
    private final static int MAXIMUM_LOGIN_RETRIES = 3;
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
     * Save or update User
     * @param user
     */
    public void saveOrUpdateUser(User user){ // util method used for testing
        userRepo.save(user);
    }

    /**
     * Fetch User from repo
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
     * Returns user details only after successful login
     * @param loginRequest
     * @return
     * @throws InvalidUserCredentialsException
     */
    public UserDto login(LoginRequest loginRequest) throws InvalidUserCredentialsException {
        UserDto userDto = null;
        if (loginRequest.getPassword() != null && loginRequest.getUsername() != null) {
            userDto = load(loginRequest.getUsername());
            if (userDto != null) {
                if (!userDto.isLocked()) {
                    String password = userDto.getPassword();
                    if (password != null) {
                        if (loginRequest.getPassword().equals(password)) {
                            return userDto;
                        } else {
                            ConfigService.getInstance().setLoginRetries(++loginTries);
                            try {
                                lock(userDto, ConfigService.getInstance().getLoginRetries());
                            } catch (UserLockedException e) {
                                throw new UserLockedException("Password is incorrect");
                            }
                        }
                    } else {
                        throw new InvalidUserCredentialsException("Password not found");
                    }
                    return null;
                }
            } else {
                throw new InvalidUserCredentialsException("User not found");
            }
        }
        return userDto;
    }

    /**
     * Checks if original password is valid and changes the password
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
     * Returns true if User has role
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
     * @param userDto
     */
    public void lock(UserDto userDto, int loginRetries) throws  UserLockedException {
//        loginRetries = ConfigService.getInstance().getLoginRetries();
        if(this.loginTries >= loginRetries){

            User user = new User(new CreateUserRequest(userDto.getUsername(), userDto.getPassword(), "newRole"));
            saveLockedUser(user, true);

            throw  new UserLockedException("Maximum loginTries reached for user");
        }
    }

    public void saveLockedUser(User user, boolean lockUser) {
        user.setLocked(lockUser);

        saveOrUpdateUser(user);
    }

    /**
     * Unlocks a User and resets the login retries
     * @param username
     */
    public void unlockUser(String username) {
        if (username != null) {
            UserDto userDto = load(username);
            if (userDto != null && userDto.isLocked()) {

                User user = new User(new CreateUserRequest(userDto.getUsername(), userDto.getPassword(), userDto.getRole()));
                user.setLocked(false);

                saveOrUpdateUser(user);

                ConfigService.getInstance().setLoginRetries(0);
            }
        }
    }

    /**
     * Fetches user from repo
     * @param username
     * @return
     */
    public UserDto load(String username) {
        UserDto userDto = null;
        if (username != null) {
            User user = loadUser(username);
            if(user != null){
                userDto = new UserDto(user);
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
