package com.bolsadeideas.springboot.app.controllers;

import com.bolsadeideas.springboot.app.models.entity.User;
import com.bolsadeideas.springboot.app.models.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

@Controller
public class LoginController {

    Log log = LogFactory.getLog(LoginController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/logout")
    public String logout() {

        return "redirect:/login?logout";
        // Redirigir al login con el parámetro de logout
    }


    //todo falta matizar lo del login que slaga cuando esta de baja un msj
    @GetMapping("/login")
    public String showLoginForm(
            @RequestParam(value = "error", required = false) String error,
            Model model) {

        // Obtén el usuario autenticado desde el contexto de seguridad
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            // Verificar si el usuario autenticado es una instancia de UserDetails
            if (principal instanceof UserDetails) {
                String username = ((UserDetails) principal).getUsername();
                log.info("Usuario autenticado: " + username);

                // Verificar si el usuario está activo
                if (!userService.findByUsernameisActive(username)) {
                    model.addAttribute("errorMessage", "Este usuario está de baja. No puede iniciar sesión.");
                    return "login";
                }
            }
        }

        // Mensaje de error en caso de credenciales incorrectas
        if (error != null) {
            model.addAttribute("errorMessage", "Usuario o contraseña incorrectos. Por favor, inténtelo de nuevo.");
        }

        return "login";
    }
}

