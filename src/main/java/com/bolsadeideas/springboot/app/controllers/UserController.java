package com.bolsadeideas.springboot.app.controllers;

import com.bolsadeideas.springboot.app.models.entity.Role;
import com.bolsadeideas.springboot.app.models.entity.User;
import com.bolsadeideas.springboot.app.models.dao.RoleRepository;
import com.bolsadeideas.springboot.app.models.dao.UserRepository;
import com.bolsadeideas.springboot.app.models.service.UserService;
import com.bolsadeideas.springboot.app.util.paginator.PageRender;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class UserController {

    Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;


    /**
     * @param model
     * @return
     */
    @GetMapping("/users/new")
    public String createUserForm(Model model) {
        User user = new User();
        Set<Role> rolesSet = new HashSet<>(roleRepository.findAll()); // Convertir a Set
        log.info("Roles: " + rolesSet);
        model.addAttribute("user", user);
        model.addAttribute("rolesList", rolesSet);
        return "user/create_user_with_role";
    }

    /**
     * Método para registrar un usuario con roles
     *
     * @param user
     * @param model
     * @param roles
     * @return
     */
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model, @RequestParam List<Long> roles) {
        // Verificar si el nombre de usuario ya existe
        if (userService.userExists(user.getUsername())) {
            log.info("El usuario ya existe.");
            model.addAttribute("errorMessage", "El usuario ya existe.");

            // Recargar la lista de roles para la vista
            List<Role> rolesList = roleRepository.findAll();
            model.addAttribute("rolesList", rolesList);

            return "user/create_user_with_role";
        }

        // Verificar si el email ya existe
        if (userRepository.existsByEmail(user.getEmail())) {
            log.info("El email ya existe.");
            model.addAttribute("errorMessage", "El email ya existe.");

            // Recargar la lista de roles para la vista
            List<Role> rolesList = roleRepository.findAll();
            model.addAttribute("rolesList", rolesList);

            return "user/create_user_with_role";
        }

        // Obtener los roles correspondientes y convertirlos a un Set

        Set<Role> userRoles = new HashSet<>(roleRepository.findAllById(roles));
        user.setRoles(userRoles); // Asigna el Set de roles al usuario

        // Registrar el usuario
        userService.registerUser(user); // Aquí puedes incluir la lógica de codificación y guardado del usuario

        return "redirect:/users";
    }

    @GetMapping("/users")
    public String listUsers(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {
        Pageable pageRequest = PageRequest.of(page, 6); // Cambia 4 por el tamaño de página deseado
        Page<User> users = userRepository.findActiveUsers(pageRequest); // Página de usuarios
        PageRender<User> pageRender = new PageRender<>("/users", users); // Usamos el paginador PageRender

        model.addAttribute("users", users.getContent()); // Lista de usuarios
        model.addAttribute("page", pageRender); // Paginación
        model.addAttribute("titulo", "Lista de Usuarios"); // Título de la vista

        return "user/list_users"; // Retornamos la vista
    }

    @PostMapping("/users/deactivate/{id}")
    public String deactivateUser(@PathVariable Long id) {
        userService.deactivateUser(id);
        return "user/list_users"; // Redirige a la lista de usuarios
    }

}

