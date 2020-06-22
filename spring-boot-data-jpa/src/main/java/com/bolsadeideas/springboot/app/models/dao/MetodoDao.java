package com.bolsadeideas.springboot.app.models.dao;



import org.springframework.data.repository.PagingAndSortingRepository;

import com.bolsadeideas.springboot.app.models.entity.MetodoPago;

public interface  MetodoDao  extends PagingAndSortingRepository<MetodoPago, Long> {

}
