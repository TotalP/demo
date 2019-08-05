package org.total.service;

import org.total.model.User;

import java.util.List;

/**
 * @author Pavlo.Fandych
 */

public interface UserService {

    List<User> fetchAllUsers();

    User fetchUserByName(final String name);

    List<User> fetchUserByNameCaseInsensitive(final String name);

    void saveUser(final User user);
}
