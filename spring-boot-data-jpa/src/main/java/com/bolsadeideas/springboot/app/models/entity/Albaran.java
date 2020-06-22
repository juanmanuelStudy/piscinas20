package com.bolsadeideas.springboot.app.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;



@Entity
@Table(name = "ALBARAN")
public class Albaran implements Serializable {

	private static final long serialVersionUID = 1456456L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IDENTIFICADOR")
	@SequenceGenerator(sequenceName = "albaran_seq", allocationSize = 1, name = "IDENTIFICADOR")
	private Long id;



	@ManyToOne(fetch = FetchType.LAZY)
	private Cliente cliente;

	@ManyToOne
	@JoinColumn(name = "NPROVEEDOR")
	private Proveedor proveedor;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "albaran_id")
	private List<ItemAlbaran> items;

	public Albaran() {
		this.items = new ArrayList<ItemAlbaran>();
	}
	
	
	private String descripcion;
	
	
	private String observacion;

	
	@Temporal(TemporalType.TIMESTAMP)	
	private Date dfecha;


		
	private String dfechavenci;

	
	private String lugar;

	
	private String numeroAlbaran;
	
	
	private String foto;

	@PrePersist
	public void prePersit() {
		dfecha = new Date();
	}

	public void addItemAlbaran(ItemAlbaran item) {
		this.items.add(item);
	}

	public Double getTotal() {
		Double total = 0.0;

		int size = items.size();

		for (int i = 0; i < size; i++) {
			total += items.get(i).getCantidad();
		}
		return total;
	}
	


	public String getLugar() {
		return lugar;
	}

	

	public String getNumeroAlbaran() {
		return numeroAlbaran;
	}

	public void setNumeroAlbaran(String numeroAlbaran) {
		this.numeroAlbaran = numeroAlbaran;
	}

	public void setLugar(String lugar) {
		this.lugar = lugar;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

	public Date getDfecha() {
		return dfecha;
	}

	public void setDfecha(Date dfecha) {
		this.dfecha = dfecha;
	}

	

	public String getDfechavenci() {
		return dfechavenci;
	}

	public void setDfechavenci(String dfechavenci) {
		this.dfechavenci = dfechavenci;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<ItemAlbaran> getItems() {
		return items;
	}

	public void setItems(List<ItemAlbaran> items) {
		this.items = items;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}
	
	

}
