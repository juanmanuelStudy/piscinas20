package com.bolsadeideas.springboot.app.controllers;


import java.io.IOException;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.springboot.app.models.entity.Albaran;
import com.bolsadeideas.springboot.app.models.entity.Cliente;

import com.bolsadeideas.springboot.app.models.entity.ItemAlbaran;
import com.bolsadeideas.springboot.app.models.entity.Producto;
import com.bolsadeideas.springboot.app.models.service.AlbaranServiceImpl;
import com.bolsadeideas.springboot.app.models.service.IClienteService;
import com.bolsadeideas.springboot.app.models.service.IUploadFileService;

import com.bolsadeideas.springboot.app.models.service.ProveedorServiceImpl;
import com.bolsadeideas.springboot.app.util.paginator.PageRender;
@Controller
@RequestMapping("/albaranes")
@SessionAttributes("albaran")
public class AlbaranController {

    //ACCESO A SERVICIOS
    @Autowired
    private IClienteService clienteService;
    @Autowired
    private ProveedorServiceImpl proveedorService;
    @Autowired
    private AlbaranServiceImpl albaranService;
    @Autowired
    private IUploadFileService uploadFileService;

    /**
     * Se precarga con valores iniciales
     */
    @PostConstruct
    public void init() {
        // TODO document why this method is empty
    }

    //VARIABLES

    static final String TITULO = "titulo";
    static final String ERROR = "error";

    //VISTAS
    static final String REDIRECTLISTAR = "redirect:/listar";
    static final String ALBARANFORM = "albaran/form";
    static final String CREARALBARAN = "Crear Albaran";

    @GetMapping(value = "/uploads/{filename:.+}")
    public ResponseEntity<Resource> verFoto(@PathVariable String filename) {
        Resource recurso = null;
        try {
            recurso = uploadFileService.load(filename);

            if (recurso != null) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + recurso.getFilename() + "\"")
                        .body(recurso);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            // Manejar la excepción adecuadamente, por ejemplo, lanzar una excepción personalizada o registrarla.
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping(value = "/listarAlbaranes")
    public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

        Pageable pageRequest = PageRequest.of(page, 4);
        Page<Albaran> albaranes = albaranService.findAll(pageRequest);
        PageRender<Albaran> pageRender = new PageRender<>("/listarAlbaranes", albaranes);
        model.addAttribute(TITULO, "Listado de Albaranes");
        model.addAttribute("albaranes", albaranes);
        model.addAttribute("page", pageRender);
        return "albaran/listarAlbaranes";
    }

    @GetMapping("/ver/{id}")
    public String ver(@PathVariable(value = "id") Long id,
                      Model model,
                      RedirectAttributes flash) {
        Albaran albaran = clienteService.findAlbaranById(id);
        if (albaran == null) {
            flash.addFlashAttribute(ERROR, "La albaran no existe en la base de datos!");
            return REDIRECTLISTAR;
        }
        model.addAttribute("albaran", albaran);
        model.addAttribute(TITULO, "Detalles del Albaran");
        return "albaran/ver";
    }

    /**
     * Crear los albaranes para las facturas
     *
     * @param clienteId
     * @param model
     * @param flash
     * @return
     */
    @GetMapping("/form/{clienteId}")
    public String crear(@PathVariable(value = "clienteId") Long clienteId, Map<String, Object> model,
                        RedirectAttributes flash) {
        Cliente cliente = clienteService.findOne(clienteId);
        if (cliente == null) {
            flash.addFlashAttribute(ERROR, "El cliente no existe en la base de datos");
            return REDIRECTLISTAR;
        }
        Albaran albaran = new Albaran();
        albaran.setCliente(cliente);
        model.put("albaran", albaran);
        model.put("proveedores", proveedorService.findAll());
        model.put(TITULO, CREARALBARAN);
        return ALBARANFORM;
    }

    /**
     * Muestra la lista de Productos creados
     */
    @GetMapping(value = "/cargar-productos/{term}", produces = {"application/json"})
    public @ResponseBody List<Producto> cargarProductos(@PathVariable String term) {
        return clienteService.findByNombre(term);
    }

    /**
     * Guarda los albaranes desde el formulario
     *
     * @param albaran
     * @param result
     * @param model
     * @param itemId
     * @param cantidad
     * @param flash
     * @param status
     * @param foto
     * @return
     */
    @PostMapping("/form")
    public String guardar(@Valid Albaran albaran,
                          BindingResult result, Model model,
                          @RequestParam(name = "item_id[]", required = false) Long[] itemId,
                          @RequestParam(name = "cantidad[]", required = false) Integer[] cantidad,
                          RedirectAttributes flash,
                          SessionStatus status,
                          @RequestParam("file") MultipartFile foto) {
        if (result.hasErrors()) {
            model.addAttribute(TITULO, CREARALBARAN);
            return ALBARANFORM;
        }

        if (itemId == null || itemId.length == 0) {
            model.addAttribute(TITULO, CREARALBARAN);
            model.addAttribute(ERROR, "Error: La Albaran NO puede no tener líneas!");
            return ALBARANFORM;
        }
        for (int i = 0; i < itemId.length; i++) {
            Producto producto = clienteService.findProductoById(itemId[i]);
            ItemAlbaran linea = new ItemAlbaran();
            linea.setCantidad(cantidad[i]);
            linea.setProducto(producto);
            albaran.addItemAlbaran(linea);

        }
        if (!foto.isEmpty()) {

            if (albaran.getId() != null && albaran.getId() > 0 && albaran.getFoto() != null
                    && albaran.getFoto().length() > 0) {

                uploadFileService.delete(albaran.getFoto());
            }
            String uniqueFilename = null;
            try {
                uniqueFilename = uploadFileService.copy(foto);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            flash.addFlashAttribute("info", "Has subido correctamente '" + uniqueFilename + "'");

            if (uniqueFilename != null) {
                albaran.setFoto(uniqueFilename);
            } else {
                albaran.setFoto(null);
            }
        }
        albaranService.save(albaran);
        status.setComplete();
        flash.addFlashAttribute("success", "Albaran '" + albaran.getNumeroAlbaran() + "'" + " creado con éxito!");
        return "redirect:/albaranes/form/" + albaran.getCliente().getId();
    }

    /**
     * Elimina los albaranes que interesen
     *
     * @param id
     * @param flash
     * @return
     */
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {

        Albaran albaran = clienteService.findAlbaranById(id);
        if (albaran != null) {
            clienteService.deleteFactura(id);
            flash.addFlashAttribute("success", "Albaran eliminada con éxito!");
            return "redirect:/ver/" + albaran.getCliente().getId();
        }
        flash.addFlashAttribute(ERROR, "La factura no existe en la base de datos, no se pudo eliminar!");
        return REDIRECTLISTAR;
    }

    /**
     * Filtro para buscar los ALBARANES
     *
     * @param page
     * @param proveedor
     * @param cliente
     * @param lugar
     * @param pageable
     * @param model
     * @return
     */
    @PostMapping("/buscar")
    public String buscar(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "proveedor") String proveedor,
            @RequestParam(name = "cliente") String cliente,
            @RequestParam(name = "lugar") String lugar,
            Pageable pageable, Model model) {

        Page<Albaran> albaran = albaranService.findByClienteAndProveedorAndLugar(cliente, lugar, proveedor, pageable);
        PageRender<Albaran> pageRender = new PageRender<>("/listarAlbaran", albaran);

        //captura la candidad buscada en el filtro
        model.addAttribute("cantidad", albaranService.findByClienteAndProveedorAndLugar(cliente, lugar, proveedor, pageable).getNumberOfElements());
        model.addAttribute(TITULO, "Lista de ALbaranes Encontradas ");
        model.addAttribute("textoR", "Resultados Encontrados: ");
        model.addAttribute("albaranes", albaran);
        model.addAttribute("page", pageRender);
        model.addAttribute("countProveedor", proveedorService.count());
        return "albaran/listarAlbaranes";
    }

}
