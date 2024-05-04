package com.bolsadeideas.springboot.app.models.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


@Getter
@Setter
@Data
@NoArgsConstructor
@Entity
@Table(name = "notificaciones")
public class Notificacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mensaje;

    private boolean leida;

    @OneToOne
    private Producto materialAfectado;

}
