package org.total.service;

import org.total.model.User;

import java.util.List;

/**
 * @author Pavlo.Fandych
 */

public interface UserService {

    List<User> fetchAllUsers(final Integer pageNumber,
                             final Integer pageSize,
                             final String sortBy);

    User fetchUserById(final String id);

    User fetchUserByName(final String name);

    List<User> fetchUserByNameCaseInsensitive(final String name);

    User saveUser(final User user);

    Long deleteUserByName(final String name);
}
