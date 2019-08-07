package org.total.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.total.dto.UserDTO;
import org.total.model.User;
import org.total.service.UserService;
import org.total.util.UserConverter;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

import static org.total.util.Constants.USER_DOES_NOT_EXIST;

/**
 * @author Pavlo.Fandych
 */

@RestController
@RequestMapping("/api/users")
@Slf4j
@Getter
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(@RequestParam(defaultValue = "0") Integer pageNumber,
                                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                                  @RequestParam(defaultValue = "name") String sortBy) {
        log.info("UserController#getAllUsers() called.");
        final List<User> users = getUserService().fetchAllUsers(pageNumber, pageSize, sortBy);

        if (users != null && !users.isEmpty()) {
            return new ResponseEntity<>(users, HttpStatus.OK);
        } else {
            log.info("Users collection is empty");

            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/{name}")
    public ResponseEntity<Object> getUserByName(final @PathVariable("name") String name,
                                                final @RequestParam(required = false, defaultValue = "true") boolean cs) {
        log.info("UserController#getUserByName() called.");
        log.info("Case-sensitive mode: {}, passed parameter 'name': '{}'", cs, name);

        if (cs) {
            final User user = getUserService().fetchUserByName(name);

            if (user != null) {
                return new ResponseEntity<>(user, HttpStatus.OK);
            } else {
                log.info(USER_DOES_NOT_EXIST);

                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }

        final List<User> users = getUserService().fetchUserByNameCaseInsensitive(name);
        if (users != null && !users.isEmpty()) {
            return new ResponseEntity<>(users, HttpStatus.OK);
        } else {
            log.info("User collection is empty.");

            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/")
    public ResponseEntity<User> createUser(final @Valid @RequestBody UserDTO userDTO) {
        log.info("UserController#createUser() called.");

        final User userFromRequest = UserConverter.convertToUser(userDTO);

        final User fetchedUser = getUserService().fetchUserByName(userFromRequest.getName());
        if (fetchedUser != null) {
            log.info("User exists.");

            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        final User savedUser = getUserService().saveUser(userFromRequest);

        if (savedUser != null) {
            log.info("User created successfully.");

            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } else {
            log.warn("User has not been created.");

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(path = "/{name}")
    public ResponseEntity<Object> deleteUserByName(final @PathVariable("name") String name) {
        log.info("UserController#deleteUserByName() called.");

        final User fetchedUser = getUserService().fetchUserByName(name);
        if (fetchedUser == null) {
            log.info(USER_DOES_NOT_EXIST);

            return new ResponseEntity<>(USER_DOES_NOT_EXIST, HttpStatus.CONFLICT);
        }

        final Long deleted = getUserService().deleteUserByName(name);

        if (deleted >= 1) {
            log.info("Deleted documents: {}", deleted);

            return new ResponseEntity<>(deleted + " documents were deleted.", HttpStatus.OK);
        } else {
            log.warn("Something happened during user deletion");

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}