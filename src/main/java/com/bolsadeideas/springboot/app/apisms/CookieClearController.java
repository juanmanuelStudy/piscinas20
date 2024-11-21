package com.bolsadeideas.springboot.app.apisms;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CookieClearController {

    @GetMapping("/clear-cookies")
    public String clearCookies(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookie.setMaxAge(0);  // Eliminar la cookie
                cookie.setPath("/");   // Asegurarse de que sea válida para todo el dominio
                response.addCookie(cookie);  // Añadir la cookie modificada al response
            }
        }
        return "redirect:/";  // Redirigir a la página de inicio o a donde lo necesites
    }
}
