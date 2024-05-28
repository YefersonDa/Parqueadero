package com.prueba.microservicios.app.usuario.services.impl;

import com.prueba.microservicios.app.usuario.exception.GenericException;
import com.prueba.microservicios.app.usuario.models.dto.UserDto;
import com.prueba.microservicios.app.usuario.models.dto.UserDtoResponse;
import com.prueba.microservicios.app.usuario.models.entity.Role;
import com.prueba.microservicios.app.usuario.models.entity.User;
import com.prueba.microservicios.app.usuario.models.mapper.UserMapper;
import com.prueba.microservicios.app.usuario.repository.RoleRepository;
import com.prueba.microservicios.app.usuario.repository.UserRepository;
import com.prueba.microservicios.app.usuario.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username);
        if(user==null){
            log.error("UserService: Usuario no encontrado en la base de datos");
            throw new GenericException(HttpStatus.NOT_FOUND,"El usuario no se encontro");
        }


        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        log.info("UserService: Usuario encontrado en la base de datos",username);
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),authorities);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDtoResponse> findAllUser() {

        try {
            List<User> users= (List<User>) repository.findAll();
            log.info("UserService: Obtener a todo los usuarios");
            return users.stream()
                    .map(u->userMapper.toUserDtoResponse(u))
                    .collect(Collectors.toList());
        }catch (HttpServerErrorException e){
            log.error("UserService: Error Obteniendo a todo los usuarios");
            throw  new GenericException(HttpStatus.INTERNAL_SERVER_ERROR,"Ha ocurrido un error interno");
        }
    }

    @Override
    @Transactional
    public UserDtoResponse saveUser(UserDto userDto) {

        try {

            if(repository.findByEmail(userDto.getEmail())!=null){
                log.error("UserService: Error al guardar el usuario, el email ya existe");
                throw new GenericException(HttpStatus.BAD_REQUEST,"El email ya existe");

            }
            if(repository.findByUsername(userDto.getUsername())!=null){
                log.error("UserService: Error al guardar el usuario, el username ya existe");
                throw new GenericException(HttpStatus.BAD_REQUEST,"El username ya existe");
            }
            User user = repository.save(userMapper.toUser(userDto));
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            log.info("UserService: Guardando un usuario en la base de datos",userDto.getUsername());
            return userMapper.toUserDtoResponse(user);
        }catch (HttpServerErrorException e){
            log.error("UserService: Error interno al guardar el usuario");
            throw  new GenericException(HttpStatus.INTERNAL_SERVER_ERROR,"Ha ocurrido un error interno");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserDtoResponse findByIdUser(Long id) {
        try {
            User user = repository.findById(id).orElseThrow(()-> new GenericException(HttpStatus.NOT_FOUND,"No se encontro el usuario"));
            log.info("UserService: Obtener usuario por id: ",id);
            return userMapper.toUserDtoResponse(user);
        }catch (HttpServerErrorException e){
            log.error("UserService: Error interno al buscar el usuario por id",id);
            throw  new GenericException(HttpStatus.INTERNAL_SERVER_ERROR,"Ha ocurrido un error interno");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        try {
            log.info("UserService: buscar usuario por username ",username);
            return repository.findByUsername(username);
        }catch (HttpServerErrorException e){
            log.error("UserService: Error interno al buscar el usuario por username",username);
            throw  new GenericException(HttpStatus.INTERNAL_SERVER_ERROR,"Ha ocurrido un error interno");
        }
    }

    @Override
    @Transactional
    public void deleteByIdUser(Long id) {
        log.info("UserService: Borrar el usuario por id ",id);
        findByIdUser(id);
        try {
            repository.deleteById(id);
        }catch (HttpServerErrorException e){
            log.error("UserService: Error interno al borrar el usuario por id",id);
            throw  new GenericException(HttpStatus.INTERNAL_SERVER_ERROR,"Ha ocurrido un error interno");
        }
    }

    @Override
    @Transactional
    public UserDtoResponse updateUser(Long id, UserDto userDto) {
        log.info("UserService: Actualizado el usuario por id",id," Username ",userDto.getUsername());
        UserDtoResponse userDtoResponse= findByIdUser(id);
        try {
            if(userDto.getEmail()!=userDtoResponse.getEmail()){
                if(repository.findByEmail(userDto.getEmail())!=null){
                    log.error("UserService: Error  al actualizar el usuario por id ya existe el email",id);
                    throw  new GenericException(HttpStatus.BAD_REQUEST,"El email ya existe");
                }
            }
            if(userDto.getUsername()!=userDtoResponse.getUsername()){
                if(repository.findByUsername(userDto.getUsername())!=null){
                    log.error("UserService: Error  al actualizar el usuario por id ya existe el username",id);
                    throw  new GenericException(HttpStatus.BAD_REQUEST,"El username ya existe");
                }
            }
            return userMapper.toUserDtoResponse(
                    repository.save( User.builder()
                            .id(userDtoResponse.getId())
                            .username(userDto.getUsername())
                            .email(userDto.getEmail())
                            .password(userDto.getPassword())
                            .build()));
        }catch (HttpServerErrorException e){
            log.error("UserService: Error interno al actualizar el usuario por id",id);
            throw  new GenericException(HttpStatus.INTERNAL_SERVER_ERROR,"Ha ocurrido un error interno");
        }

    }

    @Override
    @Transactional
    public Role saveRole(Role role) {
        try {
            log.info("UserService: Guardando nuevo rol en la base de datos",role.getName());
            return roleRepository.save(role);
        }catch (HttpServerErrorException e){
            log.error("UserService: Error interno al guardar un rol",role.getName());
            throw  new GenericException(HttpStatus.INTERNAL_SERVER_ERROR,"Ha ocurrido un error interno");
        }
    }

    @Override
    @Transactional
    public void addRoleToUser(String username, String roleName) {
        try {
            User user = repository.findByUsername(username);
            Role role = roleRepository.findByName(roleName);
            System.out.println(username.toString());
            System.out.println(role.toString());
            log.info("UserService: Añadiendo role al usuario",roleName,username);
            user.getRoles().add(role);
        }catch (HttpServerErrorException e){
            log.error("UserService: Error interno al añadir un rol al usuario",username," rol",roleName);
            throw  new GenericException(HttpStatus.INTERNAL_SERVER_ERROR,"Ha ocurrido un error interno");
        }
    }


}
