package com.bolsadeideas.springboot.app.models.dto.mapper;

import com.bolsadeideas.springboot.app.models.dto.FacturaDto;
import com.bolsadeideas.springboot.app.models.entity.Factura;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import javax.annotation.Generated;

@Generated(
        value = "org.mapstruct.ap.MappingProcessor",
        comments = "version: 1.3.1.Final, compiler: javac, environment: Java 1.8.0_381 (Oracle Corporation)"
)
@Component
public class FacturaMapperImpl implements FacturaMapper {

    @Autowired
    private FacturaMapper facturaMapper;

    @Override
    public Factura toFactura(FacturaDto facturaDto) {

        return null;
    }
    @Override
    public FacturaDto toFacturaDto(Factura factura) {
        FacturaDto facturaDtos = new FacturaDto();
       // facturaDtos.setDfechaFactura(factura.getDfechaFactura());
        facturaDtos.setCliente(factura.getCliente());
        facturaDtos.setCreateAt(factura.getCreateAt());
        facturaDtos.setNpedido(factura.getNpedido());
        return facturaDtos;
    }
}
