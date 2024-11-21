package com.bolsadeideas.springboot.app.models.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Data
@Table(name = "PEDIDO")
public class Pedido implements Serializable {
    private static final long serialVersionUID = 1456456L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IDENTIFICADOR")
    @SequenceGenerator(sequenceName = "pedido_seq", allocationSize = 1, name = "IDENTIFICADOR")
    private Long npedido;

    @ManyToOne(fetch = FetchType.LAZY)
    private Cliente cliente;

    @Temporal(TemporalType.DATE)
    private Date dfecha;

    private String observacion;

    //se añade el estado del pedido con un valor por defecto de PENDIENTE
    private String estado;

    private String tipoPedido;

    private Boolean facturado = false;//defecto es false

    private Boolean enviadoSms = false;//defecto es false

    //fecha de envio de sms
    @Temporal(TemporalType.DATE)
    private Date fechaEnvioSms;

    //estado de envio del sms (enviado, no enviado)
    private String estadoEnvioSms;

    //añadir una variable que guarde los archivos adjuntos en un arraylist
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "pedido")
    private List<ArchivoAdjunto> archivosAdjuntos;

    @PrePersist
    public void prePersit() {
        dfecha = new Date();
    }

    public Cliente getCliente() {
        return cliente;
    }

    public String getClientes() {
      return  this.cliente != null ? this.cliente.getNombre():"---";

    }

}



