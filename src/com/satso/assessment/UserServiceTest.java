package com.satso.assessment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

        @org.junit.Test
//    @org.junit.Test(expected=InvalidUserCredentialsException.class)
    public void testLoginSuccess() {
        // setup
        UserService sut = new UserService();
        String username = "username";
        String password = "password";
        String role = "role";
        // test
        sut.createUser(new CreateUserRequest(username, password, role));

        UserDto userDto = sut.login(new LoginRequest(username, password));
        // verify
        assertNotNull(userDto);  // FIXME toggle with expected exception
    }

    //    @org.junit.Test
    @org.junit.Test(expected=InvalidUserCredentialsException.class)
    public void testLoginInvalidUsername() {
        // setup
        UserService sut = new UserService();
        String username = "username";
        String password = "password";
        String role = "role";
        // test
        sut.createUser(new CreateUserRequest(username, password, role));

        String invalidPassword = "invalidPassword";

        UserDto userDto = sut.login(new LoginRequest(invalidPassword, password));
        // verify
//        assertNull(userDto);  FIXME toggle with expected exception
    }

    //    @org.junit.Test
    @org.junit.Test(expected=InvalidUserCredentialsException.class)
    public void testLoginInvalidPassword() {
        // setup
        UserService sut = new UserService();
        String username = "username";
        String password = "password";
        String role = "role";
        // test
        sut.createUser(new CreateUserRequest(username, password, role));

        UserDto userDto = sut.login(new LoginRequest(username, "invalid"));
        // verify
//        assertNull(userDto); FIXME toggle with expected exception
    }

    //    @org.junit.Test
    @org.junit.Test(expected=InvalidUserCredentialsException.class)
    public void testChangePassword() {
        // setup
        UserService sut = new UserService();
        String username = "username";
        String password = "password";
        String role = "role";
        // test
        sut.createUser(new CreateUserRequest(username, password, role));

        String newpassword = "newpassword";
        sut.changePassword(new ChangePasswordRequest(newpassword, username, password));

        UserDto dto = sut.loadUser(username);
        // verify
        String myPassword = dto.getPassword();
        System.out.print("newpassword=" + myPassword);
//        assertEquals(newpassword, myPassword); FIXME toggle with expected exception
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

        boolean hasRole = sut.hasRole(new HasRoleRequest(username, role));

        UserDto dto = sut.loadUser(username);
        System.out.print("role=" + dto.getRole());
        // verify
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

        int numberOfTries = 5;
        for(int i = 0; i < numberOfTries; i++) {
            sut.login(new LoginRequest(username, password));
        }
        UserDto dto = sut.loadUser(username);
//        assertEquals(dto.isLocked(), true);  FIXME toggle with expected exception
    }

    @org.junit.Test
    public void testUnlockUser() {
        // setup
        UserService sut = new UserService();
        String username = "username";
        String password = "password";
        String role = "role";
        // test
        sut.createUser(new CreateUserRequest(username, password, role));

        sut.unLockUser(username);
        // verify
        assertEquals(ConfigService.getInstance().getLoginRetries(), 0);
    }
}
