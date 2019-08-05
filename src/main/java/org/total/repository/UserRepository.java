package org.total.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.total.model.User;

/**
 * @author Pavlo.Fandych
 */

public interface UserRepository extends MongoRepository<User, String> {

    User findByName(String name);
}