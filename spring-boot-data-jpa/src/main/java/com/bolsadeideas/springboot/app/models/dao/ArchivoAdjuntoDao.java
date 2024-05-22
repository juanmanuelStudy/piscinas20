package com.bolsadeideas.springboot.app.models.dao;

import com.bolsadeideas.springboot.app.models.entity.ArchivoAdjunto;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ArchivoAdjuntoDao extends PagingAndSortingRepository<ArchivoAdjunto, Long> {

    @Query("select a from ArchivoAdjunto a where a.pedido.npedido = ?1")
    public List<ArchivoAdjunto> findArchivoAdjuntoById(Long pedidoId);

    @Query("select a.nombre from ArchivoAdjunto a")
    public List<String> findArchivoAdjunto();

}
