package com.github.rhllor.pc.library.service;

import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

import com.github.rhllor.pc.library.entity.User;
import com.github.rhllor.pc.library.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UserService {
    
    @Autowired
    private UserRepository _repository;

    public User save(User user) {
        return _repository.save(user);
    }

    public Optional<User> getByToken(String token) {
        return _repository.findByToken(token);
    }

    public Optional<User> getByUsername(String username) {
        return _repository.findByUsername(username);
    }

    public Optional<User> findById(Long id) {
        return _repository.findById(id);
    }

    public List<User> findAll() {
        return _repository.findAll();
    }
}
