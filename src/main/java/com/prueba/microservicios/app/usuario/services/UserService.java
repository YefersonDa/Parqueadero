package com.prueba.microservicios.app.usuario.services;

import com.prueba.microservicios.app.usuario.models.dto.UserDto;
import com.prueba.microservicios.app.usuario.models.dto.UserDtoResponse;
import com.prueba.microservicios.app.usuario.models.entity.Role;
import com.prueba.microservicios.app.usuario.models.entity.User;

import java.util.List;

public interface UserService {

    List<UserDtoResponse> findAllUser();
    UserDtoResponse saveUser(UserDto userDto);
    UserDtoResponse findByIdUser(Long id);
    User findByUsername(String username);
    void deleteByIdUser(Long id);
    UserDtoResponse updateUser(Long id, UserDto userDto);

    Role saveRole(Role role);
    void addRoleToUser(String username,String roleName);
}
