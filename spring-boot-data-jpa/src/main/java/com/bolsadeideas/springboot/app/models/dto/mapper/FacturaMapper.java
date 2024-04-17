package com.bolsadeideas.springboot.app.models.dto.mapper;

import com.bolsadeideas.springboot.app.models.dto.FacturaDto;
import com.bolsadeideas.springboot.app.models.entity.Factura;
import org.mapstruct.Mapper;
import org.springframework.context.annotation.Bean;


@Mapper(componentModel = "spring")
public interface FacturaMapper {

//crea un objeto de tipo FacturadTO a partir de un objeto de tipo Factura
Factura toFactura(FacturaDto facturaDto);

    FacturaDto toFacturaDto(Factura factura);

}
