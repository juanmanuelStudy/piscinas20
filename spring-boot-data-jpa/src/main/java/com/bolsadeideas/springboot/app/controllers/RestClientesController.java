package com.bolsadeideas.springboot.app.controllers;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.springboot.app.models.entity.Cliente;



@RestController
public class RestClientesController {
	
	
	
	@RequestMapping(value = "/listare", method = RequestMethod.GET)
	public Cliente listar() {
		
		Cliente cliente = new Cliente();		
		cliente.setNombre("juan");
		cliente.setEmail("sdfsd@fdsdf.com");
	
		return cliente;
	}

}
