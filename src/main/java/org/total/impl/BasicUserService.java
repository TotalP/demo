package org.total.impl;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Collation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.total.model.User;
import org.total.repository.UserRepository;
import org.total.service.UserService;

import java.util.List;
import java.util.Locale;

/**
 * @author Pavlo.Fandych
 */

@Service
@Getter
@Slf4j
public class BasicUserService implements UserService {

    private UserRepository userRepository;

    private MongoOperations mongoOperations;

    @Autowired
    public BasicUserService(UserRepository userRepository, MongoOperations mongoOperations) {
        this.userRepository = userRepository;
        this.mongoOperations = mongoOperations;
    }

    @Override
    public List<User> fetchAllUsers() {
        log.info("BasicUserService#fetchAllUsers() called.");

        return getUserRepository().findAll();
    }

    @Override
    @Cacheable(cacheNames = "userCache", unless = "#result == null", key = "#name")
    public User fetchUserByName(final String name) {
        log.info("BasicUserService#fetchUserByName() touched the database.");

        return getUserRepository().findByName(name);
    }

    @Override
    public List<User> fetchUserByNameCaseInsensitive(final String name) {
        log.info("BasicUserService#fetchUserByNameCaseInsensitive() touched the database.");

        Query query = new Query(Criteria.where("name").is(name))
                .collation(Collation.of(Locale.US).strength(Collation.ComparisonLevel.secondary()));

        return mongoOperations.find(query, User.class);
    }

    @Override
    public void saveUser(final User user) {
        getUserRepository().save(user);
    }
}
