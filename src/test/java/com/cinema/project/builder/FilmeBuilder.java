package com.cinema.project.builder;

import java.time.LocalTime;

import com.cinema.project.model.Filme;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Builder;

@Builder
@JsonDeserialize(builder = FilmeBuilder.class)
public class FilmeBuilder {

	@Builder.Default
	private Long codigo = 255L;
		
	@Builder.Default
	private String titulo = "Harry Potter e a Pedra Filosofal";

	@Builder.Default
	private String descricao = "Um filme baseado na obra de J.K Rowling";
	
	@Builder.Default
	@JsonFormat(shape= JsonFormat.Shape.STRING, pattern= "HH:mm")
	private LocalTime duracao = LocalTime.parse("02:32");
	
	@Builder.Default
	private String imagem = "5bbb9f21-15d8-44d9-b720-dbdb889f3675_montanha.jpg";
		
	public Filme toFilme() {
		return new Filme(codigo, titulo, descricao, duracao, imagem);
	}
}

