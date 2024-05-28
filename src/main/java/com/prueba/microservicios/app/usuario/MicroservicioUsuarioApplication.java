package com.prueba.microservicios.app.usuario;

import com.prueba.microservicios.app.usuario.models.dto.UserDto;
import com.prueba.microservicios.app.usuario.models.entity.Role;
import com.prueba.microservicios.app.usuario.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class MicroservicioUsuarioApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroservicioUsuarioApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner run(UserService userService){
		return args -> {
			userService.saveRole(new Role(null,"ROLE_USER"));
			userService.saveRole(new Role(null,"ROLE_ADMIN"));

			userService.saveUser(new UserDto(null,"admin","admin@mail.com","admin"));
			userService.saveUser(new UserDto(null,"yeferson","yeferson@mail.com","admin"));
			userService.addRoleToUser("admin","ROLE_ADMIN");
			userService.addRoleToUser("admin","ROLE_USER");
			userService.addRoleToUser("yeferson","ROLE_USER");
		};
	}

}
