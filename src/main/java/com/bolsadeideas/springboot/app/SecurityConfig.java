package com.bolsadeideas.springboot.app;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/css/**", "/dist/**", "/plugins/**", "/js/**", "/images/**").permitAll() // Permitir acceso a recursos estáticos sin autenticación
                .antMatchers("/login", "/logout").permitAll() // Permitir acceso a la página de login
                .anyRequest().authenticated() // Cualquier otra solicitud requiere autenticación
                .and()
                .formLogin()
                .loginPage("/login") // Página personalizada de login
                .permitAll() // Permitir acceso a la página de login
                .and()
                .logout()
                .permitAll(); // Permitir acceso al logout
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        auth
                .inMemoryAuthentication()
                .withUser("admin").password(encoder.encode("admin")).roles("ADMIN")
                .and()
                .withUser("user").password(encoder.encode("user")).roles("USER");

    }

}

