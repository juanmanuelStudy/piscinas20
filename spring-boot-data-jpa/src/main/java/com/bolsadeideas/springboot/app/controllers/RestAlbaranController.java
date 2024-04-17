package com.bolsadeideas.springboot.app.controllers;


import com.bolsadeideas.springboot.app.models.dto.AlbaranDto;
import com.bolsadeideas.springboot.app.models.dto.mapper.AlbaranesMapper;
import com.bolsadeideas.springboot.app.models.dto.mapper.FacturaMapperImpl;
import com.bolsadeideas.springboot.app.models.entity.Albaran;
import com.bolsadeideas.springboot.app.models.service.AlbaranServiceImpl;
import com.bolsadeideas.springboot.app.models.service.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Generated;
import java.util.List;
import java.util.stream.Collectors;

@Generated(
		value = "org.mapstruct.ap.MappingProcessor",
		comments = "version: 1.3.1.Final, compiler: javac, environment: Java 1.8.0_381 (Oracle Corporation)"
)
@RestController
@RequestMapping("/api/albaran/")
public class RestAlbaranController {


	@Autowired
	private FacturaMapperImpl facturaMapper;

	@Autowired
	private IClienteService clienteService;

	@Autowired
	private AlbaranesMapper albaranesMapper;

	@Autowired
	private AlbaranServiceImpl albaranesService;



	@GetMapping("/VerAlbaranFinalizado/{idCliente}")
	public ResponseEntity<List<AlbaranDto>> crearFacturaCliente(@PathVariable(value="idCliente") Long idCliente) {
		// Obtener las facturas de un cliente
		Page<Albaran> albaran = albaranesService.findAlbaranCliente(idCliente, PageRequest.of(0, 5));

		// Convertir las facturas a DTOs
		List<AlbaranDto> albaranDTOs = albaran.getContent().stream()
				.map(albaranesMapper::toAlbaranesDto)
				.collect(Collectors.toList());

		if (albaranDTOs.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		// Enviar en JSON
		return ResponseEntity.ok(albaranDTOs);
	}





}
