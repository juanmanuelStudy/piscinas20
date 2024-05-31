package com.bolsadeideas.springboot.app.controllers;

import java.util.Map;

import javax.validation.Valid;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.springboot.app.models.entity.MetodoPago;
import com.bolsadeideas.springboot.app.models.entity.Producto;
import com.bolsadeideas.springboot.app.models.service.ClienteServiceImpl;
import com.bolsadeideas.springboot.app.models.service.ProductoServiceImpl;
import com.bolsadeideas.springboot.app.models.service.ProveedorServiceImpl;
import com.bolsadeideas.springboot.app.models.service.ServiceMetodo;
import com.bolsadeideas.springboot.app.util.paginator.PageRender;


@Controller
@SessionAttributes("metodo")
@RequestMapping("/metodo")
public class MetodoController {
	
	
	@Autowired
	private ServiceMetodo metodoService;
	
	
	 private static final org.jboss.logging.Logger log = LoggerFactory.logger(MetodoPago.class);
	
	@RequestMapping(value = "/listarMetodo", method = RequestMethod.GET)
	public String listar(Model model,@RequestParam(name="page", defaultValue="0")int page) {
				
		PageRequest pageRequest =  PageRequest.of(page,5);
		Page <MetodoPago> metodo = metodoService.findAll(pageRequest);
		PageRender<MetodoPago> pageRender = new PageRender<>("listarMetodo",metodo);
		model.addAttribute("metodo",metodo);
		model.addAttribute("page",pageRender);
		model.addAttribute("titulo", "Listado de Metodo de Pagos de las Facturas");
		
		return "/metodo/listarMetodo";
	}
		

	
	
	@RequestMapping(value = "/form")
	public String crear( Map<String, Object> model) {
		
		//se añade todos los findAll que tenga los select en el formulario para poder selecionar
		MetodoPago metodo = new MetodoPago();	
		
			
			model.put("metodo", metodo);
			
			model.put("titulo", "Formulario de Metodo de Pado de Facturas");
		
		return "/metodo/metodoForm";
	}
	
	@RequestMapping(value="/form/{id}")
	public String editar(@PathVariable(value="id") Long id, Map<String, Object> model) {
		
		MetodoPago metodo = null;
		
		if(id > 0) {
			metodo = metodoService.findOne(id);
		} else {
			return "redirect:/listarMetodo";
		}
		model.put("metodo", metodo);
		model.put("titulo", "Editar MEtodo de Pago");
		return "/metodo/metodoForm";
	}
	

	
	
	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String guardar(@Valid MetodoPago  metodo, BindingResult result, Model model, SessionStatus status,RedirectAttributes flash) {
		
		if(result.hasErrors()) {
			
			model.addAttribute("titulo", "Formulario de Metodo Pago");
			return "/metodo/metodoForm";
		}
		
		metodoService.save(metodo);
		flash.addFlashAttribute("success", "Creado el metodo de pago con exito");
		status.setComplete();
		return "redirect:listarMetodo";
	}
	
	@RequestMapping(value="/eliminar/{id}")
	public String eliminar(@PathVariable(value="id") Long id,RedirectAttributes flash) {
		
		if(id > 0) {
			metodoService.delete(id);
		}
		flash.addFlashAttribute("success", "Eliminado el metodo de pago con exito");

		return "redirect:/metodo/listarMetodo";
	}
}
