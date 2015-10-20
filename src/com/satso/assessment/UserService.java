package com.satso.assessment;

import java.util.HashMap;
import java.util.Map;

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

    public void unLockUser(String username) throws UserLockedException {
        try {
            user.unlockUser(username);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createUser(CreateUserRequest createUserRequest) {
        User user = userRepo.load(createUserRequest.getUsername());
        if (user == null) {
            HashMap<String, User> userHashMap = userRepo.save(new User(createUserRequest));
            for (Map.Entry entry : userHashMap.entrySet()) {
                User user1 = (User) entry.getValue();
                System.out.println("username=" + entry.getKey() + ", username=" + user1.getUsername());
            }
        }
    }
}
