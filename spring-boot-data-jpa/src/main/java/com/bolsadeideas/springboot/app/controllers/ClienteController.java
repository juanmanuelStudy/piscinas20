package com.bolsadeideas.springboot.app.controllers;


import java.io.File;


import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;


import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.bolsadeideas.springboot.app.models.entity.Albaran;
import com.bolsadeideas.springboot.app.models.entity.Pedido;
import com.bolsadeideas.springboot.app.models.service.PedidoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.entity.Factura;
import com.bolsadeideas.springboot.app.models.service.IClienteService;
import com.bolsadeideas.springboot.app.models.service.IUploadFileService;
import com.bolsadeideas.springboot.app.util.paginator.PageRender;


@Controller
@SessionAttributes("cliente")
public class ClienteController {

    private String fechadefactura;

    @Autowired
    private IClienteService clienteService;

    @Autowired
    private IUploadFileService uploadFileService;

    @Autowired
    private PedidoServiceImpl pedidoService;

    @GetMapping(value = "/uploads/{filename:.+}")
    public ResponseEntity<Resource> verFoto(@PathVariable String filename) {

        Resource recurso = null;

        try {
            recurso = uploadFileService.load(filename);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
                .body(recurso);
    }

    @GetMapping(value = "/ver/{id}")
    public String ver(@PathVariable(value = "id") Long id,
                      Map<String, Object> model,
                      RedirectAttributes flash,
                      @RequestParam(name = "pagePedido", defaultValue = "0") int pagePedido,
                      @RequestParam(name = "pageFactura", defaultValue = "0") int pageFactura) {

        Cliente cliente = clienteService.findOne(id);
        if (cliente == null) {
            flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
            return "redirect:/listar";
        }

        // Paginación para el listado de pedidos
        Pageable pageRequestPedido = PageRequest.of(pagePedido, 4);
        Page<Pedido> pedido = pedidoService.findAllByCliente(id, pageRequestPedido);
        PageRender<Pedido> pageRenderPedido = new PageRender<>("/ver/" + id, pedido);

        // Paginación para el listado de facturas
        Pageable pageRequestFactura = PageRequest.of(pageFactura, 4);
        Page<Factura> factura = clienteService.findFacturaByIdPage(id, pageRequestFactura);
        PageRender<Factura> pageRenderFactura = new PageRender<>("/ver/" + id, factura);

        model.put("pedido", pedido);
        model.put("cliente", cliente);
        model.put("factura", factura);
        model.put("titulo", "Detalles cliente: " + cliente.getNombre());
        model.put("pagePedido", pageRenderPedido);
        model.put("pageFactura", pageRenderFactura);

        return "ver";
    }



    @RequestMapping(value = {"/listar", "/"}, method = RequestMethod.GET)
    public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

        Pageable pageRequest = PageRequest.of(page, 4);

        Page<Cliente> clientes = clienteService.findAll(pageRequest);

        PageRender<Cliente> pageRender = new PageRender<Cliente>("/listar", clientes);
        model.addAttribute("titulo", "Listado de clientes");
        model.addAttribute("clientes", clientes);
        model.addAttribute("page", pageRender);
        //return "listar";
        return "listar";
    }

    @RequestMapping(value = "/form")
    public String crear(Map<String, Object> model) {

        Cliente cliente = new Cliente();
        model.put("cliente", cliente);
        model.put("titulo", "Formulario de Cliente");
        return "form";
    }

    @RequestMapping(value = "/form/{id}")
    public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

        Cliente cliente = null;

        if (id > 0) {
            cliente = clienteService.findOne(id);
            if (cliente == null) {
                flash.addFlashAttribute("error", "El ID del cliente no existe en la BBDD!");
                return "redirect:/listar";
            }
        } else {
            flash.addFlashAttribute("error", "El ID del cliente no puede ser cero!");
            return "redirect:/listar";
        }
        model.put("cliente", cliente);
        model.put("titulo", "Editar Cliente");
        return "form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String guardar(@Valid Cliente cliente, BindingResult result, Model model,
                          RedirectAttributes flash, SessionStatus status) {

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Formulario de Cliente");
            return "form";
        }


        String mensajeFlash = (cliente.getId() != null) ? "Cliente editado con éxito!" : "Cliente creado con éxito!";

        clienteService.save(cliente);
        status.setComplete();
        flash.addFlashAttribute("success", mensajeFlash);
        return "redirect:form";
    }

    @RequestMapping(value = "/eliminar/{id}")
    public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {

        if (id > 0) {
            Cliente cliente = clienteService.findOne(id);

            clienteService.delete(id);
            flash.addFlashAttribute("success", "Cliente eliminado con éxito!");

            if (uploadFileService.delete(cliente.getFoto())) {
                flash.addFlashAttribute("info", "Foto " + cliente.getFoto() + " eliminada con exito!");
            }

        }
        return "redirect:/listar";
    }

    //Busqueda Listar
    @PostMapping("/buscar")
    public String buscar(
            @RequestParam(name = "nombre") String nombre,
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model) {
        Pageable pageRequest = PageRequest.of(page, 10);
        Page<Cliente> clientes = clienteService.findByNombreListar(nombre, pageRequest);

        PageRender<Cliente> pageRender = new PageRender<Cliente>("/listar", clientes);
        model.addAttribute("titulo", "Lista de Usuario");
        model.addAttribute("clientes", clientes);
        model.addAttribute("page", pageRender);
        return "listar";
    }

    @GetMapping("/fotos/{nombreArchivo}")
    public ResponseEntity<Resource> obtenerFoto(@PathVariable String nombreArchivo) {
        // Lógica para leer el archivo desde el sistema de archivos
        Path archivo = Paths.get("c:/temp/fotos/", nombreArchivo);
        Resource recurso = new FileSystemResource(archivo);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // Cambia el tipo de contenido según el tipo de archivo
                .contentType(MediaType.IMAGE_PNG)
                .contentType(MediaType.IMAGE_GIF)
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
                .body(recurso);
    }


}