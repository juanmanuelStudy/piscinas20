package com.bolsadeideas.springboot.app.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.ColumnDefault;



@Entity
@Table(name = "facturas")
public class Factura implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String npersonal;
	
	
	private String numeroAlbaran;
	

	private String dfechaFactura;
	
	
	
	private String vdomiciliacion;
	
	
	private String 	vtrimenstre;	
	
	
	private String enviadoagestor;
	
	
	private String npagada_factura;
	
	
	private Double nanticipo;
	
	
	private Integer iva;
	
	
	private String metodoPago;
	
	private String vlugar;
	
	private Double ivaTotal;
	
	
	private String foto;
	
	@ManyToOne
	@JoinColumn(name="NPROVEEDOR")
	private Proveedor nproveedor;
	

	private String dfechaAlbaran;
	
	private String fechavencimiento;
	
	public  Double total;

	private String observacion;

	@Temporal(TemporalType.DATE)
	@Column(name = "create_at")
	private Date createAt;

	@ManyToOne(fetch = FetchType.LAZY)
	private Cliente cliente;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "factura_id")
	private List<ItemFactura> items;

	public Factura() {
		this.items = new ArrayList<ItemFactura>();
	}

	@PrePersist
	public void prePersist() {
		createAt = new Date();
	}
	

	public String getFechavencimiento() {
		return fechavencimiento;
	}

	public void setFechavencimiento(String fechavencimiento) {
		this.fechavencimiento = fechavencimiento;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	
	

	

	public Double getIvaTotal() {
		return ivaTotal;
	}

	public void setIvaTotal(Double ivaTotal) {
		this.ivaTotal = ivaTotal;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Integer getIva() {
		return iva;
	}

	public void setIva(Integer iva) {
		this.iva = iva;
	}

	public String getMetodoPago() {
		return metodoPago;
	}

	public void setMetodoPago(String metodoPago) {
		this.metodoPago = metodoPago;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public List<ItemFactura> getItems() {
		return items;
	}

	public void setItems(List<ItemFactura> items) {
		this.items = items;
	}

	public void addItemFactura(ItemFactura item) {
		this.items.add(item);
	}

public Double getTotal() {
		Double total = 0.0;
		Double resultado = 0.0;
		int size = items.size();

		for (int i = 0; i < size; i++) {
			total += items.get(i).calcularImporte();
			total-=nanticipo;
			resultado = total*21/100+total;
			return resultado;
		}
		
		return null;
	}

public Double getTotalIva() {
	Double total = 0.0;
	Double resultado = 0.0;
	int size = items.size();

	for (int i = 0; i < size; i++) {
		total += items.get(i).calcularImporte();
		
		resultado = total*21/100;
		return resultado;

	}	
	return null;
	
}

public Double getSubTotal() {
	Double total = 0.0;
	int size = items.size();

	for (int i = 0; i < size; i++) {
		total += items.get(i).calcularImporte();
		
		
		return total;

	}	
	return null;
	
}

	private static final long serialVersionUID = 1L;

	public String getNpersonal() {
		return npersonal;
	}

	public void setNpersonal(String npersonal) {
		this.npersonal = npersonal;
	}

	public String getNumeroAlbaran() {
		return numeroAlbaran;
	}

	public void setNumeroAlbaran(String numeroAlbaran) {
		this.numeroAlbaran = numeroAlbaran;
	}

	public String getVdomiciliacion() {
		return vdomiciliacion;
	}

	public void setVdomiciliacion(String vdomiciliacion) {
		this.vdomiciliacion = vdomiciliacion;
	}

	public String getVtrimenstre() {
		return vtrimenstre;
	}

	public void setVtrimenstre(String vtrimenstre) {
		this.vtrimenstre = vtrimenstre;
	}

	

	
	

	

	

	public String getEnviadoagestor() {
		return enviadoagestor;
	}

	public void setEnviadoagestor(String enviadoagestor) {
		this.enviadoagestor = enviadoagestor;
	}

	public String getNpagada_factura() {
		return npagada_factura;
	}

	public void setNpagada_factura(String npagada_factura) {
		this.npagada_factura = npagada_factura;
	}

	public Double getNanticipo() {
		return nanticipo;
	}

	public void setNanticipo(Double nanticipo) {
		this.nanticipo = nanticipo;
	}

	public String getVlugar() {
		return vlugar;
	}

	public void setVlugar(String vlugar) {
		this.vlugar = vlugar;
	}

	public Proveedor getNproveedor() {
		return nproveedor;
	}

	public void setNproveedor(Proveedor nproveedor) {
		this.nproveedor = nproveedor;
	}


	public String getDfechaFactura() {
		return dfechaFactura;
	}

	public void setDfechaFactura(String dfechaFactura) {
		this.dfechaFactura = dfechaFactura;
	}


	public String getDfechaAlbaran() {
		return dfechaAlbaran;
	}

	public void setDfechaAlbaran(String dfechaAlbaran) {
		this.dfechaAlbaran = dfechaAlbaran;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}
	
	
}