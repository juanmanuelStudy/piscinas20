package com.bolsadeideas.springboot.app.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bolsadeideas.springboot.app.models.entity.MetodoPago;

public interface IMetodo {
	
	public List<MetodoPago> findAll();

	public Page<MetodoPago> findAll(Pageable pageable);

	public MetodoPago save(MetodoPago metodo);

	public MetodoPago findOne(Long id);

	public void delete(Long id);
}
