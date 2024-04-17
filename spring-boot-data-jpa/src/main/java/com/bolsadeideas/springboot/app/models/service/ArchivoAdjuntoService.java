package com.bolsadeideas.springboot.app.models.service;

import com.bolsadeideas.springboot.app.models.dao.ArchivoAdjuntoDao;
import com.bolsadeideas.springboot.app.models.entity.ArchivoAdjunto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArchivoAdjuntoService {

    private final ArchivoAdjuntoDao archivoAdjuntoRepository;

    @Autowired
    public ArchivoAdjuntoService(ArchivoAdjuntoDao archivoAdjuntoRepository) {
        this.archivoAdjuntoRepository = archivoAdjuntoRepository;
    }

    public void eliminarArchivoAdjunto(ArchivoAdjunto archivoAdjunto) {
        archivoAdjuntoRepository.delete(archivoAdjunto);
    }

    //metodo guarda
    public void guardar(ArchivoAdjunto archivoAdjunto) {
        archivoAdjuntoRepository.save(archivoAdjunto);
    }

    public List<ArchivoAdjunto> findArchivosAdjuntosByPedidoId(Long pedidoId) {
        return archivoAdjuntoRepository.findArchivoAdjuntoById(pedidoId);
    }



}
