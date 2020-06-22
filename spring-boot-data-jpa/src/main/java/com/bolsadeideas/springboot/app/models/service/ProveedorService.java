package com.bolsadeideas.springboot.app.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bolsadeideas.springboot.app.models.entity.Proveedor;


public interface ProveedorService {
	
	public List<Proveedor> findAll();

	public Page<Proveedor> findAll(Pageable pageable);

	public Proveedor save(Proveedor proveedor);

	public Proveedor findOne(Long id);

	public void delete(Long id);
	
	public long count();

	public List<Proveedor> findByNombre(String term);
	
	public Page<Proveedor> findByNombreListar(String nombre, Pageable pageRequest);
	
	
}
