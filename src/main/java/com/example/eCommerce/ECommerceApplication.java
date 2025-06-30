package com.example.eCommerce;

import com.example.eCommerce.repository.RoleRepository;
import com.example.eCommerce.enums.ERole;
import com.example.eCommerce.entity.Role;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class ECommerceApplication {

	@Autowired
	private RoleRepository roleRepository;


	public static void main(String[] args) {
		SpringApplication.run(ECommerceApplication.class, args);
	}

	@PostConstruct
	public void initRoles() {
		Arrays.stream(ERole.values()).forEach(role -> {
			if (!roleRepository.existsByName(role)) {
				roleRepository.save(new Role(role));
			}
		});
	}

}
