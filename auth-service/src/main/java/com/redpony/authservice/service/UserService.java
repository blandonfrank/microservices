package com.redpony.authservice.service;

import com.redpony.authservice.exceptions.UserAlreadyExistsException;
import com.redpony.authservice.model.User;
import com.redpony.authservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

    @Autowired
    private UserRepository repository;

    public void create(User user) {
        if(repository.findById(user.getUsername()).isPresent())
            throw new UserAlreadyExistsException("User already exists! " + user.getUsername());

        String pass = encoder.encode(user.getPassword());
        user.setPassword(pass);
        repository.save(user);

        log.info("new user has been created: {}", user.getUsername());
    }
}
