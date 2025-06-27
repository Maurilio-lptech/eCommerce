package com.example.eCommerce.repository;

import com.example.eCommerce.dto.UserDto;
import com.example.eCommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    boolean existsByEmailIgnoreCase(String trim);
}
