package com.cinema.project.builder;

import com.cinema.project.model.Sala;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Builder;

@Builder
@JsonDeserialize(builder = SalaBuilder.class)
public class SalaBuilder {

	@Builder.Default
	private Long codigo = 8L;
	
	@Builder.Default
	private String nome = "SALA08";
	
	@Builder.Default
	private int qtdAssentos = 35;
	
	public Sala toSala() {
		return new Sala(codigo, nome, qtdAssentos);		
	}
}


