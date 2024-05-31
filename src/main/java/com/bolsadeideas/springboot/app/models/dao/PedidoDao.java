package com.bolsadeideas.springboot.app.models.dao;


import com.bolsadeideas.springboot.app.models.entity.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;



public interface PedidoDao extends PagingAndSortingRepository<Pedido, Long> {

	//todo añadir los campos de buscqueda para el pedido
	@Query("select p from Pedido p where p.cliente.nombre =?1 and p.estado like %?2% ")
	public Page<Pedido> findByClienteOrEstado(String cliente, String estado, Pageable pageable);

	@Query("select p from Pedido p where p.cliente.nombre =?1 and p.estado = ?2")
	public Iterable<Pedido> findByClienteOrEstadoReport(String cliente, String estado);


	@Query("select p from Pedido p where p.cliente.id = ?1")
	public Page<Pedido> findByClienteId(Long cliente,  Pageable pageable);

	public Pedido findTopByOrderByNpedidoDesc();

	@Query("select  COUNT(*) FROM Pedido ")
	public  long count();

	@Query("select p from Pedido p where p.cliente.id = ?1 and p.estado = 'TERMINADO' and p.facturado=false")
	public Page<Pedido> findPedidosByIdClienteAndTerminado(Long idCliente, Pageable pageable);
}
