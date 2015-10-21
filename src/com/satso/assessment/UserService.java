package com.satso.assessment;

public class UserService {
    private CreateUserRequest createUserRequest;
    private User user = new User();
    private UserRepository userRepo = new UserRepository();

    public UserDto loadUser(String username) {
        return new UserDto(userRepo.load(username));
    }

    public UserDto login(LoginRequest loginRequest) throws InvalidUserCredentialsException {
        return user.login(loginRequest);
    }

    public void changePassword(ChangePasswordRequest changePasswordRequest) throws InvalidUserCredentialsException {
        user.changePassword(changePasswordRequest);
    }

    public boolean hasRole(HasRoleRequest hasRoleRequest) {
        return user.hasRole(hasRoleRequest);
    }

    public void unLockUser(String username) {
         user.unlockUser(username);
    }

    public void saveLockedUser(User user, boolean lockUser) {
        user.setLocked(lockUser);

        user.saveOrUpdateUser(user);
    }

    public void lock(UserDto userDto, int loginRetries) {
        user.lock(userDto, loginRetries);
    }

    public void createUser(CreateUserRequest createUserRequest) {
        if (createUserRequest.getPassword() != null && createUserRequest.getUsername() != null) {
            User user = userRepo.load(createUserRequest.getUsername());
            if (user == null) {
                userRepo.save(new User(createUserRequest));
            }
        }
    }
}
