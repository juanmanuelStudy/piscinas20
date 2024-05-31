package com.bolsadeideas.springboot.app.models.dto.mapper;

import com.bolsadeideas.springboot.app.models.dto.AlbaranDto;
import com.bolsadeideas.springboot.app.models.entity.Albaran;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Generated;

@Generated(
        value = "org.mapstruct.ap.MappingProcessor",
        comments = "version: 1.3.1.Final, compiler: javac, environment: Java 1.8.0_381 (Oracle Corporation)"
)
@Component
public class AlbaranesMapperImpl implements AlbaranesMapper {

    @Autowired
    private AlbaranesMapper albaranesMapper;


    @Override
    public Albaran toAlbaranes(AlbaranDto albaranesDto) {
        return null;
    }

    @Override
    public AlbaranDto toAlbaranesDto(Albaran albaranes) {

        if ( albaranes == null ) {
            return null;
        }
        AlbaranDto albaranDto = new AlbaranDto();
        albaranDto.setNumeroAlbaran(albaranes.getNumeroAlbaran());
        albaranDto.setCliente(albaranes.getCliente());
        albaranDto.setDfecha(albaranes.getDfecha());
        albaranDto.setLugar(albaranes.getLugar());
        return albaranDto;
    }
}
