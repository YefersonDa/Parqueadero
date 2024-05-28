package com.prueba.microservicios.app.usuario.models.mapper;

import com.prueba.microservicios.app.usuario.models.dto.UserDto;
import com.prueba.microservicios.app.usuario.models.dto.UserDtoResponse;
import com.prueba.microservicios.app.usuario.models.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toUser(UserDto userDto){
        return User.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .build();
    }

    public UserDtoResponse toUserDtoResponse(User user){
         return UserDtoResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }

}
