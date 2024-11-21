package com.bolsadeideas.springboot.app.models.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.bolsadeideas.springboot.app.models.entity.*;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IClienteService {

    public Cliente findByUsername(String username);

    public List<Cliente> findAll();

    public Page<Cliente> findAll(Pageable pageable);

    public void save(Cliente cliente);

    public Cliente findOne(Long id);

    public void delete(Long id);

	public List<Producto> findByNombre(String term);

	public List	<Producto> findAllProducto();
	
	public void saveFactura(Factura factura);
	
	public Producto findProductoById(Long id);
	
	public Factura findFacturaById(Long id);

	public Page <Factura> findFacturaByIdPage(Long id, Pageable pageRequestFactura);

	public void deleteFactura(Long id);

	public void deletePedido(Long id);

	public void saveAlbaran(Albaran albaran);
	
	public Albaran findAlbaranById(Long id);

	public Pedido findPedidoById(Long id);
	
	public void deleteAlbaran(Long id);

	public Page<Cliente> findByNombreListar(String cliente, Pageable pageable);

	public Page<Factura> findByvenviadoagestor(Pageable pageRequest);

	public Page<Factura> findByvenviadoagestorS(Pageable pageRequest);
	
	public Page<Factura> findByClienteAndProveedorAndLugarAndEmviadoS(String desde,String hasta,String cliente,String proveedor,String lugar, Pageable pageable);

	public Page<Factura> findByClienteAndProveedorAndLugarAndEnviadoSS(String cliente,String proveedor,String lugar, Pageable pageable);

	
	
	public Page<Factura> findByClienteAndProveedorAndLugarAndEmviadoN(String desde,String hasta,String cliente,String proveedor,String lugar,Pageable pageable);

	public Page<Factura> findByClienteAndProveedorAndTipo(String cliente,String tipo,Pageable pageable);

	JasperPrint generateJasperPrints(String cliente, String tipo) throws IOException, JRException;

	public Factura  modificarContbilizar(Factura factura);
	
	public Optional<Factura> findOneBy(Long id);
	
	public Factura listarFactuaByNumero(String numero);
	
	public Page<Factura> findAllByCliente(Long id, Pageable pageable);

	public Page<Factura> findFacturaAll(Pageable pageable);

	//JasperPrint generateJasperPrint(String cliente, String estado)throws IOException, JRException;

	//Busca la ultima factura
	//public Factura obtenerUltimaFactura();





}
