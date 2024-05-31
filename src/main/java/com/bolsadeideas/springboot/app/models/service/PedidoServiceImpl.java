package com.bolsadeideas.springboot.app.models.service;


import com.bolsadeideas.springboot.app.models.dao.PedidoDao;
import com.bolsadeideas.springboot.app.models.entity.Pedido;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PedidoServiceImpl implements PedidoService {

	@Autowired
	private ResourceLoader resourceLoader;
	
	@Autowired
	private PedidoDao pedidoDao;

	@Override
	@Transactional(readOnly=true)
	public Iterable<Pedido> findAll() {
		return  pedidoDao.findAll();
	}

	@Transactional
	public Pedido save(Pedido  pedido) {
		return pedidoDao.save(pedido);
	}

	@Override
	@Transactional(readOnly=true)
	public Pedido findOne(Long id) {
		return pedidoDao.findById(id).orElse(null);
	}
	@Override
	@Transactional
	public void delete(Long id) {
		pedidoDao.deleteById(id);
	}

	@Override
	@Transactional(readOnly=true)
	public long count() {			
		return pedidoDao.count();
	}

	@Override
	@Transactional(readOnly=true)
	public Page<Pedido> findByCliente(String cliente,String estado,Pageable pageable) {
		return pedidoDao.findByClienteOrEstado(cliente,estado, pageable);
	}

	@Override
	public Pedido obtenerUltimoNumeroPedido() {

		Pedido pedido = pedidoDao.findTopByOrderByNpedidoDesc();
		if(pedido == null) {
			pedido = new Pedido();
			pedido.setNpedido(0L);
		}
		return pedido;
	}

	@Override
	public List<Pedido> findAllPedidos() {
		return (List<Pedido>) pedidoDao.findAll();
	}

	public Page<Pedido> findPedidoByIdClienteAndFinalizados(Long idCliente, Pageable pageable) {
		return  pedidoDao.findPedidosByIdClienteAndTerminado(idCliente, pageable);
	}

	@Override
	public Page<Pedido> findAllByCliente(Long idcliente, Pageable pageable) {
		return pedidoDao.findByClienteId(idcliente, pageable);
	}

	//obetenemos los pedidos por cliente y estado para poder imprimir el reporte
	@Override
	public Iterable<Pedido> findAllByClienteAndEstado(String idcliente, String estado) {
		return pedidoDao.findByClienteOrEstadoReport(idcliente,estado);

	}

	@Override
	@Transactional(readOnly=true)
	public Page<Pedido> findAll(Pageable pageable) {
		return pedidoDao.findAll(pageable);
	}


	public JasperPrint generateJasperPrint(String cliente,String estado) throws IOException, JRException {

		org.springframework.core.io.Resource resourceFoto = resourceLoader.getResource("classpath:static/jasperReport/logo.png");
		InputStream logoEmpresa = resourceFoto.getInputStream();

		//obtener el listado de pedidos con parametros de estado y cliente
		Iterable<Pedido> pedido = findAllByClienteAndEstado(cliente,estado);

		// Obtener la ruta del archivo JRXML desde el directorio static
		org.springframework.core.io.Resource resources= resourceLoader.getResource("classpath:static/jasperReport/pedidos.jrxml");
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

}