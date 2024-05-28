package com.prueba.microservicios.app.usuario.repository;

import com.prueba.microservicios.app.usuario.models.entity.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role,Long> {

    Role findByName(String name);
}
