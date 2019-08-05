package org.total.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.total.model.User;
import org.total.response.Response;
import org.total.service.UserService;

import java.util.List;

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
    public ResponseEntity<Object> getAllUsers() {
        final List<User> users = getUserService().fetchAllUsers();

        return (users != null && !users.isEmpty()) ?
                new ResponseEntity<>(users, HttpStatus.OK) :
                new ResponseEntity<>(new Response("Collection is empty."), HttpStatus.NOT_FOUND);
    }

    @GetMapping(path = "/{name}")
    public ResponseEntity<Object> getUserByName(final @PathVariable("name") String name, final @RequestParam boolean cs) {
        log.info("Case-sensitive mode: {}, passed parameter 'name': '{}'", cs, name);

        if (cs) {
            final User user = getUserService().fetchUserByName(name);

            return (user != null) ?
                    new ResponseEntity<>(user, HttpStatus.OK) :
                    new ResponseEntity<>(new Response("User with name '" + name + "' not found"), HttpStatus.NOT_FOUND);
        }

        final List<User> users = getUserService().fetchUserByNameCaseInsensitive(name.toLowerCase());
        return (users != null && !users.isEmpty()) ?
                new ResponseEntity<>(users, HttpStatus.OK) :
                new ResponseEntity<>(new Response("User with name '" + name + "' not found"), HttpStatus.NOT_FOUND);
    }
}