package com.example.eCommerce.controller;

import com.example.eCommerce.dto.UserDto;
import com.example.eCommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService service;

    //CRUD
    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto EntityToCreate){
        if(EntityToCreate.getId()!=null){
            EntityToCreate.setId(null);
        }
      return ResponseEntity.status(HttpStatus.CREATED).body(service.createUser(EntityToCreate));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> updateUser(@PathVariable UUID id ,@RequestBody UserDto userToUpdate){
        return ResponseEntity.ok(service.updateUser(id,userToUpdate));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable  UUID id){
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> findUserById(@PathVariable UUID id){
        return ResponseEntity.ok(service.getUserById(id));
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserDto> findAllUsers(Pageable pageable){
        return service.getAllUsers(pageable);
    }






}
