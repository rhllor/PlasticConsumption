package com.github.rhllor.pc.library.repository;

import java.util.Optional;
import com.github.rhllor.pc.library.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    

    Optional<User> findByToken(String token);

    Optional<User> findByUsername(String username);

}
