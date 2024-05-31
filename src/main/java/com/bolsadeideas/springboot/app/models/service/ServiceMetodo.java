package com.bolsadeideas.springboot.app.models.service;

import java.util.List;

import org.hibernate.boot.Metadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bolsadeideas.springboot.app.models.dao.MetodoDao;
import com.bolsadeideas.springboot.app.models.entity.MetodoPago;

@Service
public class ServiceMetodo implements IMetodo {

	@Autowired
	private MetodoDao metodo;
	
	@Override
	public List<MetodoPago> findAll() {
		// TODO Auto-generated method stub
		return (List<MetodoPago>) metodo.findAll();
	}

	@Override
	public Page<MetodoPago> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return metodo.findAll(pageable);
	}


	

	@Override
	public MetodoPago findOne(Long id) {
		// TODO Auto-generated method stub
		return metodo.findById(id).orElse(null);
	}

	@Override
	public void delete(Long id) {
		metodo.deleteById(id);		
	}

	@Override
	public MetodoPago save(MetodoPago metodos) {
		return metodo.save(metodos);
	}

}
