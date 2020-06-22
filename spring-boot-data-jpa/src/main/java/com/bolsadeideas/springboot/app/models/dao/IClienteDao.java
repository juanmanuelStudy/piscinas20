package com.bolsadeideas.springboot.app.models.dao;





import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.entity.Factura;

public interface IClienteDao extends PagingAndSortingRepository<Cliente, Long>{

	@Query("select p from Cliente p where p.nombre like %?1%")
	public Page<Cliente> findByNombreListar(String cliente, Pageable pageable);
	
	@Query("select p from Factura p where p.fechavencimiento between ?1 and ?2  and p.cliente.nombre like %?3% and p.nproveedor.vnombre like %?4% and  p.vlugar like %?5%  and p.enviadoagestor='SI'")
	public Page<Factura> findByClienteAndProveedorAndLugarAndEnviadoS(String desde,String hasta,String cliente,String proveedor,String lugar, Pageable pageable);



	
	@Query("select p from Factura p where p.fechavencimiento between ?1 and ?2 and  p.cliente.nombre like %?3% and p.nproveedor.vnombre like %?4% and  p.vlugar like %?5%  "
			+ " and p.enviadoagestor='NO'")
	public Page<Factura> findByClienteAndProveedorAndLugarAndEnviadoN(String desde,String hasta,String cliente,String proveedor,String lugar,Pageable pageable);
	
	@Query ("select p from Factura p where p.npersonal like %?1%")
	public Factura listarFactuaByNumero(String numero);

	
		
	}
