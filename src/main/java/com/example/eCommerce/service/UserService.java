package com.example.eCommerce.service;

import com.example.eCommerce.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {

    public UserDto createUser(UserDto userDto);
    public void deleteUser(UUID id);
    public UserDto updateUser(UUID id, UserDto userDto);
    public Page<UserDto> getAllUsers( Pageable pageable);
    public UserDto getUserById(UUID id);



}
