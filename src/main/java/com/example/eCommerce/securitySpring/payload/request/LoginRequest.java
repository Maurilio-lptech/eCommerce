package com.example.eCommerce.securitySpring.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

public class LoginRequest {
	@NotBlank
	@Email
	private String email;

	@NotBlank
	private String password;

	// Getters e Setters
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}