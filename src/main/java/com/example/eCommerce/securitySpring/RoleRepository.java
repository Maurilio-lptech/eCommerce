package com.example.eCommerce.securitySpring;

import java.util.Optional;
import java.util.UUID;

import com.example.eCommerce.securitySpring.models.ERole;
import com.example.eCommerce.securitySpring.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
  Optional<Role> findByName(ERole name);


  boolean existsByName(ERole role);
}
