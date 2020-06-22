package com.bolsadeideas.springboot.app.models.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bolsadeideas.springboot.app.models.entity.Factura;

public interface IFacturaDao extends PagingAndSortingRepository<Factura, Long>{
	
	public static final EntityManager em = null;

	@Query("select p from Factura p where p.enviadoagestor='NO'")
	public Page<Factura> findByvenviadoagestor(Pageable pageable);
	
	@Query("select p from Factura p where p.enviadoagestor='SI'")
	public Page<Factura> findByvenviadoagestorS(Pageable pageable);

	
	@Query("select p from Factura p where p.cliente like %?1% and p.nproveedor like %?2% and  p.vlugar like %?3%")
	public Page<Factura> findByClienteAndProveedorAndLugar(String cliente,String proveedor,String lugar, Pageable pageable);

	@Query("select  COUNT(*) FROM Factura")
	public  long count();
	
	
	

}