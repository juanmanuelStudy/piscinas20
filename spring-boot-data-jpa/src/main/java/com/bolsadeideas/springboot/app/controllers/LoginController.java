package com.bolsadeideas.springboot.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginForm() {
        //como compribar si el usuario esta logeado y e correcto



        return "login"; // Devolver el nombre de la plantilla HTML de login
    }

  //metodo para obtener el usuario y contraseña y comparar
    @GetMapping("/login/{usuario}/{contraseña}")
    public String login(@PathVariable String usuario, @PathVariable String contraseña){

        if (usuario.equals("admin") && contraseña.equals("admin")){
            System.out.println("Usuario y contraseña correctos");

            return  "redirect:/listar";
        }else{
            System.out.println("Usuario y contraseña incorrectos");
        }
        //comparar usuario y contraseña con la base de datos
        return "login";
    }
}


