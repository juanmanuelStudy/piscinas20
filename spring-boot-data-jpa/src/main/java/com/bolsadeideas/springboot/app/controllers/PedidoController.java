package com.bolsadeideas.springboot.app.controllers;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bolsadeideas.springboot.app.models.entity.*;
import com.bolsadeideas.springboot.app.models.service.*;
import com.bolsadeideas.springboot.app.util.paginator.PageRender;
import lombok.extern.log4j.Log4j2;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
@RequestMapping("/pedidos")
@SessionAttributes("pedido")
@Log4j2
public class PedidoController {

    @Autowired
    private ResourceLoader resourceLoader;
    //ACCESO A SERVICIOS
    @Autowired
    private IClienteService clienteService;
    @Autowired
    private ProveedorServiceImpl proveedorService;
    @Autowired
    private PedidoServiceImpl pedidoService;
    @Autowired
    private IUploadFileService uploadFileService;

    @Autowired
    private ArchivoAdjuntoService archivoAdjuntoService;

    /**
     * Se precarga con valores iniciales
     */
    @PostConstruct
    public void init() {
        log.info("entra en PedidoController");
        // TODO document why this method is empty
    }

    //VARIABLES
    static final String TITULO = "titulo";
    static final String ERROR = "error";
    //VISTAS
    static final String REDIRECTLISTAR = "redirect:/pedidolistar";
    static final String PEDIDOFORM = "/pedido/pedidoform";
    static final String CREARPEDIDO = "Crear Pedido";

    @GetMapping(value = "/uploads/{filename:.+}")
    public ResponseEntity<Resource> verFoto(@PathVariable String filename) {
        log.info("entra en uploas");
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

    @RequestMapping(value = {"/listarPedidos"}, method = RequestMethod.GET)
    public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

        Pageable pageRequest = PageRequest.of(page, 6);
        Page<Pedido> pedido = pedidoService.findAll(pageRequest);
        //todo repar el paginados de los listados de  pedidos el problema es que tenia "/" en el pageRender de la url
        PageRender<Pedido> pageRender = new PageRender<>("listarPedidos", pedido);

        //Recorrer los estados para pasar al select
        List<String> estados = Arrays.asList("PENDIENTE", "REALIZANDO", "TERMINADO");
        //capturara todos los clientes para el filtro
        model.addAttribute("clientes", clienteService.findAll());

        model.addAttribute("estados", estados);
        model.addAttribute(TITULO, "Listado de Pedidos");
        model.addAttribute("pedido", pedido);
        model.addAttribute("page", pageRender);
        return "pedido/pedidolistar";
    }

    /**
     * Ver los detalles de los pedidos se debe realizar la cargar de los archivos adjuntos asociaciados al nombre de las fotos que en BD Y en el sistema operativo
     * buscar los arhvicos con el nombre de la foto y cargarlos en la vista
     * se debe realizar algun tipo de carga para que cargue todas las fotos que corresponde con el pedido
     *
     * @param id
     * @param model
     * @param flash
     * @return
     */
    @GetMapping("/ver/{id}")
    public String ver(@PathVariable(value = "id") Long id,
                      Model model,
                      RedirectAttributes flash) {
        // Buscar el pedido por su id
        Pedido pedido = clienteService.findPedidoById(id);
        if (pedido == null) {
            flash.addFlashAttribute(ERROR, "El pedido no existe en la base de datos!");
            return REDIRECTLISTAR;
        }


        // Cargar los archivos adjuntos asociados al pedido en la vista
        model.addAttribute("pedido", pedido);
      //  model.addAttribute("fotos", fotos); // Pasar la lista de nombres de fotos a la vista
        model.addAttribute(TITULO, "Detalles del Pedido");
        return "pedido/pedidover";
    }


    @GetMapping("/cargarImagenes/{id}")
    @ResponseBody
    public List<String> cargarImagenes(@PathVariable(value = "id") Long id) {
        log.info("entra en cargarImagenes");
        // Lógica para cargar las rutas de las imágenes desde el servidor
        List<String> urls = new ArrayList<>();
        // Suponiendo que tienes una lista de nombres de archivos en la base de datos
        List<ArchivoAdjunto> archivosAdjuntos = archivoAdjuntoService.findArchivosAdjuntosByPedidoId(id);
        log.info("archivosAdjuntos: " + archivosAdjuntos.size());
        for (ArchivoAdjunto archivo : archivosAdjuntos) {
            urls.add(String.valueOf(archivo.getNombre())); // Agregar el nombre del archivo a la lista de URLs
        }
        return urls;
    }

    @GetMapping("/fotos/{nombreArchivo}")
    public ResponseEntity<Resource> obtenerFoto(@PathVariable String nombreArchivo) {
        Path rutaArchivo = Paths.get("C://temp//fotos/" + nombreArchivo); // Ruta local a tus imágenes
        Resource recurso = null;
        try {
            recurso = new UrlResource(rutaArchivo.toUri());
            if (recurso.exists() || recurso.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
                        .body(recurso);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }


    /**
     * Crear los Pedidos para los clientes
     *
     * @param clienteId
     * @param model
     * @param flash
     * @return
     */
    @GetMapping("/form/{clienteId}")
    public String crear(@PathVariable(value = "clienteId") Long clienteId,
                        Map<String, Object> model,
                        RedirectAttributes flash) {
        Cliente cliente = clienteService.findOne(clienteId);
        if (cliente == null) {
            flash.addFlashAttribute(ERROR, "El cliente no existe en la base de datos");
            return REDIRECTLISTAR;
        }


        log.info("entra en PedidoController");
        Pedido numeroPedido =pedidoService.obtenerUltimoNumeroPedido();
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);

        log.info(numeroPedido);
        List<String> tipoPedido = Arrays.asList("REDISEÑO","DISEÑO NUEVO","ARREGLO" );
        model.put("tipoPedido", tipoPedido);
        List<String> estados = Arrays.asList("PENDIENTE", "REALIZANDO", "TERMINADO");
        model.put("estados", estados);

        model.put("numeroPedido", numeroPedido.getNpedido()+1);
        model.put("pedido", pedido);
        model.put("proveedores", proveedorService.findAll());
        model.put(TITULO, CREARPEDIDO);
        return PEDIDOFORM;
    }



    /**
     * Guarda los albaranes desde el formulario de pedidos con los datos del cliente
     *
     * @param result
     * @param model
     * @param flash
     * @param status
     * @return
     */
    @PostMapping("/form")
    public String guardar(@ModelAttribute @Valid Pedido pedido,
                          BindingResult result,
                          Model model,
                          @RequestParam( "npedido") Long npedido,
                          @RequestParam( "observacion") String observacion,
                          @RequestParam( "estado") String estado,
                          @RequestParam( "tipoPedido") String tipoPedido,
                          RedirectAttributes flash,
                          SessionStatus status,
                          @RequestParam("fileNamesJSON") String fileNamesJSON) {
        if (result.hasErrors()) {
            model.addAttribute(TITULO, CREARPEDIDO);
            return PEDIDOFORM;
        }

        // Obtener el pedido existente si el número de pedido coincide
        Pedido pedidoExistente = pedidoService.findOne(npedido);
        if (pedidoExistente != null) {
            // Actualizar el pedido existente con los datos del formulario
            pedidoExistente.setObservacion(observacion);
            // Añadir lógica para actualizar otros campos del pedido si es necesario
            pedidoExistente.setEstado(estado);
            // Guardar el pedido existente
            pedidoExistente.setTipoPedido(tipoPedido);
            try {
                pedidoService.save(pedidoExistente);
                flash.addFlashAttribute("info", "Pedido actualizado con éxito");
            } catch (Exception e) {
                flash.addFlashAttribute("error", "Error al actualizar el pedido: " + e.getMessage());
            }
        } else {
            // Si no hay un pedido existente con el mismo número, guardar el nuevo pedido
            try {
                pedidoService.save(pedido);
                flash.addFlashAttribute("info", "Pedido guardado con éxito");
            } catch (Exception e) {
                flash.addFlashAttribute("error", "Error al guardar el pedido: " + e.getMessage());
            }
        }
        log.info("Número de caracteres en fileNamesJSON: " + fileNamesJSON.length());

        //controlar si no hya archivos adjuntos siga el flujo
        if (fileNamesJSON.isEmpty()) {
            // Marcar la sesión como completa antes de redirigir
            status.setComplete();
            return "redirect:/pedidos/form/" + pedido.getCliente().getId();
        }
        // Convertir el JSON a una lista de nombres de archivo
        List<String> fileNamesList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(fileNamesJSON);
            for (int i = 0; i < jsonArray.length(); i++) {
                fileNamesList.add(jsonArray.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Manejar errores al convertir el JSON
        }

        log.info("Número de nombres de archivo en la lista: " + fileNamesList.size());

        // Guardar archivos adjuntos
        for (String fileName : fileNamesList) {
            log.info("Nombre de archivo: " + fileName);

            try {
                // Crear un nuevo objeto ArchivoAdjunto y establecer el nombre del archivo
                ArchivoAdjunto foto = new ArchivoAdjunto();
                foto.setNombre(fileName);
                foto.setPedido(pedido); // Establecer el pedido para la foto
                // Aquí deberías guardar el contenido del archivo, por ejemplo, en una base de datos
                archivoAdjuntoService.guardar(foto);
                flash.addFlashAttribute("info", "Pedido guardado con éxito");
                log.info("infomarcion mesj"+flash.getFlashAttributes().get("info"));
            } catch (Exception e) {
                // Manejar errores al cargar archivos adjuntos
                flash.addFlashAttribute("error", "Error al cargar el archivo: " + fileName);
                return "redirect:/pedidos/form/" + pedido.getCliente().getId();
            }
        }

        // Marcar la sesión como completa antes de redirigir
        status.setComplete();

        return "redirect:/pedidos/form/" + pedido.getCliente().getId();
    }






    /**
     * Al guardar las fotos se deben guardar en archvivos adjuntos con el numero de pedido y nombre de la foto
     * por otra parte se debe guardar en la carpeta de fotos del sistema operativo el archivo para poder ser cargado poesteriormete
     * @param pedido
     * @param foto
     * @param flash
     * @return
     */
    @PostMapping("/guardarFotos")
    public String guardarFotos(@Valid ArchivoAdjunto archivoAdjunto,@Valid Pedido pedido,
                               @RequestParam("files") MultipartFile foto,
                               RedirectAttributes flash) {
        // Directorio donde se guardarán las fotos
        String directorio = "C://temp//fotos";

        // Crear el directorio si no existe
        Path pathDirectorio = Paths.get(directorio);
        if (!Files.exists(pathDirectorio)) {
            try {
                Files.createDirectories(pathDirectorio);
            } catch (Exception e) {
                return "Error al crear el directorio";
            }
        }
        if (!foto.isEmpty()) {

            String rootPath = "C://temp//fotos";
            log.info("rootPath: " + rootPath);
            try {
                // Copiar el archivo a la carpeta uploads
                byte[] bytes = foto.getBytes();
                Path rutaCompleta = Paths.get(rootPath + "//" + foto.getOriginalFilename());
                Files.write(rutaCompleta, bytes);


                flash.addFlashAttribute("info", "Has subido correctamente '" + foto.getOriginalFilename() + "'");
                //haz que se cierre el flash a los 3 segundos


            } catch (IOException e) {
                e.printStackTrace();
            }
    }
        return "redirect:/pedidos/form/" + pedido.getCliente().getId();
    }

    /**
     * Eliminar los pedido que interesen
     *
     * @param id
     * @param flash
     * @return
     */
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {

        Pedido pedido = clienteService.findPedidoById(id);
        if (pedido != null) {
            // Eliminar todos los archivos adjuntos asociados al pedido
          //  for (ArchivoAdjunto archivoAdjunto : pedido.getArchivos()) {
                // Aquí podrías llamar a algún servicio o método para eliminar el archivo adjunto de la base de datos
                 //archivoAdjuntoService.eliminarArchivoAdjunto(archivoAdjunto);            }

            // Eliminar el pedido
            clienteService.deletePedido(id);

            flash.addFlashAttribute("success", "Pedido eliminado con éxito!");
            return "redirect:/ver/" + pedido.getCliente().getId();
        }
        flash.addFlashAttribute(ERROR, "El pedido no existe en la base de datos, no se pudo eliminar!");
        return REDIRECTLISTAR;
    }


    /**
     * Filtro para buscar los ALBARANES
     *
     * @param page
     * @param cliente
     * @param pageable
     * @param model
     * @return
     */
    @PostMapping("/buscar")
    public String buscar(
            @RequestParam(name = "page", defaultValue = "0") int page,
            //parametros de busqueda pasado en el formulario
            @RequestParam (name = "cliente") String cliente,
            @RequestParam (name = "estado") String estado,
            Pageable pageable, Model model) {

        //paginacion de las busquedas totales
        Pageable pageRequest = PageRequest.of(page, 6);
        Page<Pedido> pedido = pedidoService.findByCliente(cliente,estado, pageRequest);
        PageRender<Pedido> pageRender = new PageRender<>("listarPedidos", pedido);


        //Recorrer los estados para pasar al select
        List<String> estados = Arrays.asList("PENDIENTE", "REALIZANDO", "TERMINADO");
        //capturara todos los clientes para el filtro

        //cargramos los clientes para el filtro y estados
        model.addAttribute("clientes", clienteService.findAll());
        model.addAttribute("estados", estados);

        //captura la candidad buscada en el filtro
        model.addAttribute("cantidad", pedidoService.findByCliente(cliente,estado,pageable).getNumberOfElements());


        model.addAttribute(TITULO, "Lista de Pedidos Encontradas ");
        model.addAttribute("textoR", "Resultados Encontrados: ");
        model.addAttribute("pedido", pedido);
        model.addAttribute("page", pageRender);
        model.addAttribute("countProveedor", proveedorService.count());

        return "pedido/pedidolistar";
    }
    //controlador que elimina las fotos del directorio de fotos
    @GetMapping("/eliminarFoto/{fileName:.+}")
    public String eliminarFoto(@PathVariable(value = "fileName") String fileName,@Valid Pedido pedido, RedirectAttributes flash) {

            // Eliminar el archivo del directorio de fotos
            String rootPath = "C://temp//fotos";
            Path rutaFoto = Paths.get(rootPath + "//" + fileName);
            if (Files.exists(rutaFoto)) {
                try {
                    Files.delete(rutaFoto);
                    flash.addFlashAttribute("info", "Foto " + fileName + " eliminada con éxito!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return "redirect:/pedidos/form/" + pedido.getCliente().getId();

}
    @RequestMapping(value = "/formEditar/{id}")
    public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

        Pedido pedido = null;

        if (id > 0) {
            pedido = pedidoService.findOne(id);
            if (pedido == null) {
                flash.addFlashAttribute("error", "El ID del Pedido no existe en la BBDD!");
                return "redirect:pedido/listar";
            }
        } else {
            flash.addFlashAttribute("error", "El ID del Pedido no puede ser cero!");
            return "redirect:pedido/listar";
        }
        List<String> estados = Arrays.asList("PENDIENTE", "REALIZANDO", "TERMINADO");
        model.put("estados", estados);
        List<String> tipoPedido = Arrays.asList("REDISEÑO","DISEÑO NUEVO","ARREGLO" );
        model.put("tipoPedido", tipoPedido);
        model.put("pedido", pedido);
        model.put("titulo", "Editar Pedido");
        return "pedido/pedidoform";
    }

    @PostMapping("/report")
    public ResponseEntity<?> generateReport(HttpServletResponse response,
                                            @RequestParam(name = "cliente") String cliente,
                                            @RequestParam(name = "estado") String estado){
        log.info("cliente: " + cliente);
        log.info("estado: " + estado);
        try {
            // Generar el informe
            JasperPrint jasperPrint = pedidoService.generateJasperPrint(cliente,estado);

        // Convertir el informe a un arreglo de bytes en formato PDF
            byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

            // Configurar la respuesta HTTP para la descarga del PDF
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=pedido_report.pdf");

            // Enviar el arreglo de bytes en la respuesta
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
            //redirigir a lista de pedidos

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
