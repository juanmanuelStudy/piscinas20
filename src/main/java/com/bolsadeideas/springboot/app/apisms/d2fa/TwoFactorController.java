package com.bolsadeideas.springboot.app.apisms.d2fa;

import com.bolsadeideas.springboot.app.models.entity.User;
import com.bolsadeideas.springboot.app.models.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping
public class TwoFactorController {

    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    @GetMapping("/verify_2fa")
    public String showVerificationPage() {
        return "verify_2fa"; // Nombre de tu plantilla Thymeleaf
    }

    @PostMapping("/verify_2fa")
    public String verify2fa(
            @RequestParam("code") String code,
            @RequestParam("username") String username,
            HttpServletRequest request,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes) {

        User user = userService.findByUsername(username);

        if (user != null && user.getSecret().equals(code)) {
            log.info("Verificación exitosa para el usuario: " + username);

            // Marcar en la sesión que el 2FA fue completado
            HttpSession session = request.getSession();
            session.setAttribute("2FA_VERIFIED", true);

            // Establecer cookie opcional
            Cookie cookie = new Cookie("2FA_STATUS", "VERIFIED");
            cookie.setMaxAge(3600); // 1 hora
            cookie.setPath("/");
            response.addCookie(cookie);

            // Redirigir al usuario a la página de destino
            return "redirect:/listar";
        } else {
            log.warn("Verificación fallida para el usuario: " + username);

            // Agregar el mensaje de error a RedirectAttributes
            redirectAttributes.addFlashAttribute("error", "El código es incorrecto");

            // Redirigir a la página de verificación
            return "redirect:/verify_2fa";
        }
    }


}
