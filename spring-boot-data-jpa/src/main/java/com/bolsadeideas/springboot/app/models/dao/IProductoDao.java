package com.bolsadeideas.springboot.app.models.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bolsadeideas.springboot.app.models.entity.Producto;

public interface IProductoDao extends PagingAndSortingRepository<Producto, Long> {

	@Query("select p from Producto p where p.codigo like %?1%")
	public List<Producto> findByCodigo(String term);
	
	@Query("select p from Producto p where p.codigo like %?1%")
	public List<Producto> findByCodigoLikeIgnoreCase(String term);
	
	@Query("select p from Producto p where p.nombre like %?1% and p.nproveedor.vnombre like %?2%" )
	public Page<Producto> findByClienteOrProveedor(String cliente,String proveedor, Pageable pageable);

	@Query("select  COUNT(*) FROM Producto")
	public  long count();
	
	
	@Query("select p from Producto p where p.nproveedor.vnombre= ?1")
	public List<Producto> findMaterialByProveedor(String term);

}