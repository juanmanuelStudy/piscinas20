package com.bolsadeideas.springboot.app.models.dto.mapper;


import com.bolsadeideas.springboot.app.models.dto.AlbaranDto;
import com.bolsadeideas.springboot.app.models.entity.Albaran;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface  AlbaranesMapper {



    public abstract Albaran toAlbaranes(AlbaranDto albaranesDto);

    public abstract AlbaranDto toAlbaranesDto(Albaran albaranes);
}
