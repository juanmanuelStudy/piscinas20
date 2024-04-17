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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;


@Entity
@Data
@Getter
@Setter
@Table(name = "facturas")
public class Factura implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String npersonal;
  //  private String dfechaFactura;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String vdomiciliacion;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String vtrimenstre;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String enviadoagestor;
   // private String npagada_factura;
   // @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer iva;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String metodoPago;
    private String vlugar;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double ivaTotal;
    @JsonInclude(JsonInclude.Include.NON_NULL)
   // private String foto;

    private String tipoPedido;
    private String npedido ;




    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "NPROVEEDOR")
    private Proveedor nproveedor;

    private String dfechaAlbaran;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String fechavencimiento;

    public Double total;
    private String observacion;
    @Temporal(TemporalType.DATE)
    @Column(name = "create_at")
    private Date createAt;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Cliente cliente;

    @JsonIgnore
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

 //   public String getFechavencimiento() {
  //      return fechavencimiento;
   // }
   // public void setFechavencimiento(String fechavencimiento) {
    //    this.fechavencimiento = fechavencimiento;
  //  }
    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
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
          //  total -= nanticipo;
            resultado = total * 21 / 100 + total;
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

            resultado = total * 21 / 100;
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

    public String getNpersonal() {
        return npersonal;
    }
    public void setNpersonal(String npersonal) {
        this.npersonal = npersonal;
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
   // public String getNpagada_factura() {
   //     return npagada_factura;
   // }
   // public void setNpagada_factura(String npagada_factura) {
    //    this.npagada_factura = npagada_factura;
   // }
    //public String getVlugar() {
   //     return vlugar;
   // }
   // public void setVlugar(String vlugar) {
    //    this.vlugar = vlugar;
   // }
    public Proveedor getNproveedor() {
        return nproveedor;
    }
    public void setNproveedor(Proveedor nproveedor) {
        this.nproveedor = nproveedor;
    }
    //public String getDfechaFactura() {
   //     return dfechaFactura;
  //  }
   // public void setDfechaFactura(String dfechaFactura) {
   //     this.dfechaFactura = dfechaFactura;
  //  }
  //  public String getDfechaAlbaran() {
  //      return dfechaAlbaran;
 //   }
    public void setDfechaAlbaran(String dfechaAlbaran) {
        this.dfechaAlbaran = dfechaAlbaran;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
  //  public String getFoto() {
   //     return foto;
   // }
   // public void setFoto(String foto) {
   //     this.foto = foto;
  //  }
  public String getClientes() {
      return  this.cliente != null ? this.cliente.getNombre():"---";

  }
}