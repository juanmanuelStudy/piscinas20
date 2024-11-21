package com.bolsadeideas.springboot.app.apisms.d2fa;

import javax.mail.MessagingException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;

import com.bolsadeideas.springboot.app.models.entity.User;
import com.bolsadeideas.springboot.app.models.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class TwoFactorAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = Logger.getLogger(TwoFactorAuthenticationFilter.class.getName());

    @Autowired
    private TwoFactorMessageService twoFactorMessageService;

    @Autowired
    private UserService userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Verificar si el usuario está autenticado
        if (auth != null && auth.isAuthenticated()) {
            HttpSession session = request.getSession();

            // Excluir la página de verificación de 2FA para evitar ciclos
            String requestURI = request.getRequestURI();
            if (requestURI.contains("/verify_2fa")) {
                filterChain.doFilter(request, response);
                return;
            }

            // Comprobar si el 2FA ya fue verificado
            Boolean isTwoFactorVerified = (Boolean) session.getAttribute("2FA_VERIFIED");
            if (Boolean.TRUE.equals(isTwoFactorVerified)) {
                // Continuar con la cadena de filtros si 2FA ya está verificado
                filterChain.doFilter(request, response);
                return;
            }

            // Si el código no fue enviado, envíalo
            Boolean isCodeSent = (Boolean) session.getAttribute("2FA_CODE_SENT");
            if (isCodeSent == null || !isCodeSent) {
                String username = auth.getName();
                String email = userRepository.getUserEmail(username);

                if (email != null) {
                    String generatedCode = twoFactorMessageService.generateVerificationCode();

                    User user = userRepository.findByUsername(username);
                    if (user != null) {
                        user.setSecret(generatedCode);
                        userRepository.save(user);
                    }

                    try {
                        twoFactorMessageService.sendVerificationCode(email, generatedCode);
                    } catch (MessagingException e) {
                        log.severe("Error al enviar el código de verificación: " + e.getMessage());
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al enviar el código de verificación.");
                        return;
                    }

                    // Marcar en la sesión que el código fue enviado
                    session.setAttribute("2FA_CODE_SENT", true);
                }
            }

            // Redirigir a la página de verificación de 2FA
            response.sendRedirect("/verify_2fa");
            return;
        }

        // Continuar con la cadena de filtros si el usuario no está autenticado
        filterChain.doFilter(request, response);
    }


    private boolean isTwoFactorVerified(HttpServletRequest request) {
        // Revisamos la cookie para ver si el 2FA está verificado
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("2FA_STATUS".equals(cookie.getName()) && "VERIFIED".equals(cookie.getValue())) {
                    return true; // El 2FA está verificado
                }
            }
        }
        return false; // El 2FA no está verificado
    }


}

