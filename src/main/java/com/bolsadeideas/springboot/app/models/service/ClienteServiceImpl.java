package com.bolsadeideas.springboot.app.models.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import javax.persistence.EntityManager;

import com.bolsadeideas.springboot.app.models.dao.*;
import com.bolsadeideas.springboot.app.models.entity.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteServiceImpl implements IClienteService {

    @Autowired
    private IClienteDao clienteDao;

    @Autowired
    private IProductoDao productoDao;

    @Autowired
    private IFacturaDao facturaDao;

    @Autowired
    private AlbaranDao albaranDAO;

    @Autowired
    private PedidoDao pedidoDao;

    @Autowired
    private ResourceLoader resourceLoader;

    private final Logger log = LoggerFactory.getLogger(getClass());


    EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> findAll() {
        // TODO Auto-generated method stub
        return (List<Cliente>) clienteDao.findAll();
    }

    @Override
    @Transactional
    public void save(Cliente cliente) {
        clienteDao.save(cliente);

    }

    @Override
    @Transactional(readOnly = true)
    public Cliente findOne(Long id) {
        // TODO Auto-generated method stub
        return clienteDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        clienteDao.deleteById(id);

    }

    @Override
    @Transactional(readOnly = true)
    public Page<Cliente> findAll(Pageable pageable) {
        return clienteDao.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> findByNombre(String term) {
        return productoDao.findByCodigoLikeIgnoreCase("%" + term + "%");
    }

    @Override
    public List<Producto> findAllProducto() {
        return (List<Producto>) productoDao.findAll();
    }

    @Override
    @Transactional
    public void saveFactura(Factura factura) {
        facturaDao.save(factura);
    }

    @Override
    @Transactional(readOnly = true)
    public Producto findProductoById(Long id) {
        // TODO Auto-generated method stub
        return productoDao.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Factura findFacturaById(Long id) {

        return facturaDao.findById(id).orElse(null);
    }

    @Override
    public Page<Factura> findFacturaByIdPage(Long id, Pageable pageRequestFactura) {
        return facturaDao.findFacturaByClientid(id, pageRequestFactura);
    }

    @Override
    @Transactional
    public void deleteFactura(Long id) {
        facturaDao.deleteById(id);
    }

    @Override
    public void deletePedido(Long id) {
        pedidoDao.deleteById(id);
    }


    @Override
    public void saveAlbaran(Albaran albaran) {
        albaranDAO.save(albaran);

    }

    @Override
    public Albaran findAlbaranById(Long id) {

        return albaranDAO.findById(id).orElse(null);
    }

    @Override
    public Pedido findPedidoById(Long id) {
        return pedidoDao.findById(id).orElse(null);
    }

    @Override
    public void deleteAlbaran(Long id) {
        albaranDAO.deleteById(id);

    }

    @Override
    public Page<Cliente> findByNombreListar(String cliente, Pageable pageable) {

        return clienteDao.findByNombreListar(cliente, pageable);
    }

    public Object count() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Page<Factura> findByvenviadoagestor(Pageable pageRequest) {

        return facturaDao.findByvenviadoagestor(pageRequest);
    }

    @Override
    public Page<Factura> findByvenviadoagestorS(Pageable pageRequest) {

        return facturaDao.findByvenviadoagestorS(pageRequest);
    }

    @Override
    public Optional<Factura> findOneBy(Long id) {
        // TODO Auto-generated method stub
        return facturaDao.findById(id);
    }

    @Override
    public Page<Factura> findByClienteAndProveedorAndLugarAndEmviadoS(String desde, String hasta, String cliente, String proveedor, String lugar, Pageable pageable) {
        log.info("serv S");
        return clienteDao.findByClienteAndProveedorAndLugarAndEnviadoS(desde, hasta, cliente, proveedor, lugar, pageable);
    }

    @Override
    public Page<Factura> findByClienteAndProveedorAndLugarAndEmviadoN(String desde, String hasta, String cliente, String proveedor, String lugar, Pageable pageable) {
        log.info("serv N");
        return clienteDao.findByClienteAndProveedorAndLugarAndEnviadoN(desde, hasta, cliente, proveedor, lugar, pageable);
    }

    @Override
    public Page<Factura> findByClienteAndProveedorAndTipo(String cliente, String tipo, Pageable pageable) {
        return clienteDao.findByClienteAndProveedorAndTipos(cliente, tipo, pageable);
    }

    @Override
    public Factura listarFactuaByNumero(String numero) {

        return clienteDao.listarFactuaByNumero(numero);
    }

    @Override
    public Page<Factura> findAllByCliente(Long id, Pageable pageable) {
        return clienteDao.findByCliente(id, pageable);
    }


    @Override
    public Page<Factura> findFacturaAll(Pageable pageable) {
        return facturaDao.findAll(pageable);
    }


    @Override
    public JasperPrint generateJasperPrints(String cliente, String tipo) throws IOException, JRException {

        org.springframework.core.io.Resource resourceFoto = resourceLoader.getResource("classpath:static/jasperReport/logo.png");
        InputStream logoEmpresa = resourceFoto.getInputStream();

        //obtener el listado de pedidos con parametros de estado y cliente
        Iterable<Factura> pedido = findAllByClienteAndTipo(cliente, tipo);

        log.info("servicio "+cliente+tipo);
        // Obtener la ruta del archivo JRXML desde el directorio static
        org.springframework.core.io.Resource resources = resourceLoader.getResource("classpath:static/jasperReport/albaran.jrxml");
        InputStream jrxmlFilePath = resources.getInputStream();

        // Obtener la plantilla del informe (.jrxml) y compilarla
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFilePath);

        // Crear el DataSource del informe
        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource((Collection<?>) pedido);

        // Mapa de parámetros para el informe
        Map<String, Object> parameters = new HashMap<>();

        // Llenar los parámetros del informe (si es necesario)
        parameters.put("ds", ds);
        parameters.put("logo", logoEmpresa);

        // Generar el informe
        return JasperFillManager.fillReport(jasperReport, parameters, ds);
    }

    private Iterable<Factura> findAllByClienteAndTipo(String cliente, String tipo) {
        return clienteDao.findByClienteAndProveedorAndTipo(cliente, tipo);
    }


    /*
        @Override
        public Factura obtenerUltimaFactura() {
            return clienteDao.findTopByOrderByNfacturaDesc();
        }*/
    @Override
    public Factura modificarContbilizar(Factura factura) {
        if (factura.getId() == null) {
            em.persist(factura);
            return factura;
        } else {
            return em.merge(factura);
        }
    }

    @Override
    public Page<Factura> findByClienteAndProveedorAndLugarAndEnviadoSS(String cliente, String proveedor, String lugar,
                                                                       Pageable pageable) {
        // TODO Auto-generated method stub
        return null;
    }

}
