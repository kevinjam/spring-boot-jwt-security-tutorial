package com.codecraftwithkevin.security_jwt.repository;

import com.codecraftwithkevin.security_jwt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
