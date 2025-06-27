package com.example.eCommerce.securitySpring.payload.response;

import lombok.Getter;
import java.util.List;
import java.util.UUID;

@Getter
public class JwtResponse {
  private String token;
  private String type = "Bearer";
  private UUID id;
  private String email;
  private String name;
  private String surname;
  private List<String> roles;

  public JwtResponse(String accessToken, UUID id, String email, String name, String surname, List<String> roles) {
    this.token = accessToken;
    this.id = id;
    this.email = email;
    this.name = name;
    this.surname = surname;
    this.roles = roles;
  }
}