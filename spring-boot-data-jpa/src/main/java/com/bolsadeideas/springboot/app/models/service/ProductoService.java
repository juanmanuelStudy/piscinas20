package com.bolsadeideas.springboot.app.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bolsadeideas.springboot.app.models.entity.Producto;


public interface ProductoService {

	public Iterable<Producto> findAll();

	public Page<Producto> findAll(Pageable pageable);

	public Producto save(Producto material);

	public Producto findOne(Long id);

	public void delete(Long id);
	
	public long count();

	public List<Producto> findByNombre(String term);

	Page<Producto> findByNombreListar(String cliente,String n_proveedor, Pageable pageable);
	
	public List<Producto> findMaterialByProveedor(String term);	
	
	public List<Producto> findByNombreLikeIgnoreCase(String term);
}
