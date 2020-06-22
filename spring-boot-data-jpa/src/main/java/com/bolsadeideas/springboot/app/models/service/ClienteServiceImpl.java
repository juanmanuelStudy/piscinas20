package com.bolsadeideas.springboot.app.models.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.app.models.dao.AlbaranDao;
import com.bolsadeideas.springboot.app.models.dao.IClienteDao;
import com.bolsadeideas.springboot.app.models.dao.IFacturaDao;
import com.bolsadeideas.springboot.app.models.dao.IProductoDao;
import com.bolsadeideas.springboot.app.models.entity.Albaran;
import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.entity.Factura;
import com.bolsadeideas.springboot.app.models.entity.Producto;

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
	@Transactional
	public void saveFactura(Factura factura) {
		facturaDao.save(factura);
	}

	@Override
	@Transactional(readOnly=true)
	public Producto findProductoById(Long id) {
		// TODO Auto-generated method stub
		return productoDao.findById(id).orElse(null);
	}

	@Override
	@Transactional(readOnly=true)
	public Factura findFacturaById(Long id) {
		// TODO Auto-generated method stub
		return facturaDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void deleteFactura(Long id) {
		facturaDao.deleteById(id);
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
	public Page<Factura> findByClienteAndProveedorAndLugarAndEmviadoS(String desde,String hasta,String cliente, String proveedor, String lugar,Pageable pageable) {
		log	.info("serv S");	
		return clienteDao.findByClienteAndProveedorAndLugarAndEnviadoS(desde,hasta,cliente, proveedor, lugar, pageable);
	}

	@Override
	public Page<Factura>findByClienteAndProveedorAndLugarAndEmviadoN(String desde,String hasta,String cliente,String proveedor,String lugar,Pageable pageable) {
		log	.info("serv N");	
		return clienteDao.findByClienteAndProveedorAndLugarAndEnviadoN(desde,hasta,cliente, proveedor, lugar, pageable);
	}
	
	

	@Override
	public Factura listarFactuaByNumero(String numero) {
		
		return clienteDao.listarFactuaByNumero(numero);
	}

	@Override
	public Factura  modificarContbilizar(Factura factura) {
		
		
		
		if(factura.getId()== null) {
			em.persist(factura);
			return factura;
		}
		
		else {
			
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
