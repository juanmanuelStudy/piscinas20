package com.bolsadeideas.springboot.app.models.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.bolsadeideas.springboot.app.models.dao.ProveedorDao;
import com.bolsadeideas.springboot.app.models.entity.Proveedor;

@Service
public class ProveedorServiceImpl implements ProveedorService {

	@Autowired
	private ProveedorDao proveedorDao;
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public List<Proveedor> findAll() {
		return  (List<Proveedor>) proveedorDao.findAll();
	}
	@Override
	public Page<Proveedor> findAll(Pageable pageable) {
		return proveedorDao.findAll(pageable);
	}
	
	@Override
	public Proveedor findOne(Long id) {
		// TODO Auto-generated method stub
		return proveedorDao.findById(id).orElse(null);
	}

	@Override
	public void delete(Long id) {
		proveedorDao.deleteById(id);		
	}

	@Override
	public long count() {
		return proveedorDao.count();
	}

	@Override
	public List<Proveedor> findByNombre(String term) {
		return  proveedorDao.findByvNombreLikeIgnoreCase("%" + term + "%");
	}
	
	@Override
	public Proveedor save(Proveedor proveedor) {
		return proveedorDao.save(proveedor);
	}
	
	@Override
	public Page<Proveedor> findByNombreListar(String nombre, Pageable pageRequest) {		
		return proveedorDao.findByNombreListar(nombre, pageRequest);
	}


}
