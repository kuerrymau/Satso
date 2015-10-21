package com.satso.assessment;

import static org.junit.Assert.assertEquals;

public class UserServiceTest {
    @org.junit.Test
    public void testCreateUserSuccess() {
        // setup
        UserService sut = new UserService();
        String username = "username";
        String password = "password";
        String role = "role";
        // test
        sut.createUser(new CreateUserRequest(username, password, role));
        // verify
        UserDto dto = sut.loadUser(username);
        assertEquals(username, dto.getUsername());
        assertEquals(password, dto.getPassword());
        assertEquals(role, dto.getRole());
    }

    @org.junit.Test(expected=InvalidUserCredentialsException.class)
    public void testLoginSuccess() {
        // setup
        UserService sut = new UserService();
        String username = "username";
        String password = "password";
        String role = "role";
        // test
        sut.createUser(new CreateUserRequest(username, password, role));

        sut.login(new LoginRequest(username, password)); // successful
//        sut.login(new LoginRequest(username, "newPassword")); // invalid password
        // verify
//        assertNotNull(userDto);
    }

    @org.junit.Test(expected=InvalidUserCredentialsException.class)
    public void testLoginInvalidUsername() {
        // setup
        UserService sut = new UserService();
        String username = "username";
        String password = "password";
        String role = "role";
        // test
        sut.createUser(new CreateUserRequest(username, password, role));

          String invalidUsername = "invalidUsername";

          // verify
          UserDto userDto = sut.login(new LoginRequest(invalidUsername, password));
//        assertNull(userDto);
    }

    @org.junit.Test(expected=InvalidUserCredentialsException.class)
    public void testLoginInvalidPassword() {
        // setup
        UserService sut = new UserService();
        String username = "username";
        String password = "password";
        String role = "role";
        // test
        sut.createUser(new CreateUserRequest(username, password, role));

        String invalidUsername = "invalidUsername";

        // verify
        UserDto userDto = sut.login(new LoginRequest(invalidUsername, password));
//        assertNull(userDto);
    }

    @org.junit.Test(expected=InvalidUserCredentialsException.class)
    public void testChangePassword() {
        // setup
        UserService sut = new UserService();
        String username = "username";
        String password = "password";
        String role = "role";
        // test
        sut.createUser(new CreateUserRequest(username, password, role));

        // verify
        String myNewPassword = "myNewPassword";
        String anotherNewPassword = "anotherNewPassword";
        sut.changePassword(new ChangePasswordRequest(myNewPassword, username, anotherNewPassword));
    }

    @org.junit.Test
    public void testHasRole() {
        // setup
        UserService sut = new UserService();
        String username = "username";
        String password = "password";
        String role = "role";
        // test
        sut.createUser(new CreateUserRequest(username, password, role));

        String newRole = "newRole";
        boolean hasRole = sut.hasRole(new HasRoleRequest(username, role));

        // verify
        UserDto dto = sut.loadUser(username);
        assertEquals(hasRole, true);
    }

//    @org.junit.Test
    @org.junit.Test(expected=UserLockedException.class)
    public void testUserLocked() {
        // setup
        UserService sut = new UserService();
        String username = "username";
        String password = "password";
        String role = "role";
        // test
        sut.createUser(new CreateUserRequest(username, password, role));

        // verify
        UserDto dto = sut.loadUser(username);

        int loginRetries = 3;

        sut.lock(dto, loginRetries);
    }

    @org.junit.Test
    public void testUnlockUser() {
        // setup
        UserService sut = new UserService();
        String username = "username";
        String password = "password";
        String role = "role";
        // test
        CreateUserRequest createUserRequest = new CreateUserRequest(username, password, role);

        sut.createUser(createUserRequest);

        // verify
        User user = new User(createUserRequest);

        boolean lockUser = true;

        sut.saveLockedUser(user, lockUser);

        sut.unLockUser(username);

        UserDto dto = sut.loadUser(username);

        assertEquals(ConfigService.getInstance().getLoginRetries(), 0);
        assertEquals(dto.isLocked(), false);
    }
}
