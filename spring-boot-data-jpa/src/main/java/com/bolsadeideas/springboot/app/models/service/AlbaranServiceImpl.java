package com.bolsadeideas.springboot.app.models.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.app.models.entity.Albaran;

@Service
public class AlbaranServiceImpl implements AlbaranesService {
	
	@Autowired
	private com.bolsadeideas.springboot.app.models.dao.AlbaranDao AlbaranDao;

	@Override
	@Transactional(readOnly=true)
	public Iterable<Albaran> findAll() {		
		return  AlbaranDao.findAll();
	}

	@Override
	@Transactional
	public Albaran save(Albaran albaran) {
		return AlbaranDao.save(albaran);
	}

	@Override
	@Transactional(readOnly=true)
	public Albaran findOne(Long id) {		
		return AlbaranDao.findById(id).orElse(null);
	}


	@Override
	@Transactional
	public void delete(Long id) {			
		AlbaranDao.deleteById(id);		
	}

	@Override
	@Transactional(readOnly=true)
	public long count() {			
		return AlbaranDao.count();
	}

	

	@Override
	@Transactional(readOnly=true)
	public Page<Albaran> findByClienteAndProveedorAndLugar(String cliente, String lugar, String proveedor,Pageable pageable) {		
		return AlbaranDao.findByClienteAndProveedorAndLugar(cliente, lugar, proveedor, pageable);
	}

	@Override
	@Transactional(readOnly=true)
	public Page<Albaran> findAlbaranCliente(Long idCliente, Pageable pageable) {
		return AlbaranDao.findAlbaranCliente(idCliente, pageable);
	}

	@Override
	@Transactional(readOnly=true)
	public Page<Albaran> findAll(Pageable pageable) {		
		return AlbaranDao.findAll(pageable);
	}



	
}