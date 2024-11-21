package com.bolsadeideas.springboot.app.models.service;

import com.bolsadeideas.springboot.app.models.dao.UserRepository;
import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean authenticate(String username, String password) {
        User user = userRepository.findByUsername(username);
        return user != null && user.getPassword().equals(password);
    }

    public void registerUser(User user) {
        // Codificamos la contraseña antes de guardar el usuario
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        System.out.println("Contraseña codificada: " + encodedPassword);
        // Asignamos la contraseña codificada al usuario en Bycrypt
        user.setPassword(encodedPassword);
        user.setActive(true); // Por defecto, el usuario está activo
        // Guardamos el usuario en la base de datos
        userRepository.save(user);

    }

    //para actualizar la contraseña
    public void updateUser(User user) {
        // Verificamos si la contraseña se ha cambiado, y la codificamos
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    // Método para verificar si el usuario ya existe
    public boolean userExists(String username) {
        return userRepository.findByUsername(username) != null;
    }


    public void deleteUserById(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id); // Elimina el usuario por su ID
        } else {
            throw new RuntimeException("Usuario no encontrado");
        }
    }

    public void deactivateUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        user.setActive(false); // Cambia el estado a inactivo
        userRepository.save(user); // Guarda los cambios
    }

    public boolean isUserInactive(String username) {
        User user = userRepository.findByUsername(username);
        return user != null && !user.isActive();
    }

    public boolean findByUsernameisActive(String username) {
        User user = userRepository.findByUsernameisActive(username);
        return user != null && !user.isActive();
    }

    //comprobar si esta enable authenticator
    public boolean isTwoFactorEnabled(String username) {
        User user = userRepository.findByUsername(username);
        return user != null && user.isTwoFactorEnabled();
    }

    //obtner el email del usuario autenticado
    public String getUserEmail(String username) {
        User user = userRepository.findByUsername(username);
        return user != null ? user.getEmail() : null;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void save(User user) {
        userRepository.save(user);
    }
}
