package com.redpony.authservice.controller;

import com.redpony.authservice.exceptions.UserAlreadyExistsException;
import com.redpony.authservice.model.User;
import com.redpony.authservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/current")
    public Principal getUser(Principal principal) {
        return principal;
    }

    @PreAuthorize("#oauth2.hasScope('server')")
    @PostMapping
    public void createUser(@Valid @RequestBody User user) throws UserAlreadyExistsException{
        userService.create(user);
    }

    @ExceptionHandler( {UserAlreadyExistsException.class} )
    public ResponseEntity<String> handleAlreadyExists() {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

}