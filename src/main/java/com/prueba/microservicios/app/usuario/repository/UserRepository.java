package com.prueba.microservicios.app.usuario.repository;

import com.prueba.microservicios.app.usuario.models.entity.User;
import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<User,Long> {

    User findByUsername(String username);
    User findByEmail(String email);
}
