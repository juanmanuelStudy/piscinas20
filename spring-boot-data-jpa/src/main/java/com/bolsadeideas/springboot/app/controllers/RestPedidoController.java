package com.bolsadeideas.springboot.app.controllers;


import com.bolsadeideas.springboot.app.models.dto.PedidoDtos;
import com.bolsadeideas.springboot.app.models.dto.mapper.PedidoMapper;
import com.bolsadeideas.springboot.app.models.entity.Pedido;
import com.bolsadeideas.springboot.app.models.service.PedidoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Generated;
import java.util.List;
import java.util.stream.Collectors;

@Generated(
        value = "org.mapstruct.ap.MappingProcessor",
        comments = "version: 1.3.1.Final, compiler: javac, environment: Java 1.8.0_381 (Oracle Corporation)"
)
@RestController
@RequestMapping("/api/pedido/")
public class RestPedidoController {

    @Autowired
    private PedidoServiceImpl pedidoService;

    @Autowired
    private PedidoMapper pedidoMapper;

    @GetMapping("/listarPedidos/{clienteId}")
    public ResponseEntity<List<PedidoDtos>>listar(@PathVariable(value = "clienteId") Long clienteId) {

        // Obtener los albaranes  de un cliente
        Page<Pedido> pedido =  pedidoService.findPedidoByIdClienteAndFinalizados(clienteId, PageRequest.of(0, 100));

        // Convertir las facturas a DTOs
        List<PedidoDtos> albaranDTOs = pedido.getContent().stream()
                .map(pedidoMapper::toPedidoDto)
                .collect(Collectors.toList());

        if (albaranDTOs.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Enviar en JSON
        return ResponseEntity.ok(albaranDTOs);
    }
}
