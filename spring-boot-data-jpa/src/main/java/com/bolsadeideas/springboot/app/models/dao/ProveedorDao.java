package com.bolsadeideas.springboot.app.models.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bolsadeideas.springboot.app.models.entity.Proveedor;



public interface ProveedorDao extends PagingAndSortingRepository<Proveedor, Long> {

@Query("select p from Proveedor p where p.vnombre like %?1%")
public List<Proveedor> findByvNombreLikeIgnoreCase(String string);


@Query("select  COUNT(*) FROM Proveedor")
public long count();

@Query("select p from Proveedor p where p.vnombre like %?1%")
public Page<Proveedor> findByNombreListar(String nombre, Pageable pageRequest);
}