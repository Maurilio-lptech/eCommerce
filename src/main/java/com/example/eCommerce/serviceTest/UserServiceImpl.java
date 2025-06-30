package com.example.eCommerce.serviceTest;

import com.example.eCommerce.dto.UserDto;
import com.example.eCommerce.entity.User;
import com.example.eCommerce.mapper.UserMapper;
import com.example.eCommerce.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    //CRUD

    public UserDto getUserById(UUID id) {
        return mapper.toDto(repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nessun utente trovato con id" + id)));
    }

    @Transactional
    public UserDto createUser(UserDto userDto) {
        if (userDto.getId() != null) {
            throw new IllegalArgumentException("Passa un id nullo durante la creazione");
        }
        return mapper.toDto(repository.save(mapper.toEntity(userDto)));
    }

    @Transactional
    public void deleteUser(UUID id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Id non trovato nel DB");
        }

        repository.deleteById(id);
    }

    @Transactional
    public UserDto updateUser(UUID id, UserDto userDto) {

        User existingUser = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("user con id" + userDto.getId() + " non trovato nel db"));

        existingUser.setEmail(userDto.getEmail());
        existingUser.setName(userDto.getName());
        existingUser.setSurname(userDto.getSurname());
        existingUser.setAddress(userDto.getAddress());
        existingUser.setPhone(userDto.getPhone());



        return mapper.toDto(repository.save(existingUser));
    }

    @Transactional(readOnly = true)
    public Page<UserDto> getAllUsers(Pageable pageable) {
        Page<User> userPage = repository.findAll(pageable);
        return userPage.map(mapper::toDto);
    }


}
