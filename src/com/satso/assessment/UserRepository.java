package com.satso.assessment;

import java.util.HashMap;

public class UserRepository {
//
    static HashMap<String, User> map = new HashMap<String, User>();

    private static final long serialVersionUID = 1L;

    /**
     *
     * @param user
     */
    public HashMap<String, User> save(User user) { // FIXME remove return statement (put there for debugging)
        map.put(user.getUsername(), user);
        return map;
    }

    /**
     *
     * @param username
     * @return
     */
    public User load(String username) { // FIXME remove return statement (put there for debugging)
        User user = map.get(username);
        return user;
    }
}
