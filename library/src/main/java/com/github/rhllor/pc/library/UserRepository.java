package com.github.rhllor.pc.library;

import com.github.rhllor.pc.library.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    
}
