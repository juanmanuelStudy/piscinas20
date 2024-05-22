package com.bolsadeideas.springboot.app.models.dao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bolsadeideas.springboot.app.models.entity.Albaran;


public interface AlbaranDao extends PagingAndSortingRepository<Albaran, Long> {

	
	@Query("select p from Albaran p where p.cliente.nombre like %?1% and p.lugar like %?2% and  p.proveedor.vnombre like %?3%")
	public Page<Albaran> findByClienteAndProveedorAndLugar(String cliente,String lugar,String proveedor, Pageable pageable);

	@Query("select  COUNT(*) FROM Albaran")
	public  long count();

	@Query("select p from Albaran p where p.cliente.id = ?1")
	public Page<Albaran> findAlbaranCliente(Long Cliente, Pageable pageable);



}
