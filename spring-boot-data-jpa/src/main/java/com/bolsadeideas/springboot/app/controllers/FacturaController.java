package com.bolsadeideas.springboot.app.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

import javax.validation.Valid;

import com.bolsadeideas.springboot.app.models.dto.mapper.FacturaMapper;
import com.bolsadeideas.springboot.app.models.entity.*;
import com.bolsadeideas.springboot.app.models.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.springboot.app.util.paginator.PageRender;

import static javax.xml.bind.DatatypeConverter.parseString;


@Controller
@RequestMapping("/facturas")
@SessionAttributes("factura")
public class FacturaController {

    @Autowired
    private ProveedorServiceImpl proveedorService;
    @Autowired
    private ProductoServiceImpl materialService;
    @Autowired
    private ServiceMetodo metodoService;
    @Autowired
    private IClienteService clienteService;
    @Autowired
    private IUploadFileService uploadFileService;
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private ProveedorServiceImpl proveedores;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private FacturaMapper facturaMapper;

    @Autowired
    private NotificacionService notificacionService;

    private String fechadefactura;
    private Double total;
    private Double anticipo;

    private Date date = new Date();


    @GetMapping(value = "/uploads/{filename:.+}")
    public ResponseEntity<Resource> verFoto(@PathVariable String filename) {

        Resource recurso = null;

        try {
            recurso = uploadFileService.load(filename);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"").body(recurso);
    }


    @RequestMapping(value = "/listarFacturasSinContabilizar", method = RequestMethod.GET)
    public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

        Pageable pageRequest = PageRequest.of(page, 4);

        Page<Factura> facturas = clienteService.findByvenviadoagestor(pageRequest);

        List<String> tipos = Arrays.asList("REDISEÑO", "DISEÑO NUEVO", "ARREGLO");

        //cargramos los clientes para el filtro y tipo de pedido
        model.addAttribute("clientes", clienteService.findAll());
        model.addAttribute("tipos", tipos);

        PageRender<Factura> pageRender = new PageRender<Factura>("listarFactura", facturas);
        model.addAttribute("titulo", "Listado de Facturas Sin Contabilizar");
        model.addAttribute("facturas", facturas);
        model.addAttribute("page", pageRender);
        return "factura/listarFactura";
    }

    @RequestMapping(value = "/listarFacturasContabilizadas", method = RequestMethod.GET)
    public String listarB(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

        Pageable pageRequest = PageRequest.of(page, 4);

        Page<Factura> facturas = clienteService.findByvenviadoagestorS(pageRequest);

        PageRender<Factura> pageRender = new PageRender<Factura>("/listarFacturaCon", facturas);
        model.addAttribute("titulo", "Listado de Facturas Contabilizadas");
        model.addAttribute("facturas", facturas);
        model.addAttribute("date", date);
        model.addAttribute("page", pageRender);
        return "factura/listarFacturaCon";
    }


    @GetMapping("/ver/{id}")
    public String ver(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {
        Factura factura = clienteService.findFacturaById(id);

        if (factura == null) {
            flash.addFlashAttribute("error", "La factura no existe en la base de datos!");
            return "redirect:/listar";
        }

        //fechadefactura = factura.getDfechaFactura().substring(2,4);
        //anticipo =factura.getNanticipo();
        total = factura.getTotal();

        model.addAttribute("factura", factura);
        model.addAttribute("titulo", "FACTURA : ".concat(factura.getId().toString()));
        model.addAttribute("fechadefactura", fechadefactura);
        model.addAttribute("anticipo", anticipo);
        model.addAttribute("total", total);
        return "factura/ver";
    }

    @GetMapping("/form/{clienteId}")
    public String crear(@PathVariable(value = "clienteId") Long clienteId, Map<String, Object> model, RedirectAttributes flash) {


        Pedido pedido = pedidoService.findOne(clienteId);

        Factura factura = new Factura();
        factura.setNpedido(String.valueOf(pedido.getNpedido()));
        factura.setCliente(pedido.getCliente());
        factura.setDfechaAlbaran(String.valueOf(pedido.getDfecha()));
        factura.setTipoPedido(pedido.getTipoPedido());


        model.put("factura", factura);
        model.put("titulo", "Crear Factura de Ventas");

        return "factura/form";
    }

    @ModelAttribute("metodos")
    public List<String> metodos() {
        return Arrays.asList("30 días", "60 días", "90 días", "Efectivo", "Anticipo");

    }

    @GetMapping("cargar-productos")
    public @ResponseBody List<Producto> cargarProductos() {
        log.info(clienteService.findAllProducto().toString());
        return clienteService.findAllProducto();
    }


    @GetMapping(value = "/cargar-productos/{term}", produces = {"application/json"})
    public @ResponseBody List<Producto> cargarProductos(@PathVariable String term) {
        return clienteService.findByNombre(term);
    }

    @PostMapping("/form")
    public String guardar(@Valid Factura factura,
                          BindingResult result, Model model,
                          @RequestParam(name = "item_id[]", required = false) Long[] itemId,
                          @RequestParam(name = "cantidad[]", required = false) Integer[] cantidad,
                          @RequestParam(value = "npedido") Long npedido,
                          RedirectAttributes flash,
                          SessionStatus status) {

        // Obtener el numero de pedido por npedido
        Pedido pedido = pedidoService.findOne(npedido);
        pedido.setFacturado(true);
        pedidoService.save(pedido);

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Crear Factura");
            return "factura/form";
        }

        if (itemId == null || itemId.length == 0) {
            model.addAttribute("titulo", "Factura de Venta");
            model.addAttribute("error", "Error: La factura NO puede no tener líneas!");
            return "factura/form";
        }

        for (int i = 0; i < itemId.length; i++) {
            Producto producto = clienteService.findProductoById(itemId[i]);

            ItemFactura linea = new ItemFactura();
            linea.setCantidad(cantidad[i]);
            linea.setProducto(producto);
            factura.addItemFactura(linea);

            log.info("ID: " + itemId[i].toString() + ", cantidad: " + cantidad[i].toString());
        }

        // Iteramos sobre los ítems para restar la cantidad de material
        IntStream.range(0, itemId.length)
                .forEach(i -> {
                    Producto producto = clienteService.findProductoById(itemId[i]);
                    producto.setCantidad(producto.getCantidad() - cantidad[i]);
                    materialService.save(producto);
                });

        clienteService.saveFactura(factura);
        notificacionService.verificarStock();
        status.setComplete();
        flash.addFlashAttribute("success", "Factura'"+ factura.getNpersonal()+"'"+" creada con éxito!");

        //return "redirect:/ver/" + factura.getCliente().getId();
        //redirigue a la lista de clientes
        return "redirect:/listar";
    }



    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {

        Factura factura = clienteService.findFacturaById(id);

        if (factura != null) {
            clienteService.deleteFactura(id);
            flash.addFlashAttribute("success", "Factura eliminada con éxito!");
            return "redirect:/ver/" + factura.getCliente().getId();
        }
        flash.addFlashAttribute("error", "La factura no existe en la base de datos, no se pudo eliminar!");

        return "redirect:/listar";
    }


    //filtro de busqueda de facturas
    @PostMapping("/buscar")
    public String buscar(@RequestParam(name = "page", defaultValue = "0") int page,
                         //@RequestParam(name= "proveedor") String proveedor,
                         @RequestParam(name = "cliente") String cliente, @RequestParam(name = "tipo") String tipo,
                         //	@RequestParam(name = "lugar")String lugar,
                         // @RequestParam(name = "desde")String desde,
                         //@RequestParam(name = "hasta")String hasta,
                         Model model) {

//paginacion de las busquedas totales
        Pageable pageRequest = PageRequest.of(page, 10);
        Page<Factura> factura = clienteService.findByClienteAndProveedorAndTipo(cliente, tipo, pageRequest);
        log.info("NOMBRE DEL Cliente " + cliente.toString());
        PageRender<Factura> pageRender = new PageRender<Factura>("/listarFactura", factura);

        List<String> tipos = Arrays.asList("REDISEÑO", "DISEÑO NUEVO", "ARREGLO");

        //cargramos los clientes para el filtro y tipo de pedido
        model.addAttribute("clientes", clienteService.findAll());
        model.addAttribute("tipos", tipos);

        //captura la candidad buscada en el filtro
        model.addAttribute("cantidad", clienteService.findByClienteAndProveedorAndTipo(cliente, tipo, pageRequest).getNumberOfElements());

        log.info("cantidad encontrada  N :" + clienteService.findByClienteAndProveedorAndTipo(cliente, tipo, pageRequest).getNumberOfElements());

        model.addAttribute("titulo", "Lista de Facturas Encontradas Sin Contabilizar");
        model.addAttribute("textoR", "Resultados Encontrados: ");
        model.addAttribute("facturas", factura);
        model.addAttribute("page", pageRender);
        model.addAttribute("countMaterial", materialService.count());
        model.addAttribute("countProveedor", proveedorService.count());

        return "factura/listarFactura";
    }

    //filtro de busqueda de facturas
    @PostMapping("/buscarCon")
    public String buscarCon(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "proveedor") String proveedor, @RequestParam(name = "cliente") String cliente, @RequestParam(name = "lugar") String lugar,

                            @RequestParam(name = "desde") String desde,

                            @RequestParam(name = "hasta") String hasta,

                            Model model) {


        Pageable pageRequest = PageRequest.of(page, 10);
        Page<Factura> factura = clienteService.findByClienteAndProveedorAndLugarAndEmviadoS(desde, hasta, cliente, proveedor, lugar, pageRequest);


        log.info("NOMBRE DEL Cliente " + cliente.toString());
        log.info("NOMBRE DEL PROVEEDOR " + proveedor.toString());
        log.info("NOMBRE DEL Lugar " + lugar.toString());/*
         * log.info("NOMBRE DEL fecha "+desde.toString());
         * log.info("NOMBRE DEL fecha "+hasta.toString());
         */

        PageRender<Factura> pageRender = new PageRender<Factura>("/listarFacturaCon", factura);

        //captura la candidad buscada en el filtro
        model.addAttribute("cantidad", clienteService.findByClienteAndProveedorAndLugarAndEmviadoS(desde, hasta, cliente, proveedor, lugar, pageRequest).getNumberOfElements());
        log.info("cantidad encontrada S " + clienteService.findByClienteAndProveedorAndLugarAndEmviadoS(desde, hasta, cliente, proveedor, lugar, pageRequest).getNumberOfElements());

        model.addAttribute("titulo", "Lista de Facturas Encontradas Contabilizadas");
        //model.addAttribute("textoR", "Resultados Encontrados: ");
        model.addAttribute("facturas", factura);
        model.addAttribute("page", pageRender);
        //model.addAttribute("countMaterial", materialService.count());
        //model.addAttribute("countProveedor", proveedorService.count());

        return "factura/listarFacturaCon";
    }

    @GetMapping("/contabilizar/{npersonal}")
    public String crearConta(@PathVariable(value = "npersonal") Long npersonal, Map<String, Object> model, RedirectAttributes flash) {

        Optional<Factura> numerofactura = clienteService.findOneBy(npersonal);

        if (numerofactura.isPresent()) {
            Factura factura = numerofactura.get();
            factura.setId(npersonal);
            factura.setEnviadoagestor("SI");
            //factura.setNpagada_factura("SI");
            //TODO CUANDO EXITA USUARIO HACER UNA TABLA QUE PONGA DECLARADO Y AÑADA USUARIO Y ID FACTURA Y FECHA NEW DATE()

            clienteService.saveFactura(factura);
            flash.addFlashAttribute("info", "La factura esta contabilizada con exito!");

            return "redirect:/facturas/listarFacturasSinContabilizar";

        }
        return "factura/listarFactura";
    }
}
