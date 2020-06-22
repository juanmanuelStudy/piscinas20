package com.bolsadeideas.springboot.app.models.service;



import org.springframework.data.domain.Pageable;

import com.bolsadeideas.springboot.app.models.entity.Albaran;

import org.springframework.data.domain.Page;


public interface AlbaranesService {
	
	public Iterable<Albaran> findAll();

	public Page<Albaran> findAll(Pageable pageable);

	public Albaran save(Albaran albaran);

	public Albaran findOne(Long id);

	public void delete(Long id);
	
	public long count();
	
	public Page<Albaran> findByClienteAndProveedorAndLugar(String cliente,String lugar,String proveedor, Pageable pageable);

	
}
