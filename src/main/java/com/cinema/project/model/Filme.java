package com.cinema.project.model;

import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.cinema.project.repository.listener.FilmeImagemListener;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Essa classe contém a modelagem da entidade filme
 * os getters e os setters são feitos automaticamente pela biblioteca "Lombok".
 * @author JoaoLassi
 *
 */
@Data
@Entity
@EntityListeners(FilmeImagemListener.class)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "filme")
public class Filme {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;
	
	@NotNull
	@NotEmpty
	private String titulo;

	@NotNull
	@NotEmpty
	private String descricao;

	@NotNull
	@JsonFormat(shape= JsonFormat.Shape.STRING, pattern= "HH:mm")
	private LocalTime duracao;
			
	private String imagem;
	
	@JsonIgnore
	@Transient
	private Sessao sessao;
	
	@Transient
	private String urlImagem;
		
	public Filme(Long codigo, @NotNull @NotEmpty String titulo, @NotNull @NotEmpty String descricao,
			@NotNull @NotEmpty LocalTime duracao, String imagem) {
		super();
		this.codigo = codigo;
		this.titulo = titulo;
		this.descricao = descricao;
		this.duracao = duracao;
		this.imagem = imagem;
	}	
}
