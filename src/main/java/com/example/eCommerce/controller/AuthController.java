package com.example.eCommerce.controller;

import com.example.eCommerce.entity.User;
import com.example.eCommerce.repository.UserRepository;
import com.example.eCommerce.enums.ERole;
import com.example.eCommerce.entity.Role;
import com.example.eCommerce.repository.RoleRepository;
import com.example.eCommerce.securitySpring.payload.request.LoginRequest;
import com.example.eCommerce.securitySpring.payload.request.SignupRequest;
import com.example.eCommerce.securitySpring.payload.response.JwtResponse;
import com.example.eCommerce.securitySpring.payload.response.MessageResponse;
import com.example.eCommerce.securitySpring.security.jwt.JwtUtils;
import com.example.eCommerce.securitySpring.security.services.UserDetailsImpl;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    try {
      // 1. Autenticazione
      Authentication authentication = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                      loginRequest.getEmail(),
                      loginRequest.getPassword()
              )
      );

      SecurityContextHolder.getContext().setAuthentication(authentication);

      // 2. Generazione token JWT
      String jwt = jwtUtils.generateJwtToken(authentication);

      // 3. Recupero dettagli utente
      UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

      // 4. Recupero dati aggiuntivi dall'entità User
      User user = userRepository.findByEmail(userDetails.getEmail())
              .orElseThrow(() -> new RuntimeException("User not found"));

      // 5. Costruzione risposta
      return ResponseEntity.ok(new JwtResponse(
              jwt,
              user.getId(),          // UUID dall'entità User
              user.getEmail(),       // Email
              user.getName(),        // Nome reale
              user.getSurname(),    // Cognome reale
              userDetails.getAuthorities().stream()
                      .map(GrantedAuthority::getAuthority)
                      .collect(Collectors.toList())
      ));

    } catch (Exception e) {
      return ResponseEntity
              .status(HttpStatus.UNAUTHORIZED)
              .body(new MessageResponse("Error: Invalid credentials"));
    }
  }

  @Transactional
  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    // 1. Verifica case-insensitive e più robusta
    if (userRepository.existsByEmailIgnoreCase(signUpRequest.getEmail().trim())) {
      return ResponseEntity
              .status(HttpStatus.CONFLICT)
              .body(new MessageResponse("Error: Email is already registered"));
    }

    // 2. Normalizza l'email
    String normalizedEmail = signUpRequest.getEmail().toLowerCase().trim();

    // 3. Crea utente
    User user = new User();
    user.setEmail(normalizedEmail);
    user.setPassword(encoder.encode(signUpRequest.getPassword()));
    user.setName(signUpRequest.getName().trim());
    user.setSurname(signUpRequest.getSurname() != null ? signUpRequest.getSurname().trim() : null);
    user.setAddress(signUpRequest.getAddress());
    user.setPhone(signUpRequest.getPhone());

    // 4. Assegnazione ruoli (versione migliorata)
    user.setRoles(resolveRoles(signUpRequest.getRole()));

    // 5. Salvataggio con controllo di integrità
    try {
      userRepository.saveAndFlush(user);
      return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    } catch (DataIntegrityViolationException e) {
      return ResponseEntity
              .status(HttpStatus.CONFLICT)
              .body(new MessageResponse("Error: User already exists"));
    }
  }

  private Set<Role> resolveRoles(Set<String> strRoles) {
    Set<Role> roles = new HashSet<>();

    if (strRoles == null || strRoles.isEmpty()) {
      roles.add(getRole(ERole.ROLE_USER));
      return roles;
    }

    strRoles.forEach(role -> {
      switch (role.toLowerCase()) {
        case "admin":
          roles.add(getRole(ERole.ROLE_ADMIN));
          break;
        case "mod":
          roles.add(getRole(ERole.ROLE_MODERATOR));
          break;
        default:
          roles.add(getRole(ERole.ROLE_USER));
      }
    });

    return roles;
  }

  private Role getRole(ERole roleName) {
    return roleRepository.findByName(roleName)
            .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
  }
}