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

import com.bolsadeideas.springboot.app.models.entity.Proveedor;
import com.bolsadeideas.springboot.app.models.service.ClienteServiceImpl;
import com.bolsadeideas.springboot.app.models.service.ProductoServiceImpl;
import com.bolsadeideas.springboot.app.models.service.ProveedorServiceImpl;
import com.bolsadeideas.springboot.app.util.paginator.PageRender;


@Controller
@SessionAttributes("proveedor")
@RequestMapping("/proveedor")
public class ProveedoresController {

	@Autowired
	private ClienteServiceImpl clienteService;
	
	@Autowired
	private ProveedorServiceImpl proveedorService;
	
	@Autowired
	private ProductoServiceImpl materialService;
	
	
	 private static final org.jboss.logging.Logger log = LoggerFactory.logger(Proveedor.class);
	
	@RequestMapping(value = "/listarProveedor", method = RequestMethod.GET)
	public String listar(Model model,@RequestParam(name="page", defaultValue="0")int page) {
				
		PageRequest pageRequest =  PageRequest.of(page,5);
		Page <Proveedor> proveedor = proveedorService.findAll(pageRequest);
		PageRender<Proveedor> pageRender = new PageRender<>("/listarProveedor",proveedor);
		model.addAttribute("proveedor",proveedor);
		model.addAttribute("page",pageRender);
		model.addAttribute("titulo", "Listado de Proveedores");
		model.addAttribute("countProveedor", proveedorService.count());
		model.addAttribute("countMaterial", materialService.count());
		model.addAttribute("countCliente", clienteService.count());
		
		return "proveedor/listarProveedor";
	}
	
	//Busqueda Listar	
		@PostMapping("/buscar")
		public String buscar(
		@RequestParam(name="nombre") String nombre,@RequestParam(name = "page", defaultValue = "0") int page, Model model) 
		
	{			
			Pageable pageRequest = PageRequest.of(page, 10);
			 Page<Proveedor> proveedor =  proveedorService.findByNombreListar(nombre,pageRequest);
			 log.info(nombre.toString());
	         PageRender<Proveedor> pageRender = new PageRender<Proveedor>("/listarProveedor",proveedor);
				model.addAttribute("titulo", "Lista de Proveedor");
				model.addAttribute("proveedor", proveedor);
				model.addAttribute("page", pageRender);								
				return "proveedor/listarProveedor";
	}
		
	

	@RequestMapping(value = "/layout", method = RequestMethod.GET)
	public String count(Model model) {
		
		model.addAttribute("countProveedor", proveedorService.count());
		
		return "layoutMcompras";
	}
	
	@RequestMapping(value = "/form")
	public String crear( Map<String, Object> model) {
		
		//se añade todos los findAll que tenga los select en el formulario para poder selecionar
			Proveedor proveedor = new Proveedor();	
		
			
			model.put("proveedor", proveedor);
			model.put("titulo", "Formulario de Proveedor");
		
		return "proveedor/proveedorForm";
	}
	
	@RequestMapping(value="/form/{id}")
	public String editar(@PathVariable(value="id") Long id, Map<String, Object> model) {
		
		Proveedor proveedor = null;
		
		if(id > 0) {
			proveedor = proveedorService.findOne(id);
		} else {
			return "redirect:/listarProveedor";
		}
		model.put("proveedor", proveedor);
		model.put("titulo", "Editar Proveedor");
		return "proveedor/proveedorForm";
	}
	
	 
	
	
	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String guardar(@Valid Proveedor proveedor, BindingResult result, Model model, SessionStatus status,RedirectAttributes flash) {
		
		if(result.hasErrors()) {
			
			model.addAttribute("titulo", "Formulario de Proveedor");
			return "/proveedor/proveedorForm";
		}
		
		proveedorService.save(proveedor);
		flash.addFlashAttribute("success", "Creado el Proveedor con exito");
		status.setComplete();
		return "redirect:/proveedor/form";
	}
	
	@RequestMapping(value="/eliminar/{id}")
	public String eliminar(@PathVariable(value="id") Long id,RedirectAttributes flash) {
		
		if(id > 0) {
			proveedorService.delete(id);
		}
		
		flash.addFlashAttribute("success", "Eliminado el proveedor con exito");

		return "redirect:/proveedor/listarProveedor";
	}
}
