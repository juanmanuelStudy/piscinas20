package com.bolsadeideas.springboot.app.models.service;


import com.bolsadeideas.springboot.app.models.entity.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface PedidoService {
	
	public Iterable<Pedido> findAll();

	public Page<Pedido> findAll(Pageable pageable);

	public Pedido save(Pedido pedido);

	public Pedido findOne(Long id);

	public void delete(Long id);
	
	public long count();
	
	public Page<Pedido> findByCliente(String cliente,String estado, Pageable pageable);

	public Pedido obtenerUltimoNumeroPedido();

	public List<Pedido> findAllPedidos();

	public Page<Pedido> findPedidoByIdClienteAndFinalizados(Long idCliente, Pageable pageable);

	public Page<Pedido>  findAllByCliente(Long idcliente, Pageable pageable);

	public Iterable<Pedido> findAllByClienteAndEstado(String idcliente,String estado);
	
}
