package org.total.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.total.dto.UserDTO;
import org.total.model.User;
import org.total.response.Response;
import org.total.service.UserService;
import org.total.util.UserConverter;

import javax.validation.Valid;
import java.util.List;

import static org.total.util.Constants.USER_WITH_NAME;

/**
 * @author Pavlo.Fandych
 */

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    private UserService getUserService() {
        return userService;
    }

    @GetMapping(path = "/")
    public ResponseEntity<Object> getAllUsers(@RequestParam(defaultValue = "0") Integer pageNumber,
                                              @RequestParam(defaultValue = "10") Integer pageSize,
                                              @RequestParam(defaultValue = "name") String sortBy) {
        final List<User> users = getUserService().fetchAllUsers(pageNumber, pageSize, sortBy);

        return (users != null && !users.isEmpty()) ?
                new ResponseEntity<>(users, HttpStatus.OK) :
                new ResponseEntity<>(new Response("Collection is empty."), HttpStatus.NOT_FOUND);
    }

    @GetMapping(path = "/{name}")
    public ResponseEntity<Object> getUserByName(final @PathVariable("name") String name,
                                                final @RequestParam(required = false, defaultValue = "true") boolean cs) {
        log.info("Case-sensitive mode: {}, passed parameter 'name': '{}'", cs, name);

        if (cs) {
            final User user = getUserService().fetchUserByName(name);

            return (user != null) ?
                    new ResponseEntity<>(user, HttpStatus.OK) :
                    new ResponseEntity<>(new Response(USER_WITH_NAME + name + "' not found. Probably," +
                            " parameter needs to be passed in case-insensitive mode."), HttpStatus.NOT_FOUND);
        }

        final List<User> users = getUserService().fetchUserByNameCaseInsensitive(name);
        return (users != null && !users.isEmpty()) ?
                new ResponseEntity<>(users, HttpStatus.OK) :
                new ResponseEntity<>(new Response(USER_WITH_NAME + name + "' not found"), HttpStatus.NOT_FOUND);
    }

    @PostMapping(path = "/")
    public ResponseEntity<Object> createUser(final @Valid @RequestBody UserDTO userDTO) {
        final User user = UserConverter.convertToUser(userDTO);
        final User savedUser = getUserService().saveUser(user);

        return (savedUser != null) ?
                new ResponseEntity<>(new Response(USER_WITH_NAME + user.getName() + "' has been saved"), HttpStatus.OK) :
                new ResponseEntity<>(new Response("Something happened."), HttpStatus.BAD_REQUEST);
    }
}