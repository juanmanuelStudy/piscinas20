package com.bolsadeideas.springboot.app.models.service;

import com.bolsadeideas.springboot.app.models.dao.UserRepository;
import com.bolsadeideas.springboot.app.models.entity.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserRepository userRepository; // Usa tu repositorio o servicio para obtener el usuario
/*
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username); // Método que recupera el usuario por su nombre
        if (user == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }

        // Convertir tu clase User a UserDetails
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword()) // Aquí se espera que la contraseña ya esté codificada
                .roles(String.valueOf(user.getRoles())) // Asigna el rol correspondiente
                .build();
    }
*/

    /*
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username); // Buscar usuario por username
        if (user == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username); // Lanza excepción si no se encuentra el usuario
        }
        if (user.isActive()) { // Verifica si el usuario está dado de baja
            throw new UsernameNotFoundException("Usuario dado de baja: " + username); // Lanza excepción si el usuario está dado de baja
        }
        // Convertir tu clase User a UserDetails
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword()) // Aquí se espera que la contraseña ya esté codificada
                .roles(String.valueOf(user.getGrantedAuthorities())) // Asigna el rol correspondiente
                .build();
    }
*/

    /**
     * Método para cargar el usuario por su nombre de usuario
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User userExiste = userRepository.findByUsername(username); // Buscar usuario por username
        log.info("Usuario activo: " + userExiste + "username: " + username);

        // Verificar si el usuario está activo
        if (userExiste == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username); // Lanza excepción si no se encuentra el usuario
        }
        if (!userExiste.isActive()) { // Verifica si el usuario está dado de baja
            throw new UsernameNotFoundException("Usuario dado de baja: " + username); // Lanza excepción si el usuario está dado de baja
        }
        User user = userRepository.findByUsernameWithRoles(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), user.getGrantedAuthorities());
    }

}

