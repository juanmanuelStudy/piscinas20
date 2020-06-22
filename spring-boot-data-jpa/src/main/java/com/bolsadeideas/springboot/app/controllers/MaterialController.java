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

import com.bolsadeideas.springboot.app.models.entity.Producto;
import com.bolsadeideas.springboot.app.models.entity.Proveedor;
import com.bolsadeideas.springboot.app.models.service.ClienteServiceImpl;
import com.bolsadeideas.springboot.app.models.service.ProductoServiceImpl;
import com.bolsadeideas.springboot.app.models.service.ProveedorServiceImpl;
import com.bolsadeideas.springboot.app.util.paginator.PageRender;


@Controller
@SessionAttributes("Materiales")
@RequestMapping("/materiales")
public class MaterialController {
	
	
	@Autowired
	private ProveedorServiceImpl proveedorService;
	@Autowired
	private ProductoServiceImpl materialService;	
	@Autowired
	private ClienteServiceImpl clienteService;
	
	 private static final org.jboss.logging.Logger log = LoggerFactory.logger(Producto.class);
	
	@RequestMapping(value = "/listarMaterial", method = RequestMethod.GET)
	public String listar(Model model,@RequestParam(name="page", defaultValue="0")int page) {
				
		PageRequest pageRequest =  PageRequest.of(page,5);
		Page <Producto> material = materialService.findAll(pageRequest);
		PageRender<Producto> pageRender = new PageRender<>("/listarMaterial",material);
		model.addAttribute("material",material);
		model.addAttribute("page",pageRender);
		model.addAttribute("titulo", "Listado de Material");
		model.addAttribute("countMaterial", materialService.count());
		model.addAttribute("countCliente", clienteService.count());
		model.addAttribute("countProveedor", proveedorService.count());
		return "/productos/listarMaterial";
	}
		

	@RequestMapping(value = "/layout", method = RequestMethod.GET)
	public String count(Model model) {
		
		model.addAttribute("countMaterial", materialService.count());
		
		return "layoutMcompras";
	}
	
	@RequestMapping(value = "/form")
	public String crear( Map<String, Object> model) {
		
		//se añade todos los findAll que tenga los select en el formulario para poder selecionar
			Producto material = new Producto();	
		
			
			model.put("material", material);
			model.put("proveedores",proveedorService.findAll());
			model.put("titulo", "Formulario de Material");
		
		return "/productos/materialForm";
	}
	
	@RequestMapping(value="/form/{id}")
	public String editar(@PathVariable(value="id") Long id, Map<String, Object> model) {
		
		Producto material = null;
		Proveedor proveedor = null;
		proveedor = proveedorService.findOne(id);
		if(id > 0) {
			material = materialService.findOne(id);
			proveedor = proveedorService.findOne(id);
			model.put("material", material);
			model.put("titulo", "Editar material");
			model.put("proveedores",proveedor);
		} else {
			return "redirect:/listarMaterial";
		}
		model.put("material", material);
		model.put("titulo", "Editar material");
		model.put("proveedores",proveedor);
		return "/productos/materialForm";
	}
	

	//Busqueda Listar	
	@PostMapping("/buscar")
	public String buscar(
	@RequestParam(name="nombre") String nombre,
	@RequestParam(name="n_proveedor") String n_proveedor, 
	@RequestParam(name = "page", defaultValue = "0") int page, Model model) 
	
{			
		 Pageable pageRequest = PageRequest.of(page, 10);
		 Page<Producto> material =  materialService.findByNombreListar(nombre,n_proveedor,pageRequest);
		 log.info("NOMBRE DEL MATERIAL "+nombre.toString());
		 log.info("NOMBRE DEL PROVEEDOR "+n_proveedor.toString());
         PageRender<Producto> pageRender = new PageRender<Producto>("listarMaterial", material);
         
         //captura la candidad buscada en el filtro
         	model.addAttribute("cantidad",materialService.findByNombreListar(nombre,n_proveedor,pageRequest).getNumberOfElements());
			model.addAttribute("titulo", "Lista de Material");	
			model.addAttribute("textoR", "Resultados Encontrados: ");
			model.addAttribute("material", material);
			model.addAttribute("page", pageRender);	
			model.addAttribute("countMaterial", materialService.count());
			model.addAttribute("countCliente", clienteService.count());
			model.addAttribute("countProveedor", proveedorService.count());
			return "/productos/listarMaterial";
}
	
	
	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String guardar(@Valid Producto material, BindingResult result, Model model, SessionStatus status,RedirectAttributes flash) {
		
		if(result.hasErrors()) {
			
			model.addAttribute("titulo", "Formulario de Material");
			return "/productos/materialForm";
		}
		
		materialService.save(material);
		flash.addFlashAttribute("success", "Creado el material con exito");
		status.setComplete();
		return "redirect:/materiales/form";
	}
	
	@RequestMapping(value="/eliminar/{id}")
	public String eliminar(@PathVariable(value="id") Long id,RedirectAttributes flash) {
		
		if(id > 0) {
			materialService.delete(id);
		}
		flash.addFlashAttribute("success", "Eliminado el material con exito");

		return "redirect:/materiales/listarMaterial";
	}
}
