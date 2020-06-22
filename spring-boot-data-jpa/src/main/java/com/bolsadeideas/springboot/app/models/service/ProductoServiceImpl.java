package com.bolsadeideas.springboot.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.app.models.dao.IProductoDao;
import com.bolsadeideas.springboot.app.models.entity.Producto;



@Service
public class ProductoServiceImpl implements ProductoService {

	@Autowired
	private IProductoDao productoDao;

	@Override
	@Transactional(readOnly=true)
	public Iterable<Producto> findAll() {
		
		return productoDao.findAll();
	}

	@Override
	@Transactional(readOnly=true)
	public Page<Producto> findAll(Pageable pageable) {		
		return productoDao.findAll(pageable);
	}

	@Override
	@Transactional
	public Producto save(Producto material) {
		
		return productoDao.save(material);
	}

	@Override
	@Transactional(readOnly=true)
	public Producto findOne(Long id) {
		
		return productoDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		productoDao.deleteById(id);
		
	}

	@Override
	@Transactional(readOnly=true)
	public long count() {
		
		return productoDao.count();
	}

	@Override
	@Transactional(readOnly=true)
	public List<Producto> findByNombre(String term) {
		
		return productoDao.findByCodigo(term);
	}

	

	@Override
	@Transactional(readOnly=true)
	public Page<Producto> findByNombreListar(String cliente, String proveedor, Pageable pageable) {
		
		return productoDao.findByClienteOrProveedor(cliente, proveedor, pageable);
	}

	@Override
	@Transactional(readOnly=true)
	public List<Producto> findMaterialByProveedor(String term) {
		return productoDao.findMaterialByProveedor(term);
	}

	@Override
	@Transactional(readOnly=true)
	public List<Producto> findByNombreLikeIgnoreCase(String term) {
		return productoDao.findByCodigo(term);

	}


	
	
	

}
