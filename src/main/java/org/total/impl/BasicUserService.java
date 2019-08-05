package org.total.impl;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Collation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.total.model.User;
import org.total.repository.UserPagingRepository;
import org.total.repository.UserRepository;
import org.total.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * @author Pavlo.Fandych
 */

@Service
@Getter
@Slf4j
public class BasicUserService implements UserService {

    private final UserRepository userRepository;

    private final UserPagingRepository userPagingRepository;

    private final MongoOperations mongoOperations;

    @Autowired
    public BasicUserService(UserRepository userRepository,
                            UserPagingRepository userPagingRepository,
                            MongoOperations mongoOperations) {
        this.userRepository = userRepository;
        this.userPagingRepository = userPagingRepository;
        this.mongoOperations = mongoOperations;
    }

    @Override
    public List<User> fetchAllUsers(final Integer pageNumber,
                                    final Integer pageSize,
                                    final String sortBy) {
        log.info("BasicUserService#fetchAllUsers() called.");

        final Page<User> pagedResult = getUserPagingRepository()
                .findAll(PageRequest.of(pageNumber, pageSize, Sort.by(sortBy)));

        return pagedResult.hasContent() ? pagedResult.getContent() : Collections.emptyList();
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

        final Query query = new Query(Criteria.where("name").is(name))
                .collation(Collation.of(Locale.US).strength(Collation.ComparisonLevel.secondary()));

        return getMongoOperations().find(query, User.class);
    }

    @Override
    @CachePut(cacheNames = "userCache", unless = "#result == null", key = "#user.name")
    public User saveUser(final User user) {
        return getUserRepository().save(user);
    }
}
