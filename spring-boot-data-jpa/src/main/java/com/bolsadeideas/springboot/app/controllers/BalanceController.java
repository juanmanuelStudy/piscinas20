package com.bolsadeideas.springboot.app.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
@RequestMapping("/balance")
public class BalanceController {
	

	@RequestMapping(value = "/listarBalance", method = RequestMethod.GET)
	public String listar( Model model) {

		

		

		
		model.addAttribute("titulo", "Balance de Pagos y Beneficios");
		
		return "balance/ver";
	}

}
