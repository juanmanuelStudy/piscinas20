package com.bolsadeideas.springboot.app.models.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "archivos_adjuntos")
public class ArchivoAdjunto implements Serializable {

    private static final long serialVersionUID = 1456456L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "npedido")
    private Pedido pedido;

}
