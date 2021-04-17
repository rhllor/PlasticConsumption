package com.github.rhllor.pc.service.auth;

import org.springframework.stereotype.Service;

import java.util.Optional;

import com.github.rhllor.pc.library.UserRepository;
import com.github.rhllor.pc.library.entity.User;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    public User save(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getByToken(String token) {
        return userRepository.findByToken(token);
    }

    public Optional<User> getByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
