package com.bolsadeideas.springboot.app.models.dto;

import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.entity.ItemAlbaran;
import com.bolsadeideas.springboot.app.models.entity.Proveedor;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlbaranDto {

    private Long id;
    private String descripcion;
    private String observacion;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dfecha;
    private String dfechavenci;
    private String lugar;
    private String numeroAlbaran;
    private String foto;
    private List<ItemAlbaran> items;
    private Proveedor proveedor;
    private Cliente cliente;
}
