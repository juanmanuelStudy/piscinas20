package com.bolsadeideas.springboot.app.models.dto;

import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.entity.ItemFactura;
import com.bolsadeideas.springboot.app.models.entity.Proveedor;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class FacturaDto {

    private Long id;
    private String npersonal;
    private String numeroAlbaran;
    private String dfechaFactura;
    private String vdomiciliacion;
    private String vtrimenstre;
    private String enviadoagestor;
    private String npagada_factura;
    private Integer iva;
    private String metodoPago;
    private String vlugar;
    private Double ivaTotal;
    private String foto;
    private String tipoPedido;
    private String npedido;
    private Proveedor nproveedor;
    private String dfechaAlbaran;
    private String fechavencimiento;
    private Double total;
    private String observacion;
    private Date createAt;
    private Cliente cliente;
    private List<ItemFactura> items;
}
