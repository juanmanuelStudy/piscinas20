package com.bolsadeideas.springboot.app.models.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.bolsadeideas.springboot.app.models.entity.Albaran;
import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.entity.Factura;
import com.bolsadeideas.springboot.app.models.entity.Producto;

public interface IClienteService {

	public List<Cliente> findAll();

	public Page<Cliente> findAll(Pageable pageable);

	public void save(Cliente cliente);

	public Cliente findOne(Long id);

	public void delete(Long id);

	public List<Producto> findByNombre(String term);
	
	public void saveFactura(Factura factura);
	
	public Producto findProductoById(Long id);
	
	public Factura findFacturaById(Long id);
	
	public void deleteFactura(Long id);
	

	public void saveAlbaran(Albaran albaran);
	
	public Albaran findAlbaranById(Long id);		
	
	public void deleteAlbaran(Long id);

	public Page<Cliente> findByNombreListar(String cliente, Pageable pageable);

	public Page<Factura> findByvenviadoagestor(Pageable pageRequest);

	public Page<Factura> findByvenviadoagestorS(Pageable pageRequest);
	
	public Page<Factura> findByClienteAndProveedorAndLugarAndEmviadoS(String desde,String hasta,String cliente,String proveedor,String lugar, Pageable pageable);

	public Page<Factura> findByClienteAndProveedorAndLugarAndEnviadoSS(String cliente,String proveedor,String lugar, Pageable pageable);

	
	
	public Page<Factura> findByClienteAndProveedorAndLugarAndEmviadoN(String desde,String hasta,String cliente,String proveedor,String lugar,Pageable pageable);


	public Factura  modificarContbilizar(Factura factura);
	
	public Optional<Factura> findOneBy(Long id);
	
	public Factura listarFactuaByNumero(String numero);
	
	

}
