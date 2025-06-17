package com.example.eCommerce.mapper;

import com.example.eCommerce.dto.UserDto;
import com.example.eCommerce.entity.User;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;


@Mapper(componentModel = "spring")
public interface UserMapper {

    public UserDto toDto(User user);
    public User toEntity(UserDto userDto);



}
