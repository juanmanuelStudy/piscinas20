package com.bolsadeideas.springboot.app.models.dto.mapper;

import com.bolsadeideas.springboot.app.models.dto.PedidoDtos;
import com.bolsadeideas.springboot.app.models.entity.Pedido;
import org.springframework.stereotype.Component;

import javax.annotation.Generated;

@Generated(
        value = "org.mapstruct.ap.MappingProcessor",
        comments = "version: 1.3.1.Final, compiler: javac, environment: Java 1.8.0_381 (Oracle Corporation)"
)
@Component
public class PedidoMapperImpl implements PedidoMapper{

    @Override
    public Pedido toPedido(PedidoDtos pedidoDto) {
        return null;
    }

    @Override
    public PedidoDtos toPedidoDto(Pedido pedido) {
        PedidoDtos pedidoDtos = new PedidoDtos();
        pedidoDtos.setTipoPedido(pedido.getTipoPedido());
        pedidoDtos.setNpedido(pedido.getNpedido());
        pedidoDtos.setEstado(pedido.getEstado());
        pedidoDtos.setCliente(pedido.getCliente());
        return pedidoDtos;


    }
}
