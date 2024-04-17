package com.bolsadeideas.springboot.app.models.dto.mapper;


import com.bolsadeideas.springboot.app.models.dto.PedidoDtos;
import com.bolsadeideas.springboot.app.models.entity.Pedido;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PedidoMapper {

    //crea un objeto de tipo PedidodTO a partir de un objeto de tipo Pedido
    Pedido toPedido(PedidoDtos pedidoDto);

    PedidoDtos toPedidoDto(Pedido pedido);

}
