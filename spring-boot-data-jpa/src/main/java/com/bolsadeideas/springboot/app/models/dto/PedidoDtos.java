package com.bolsadeideas.springboot.app.models.dto;

import com.bolsadeideas.springboot.app.models.entity.ArchivoAdjunto;
import com.bolsadeideas.springboot.app.models.entity.Cliente;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDtos {

    private Long npedido;
    private Cliente cliente;
    private Date dfecha;
    private String observacion;
    private String estado;
    private String tipoPedido;
    private List<ArchivoAdjunto> archivosAdjuntos;

}
