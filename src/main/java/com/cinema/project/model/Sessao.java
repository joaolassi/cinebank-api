package com.cinema.project.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sessao")
public class Sessao {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;

	@NotNull
	@Column(name = "data")
	private LocalDate data;

	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	@Column(name = "hora_inicio")
	private LocalTime horaInicio;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	@Column(name = "hora_fim")
	private LocalTime horaFim;

	@NotNull
	private BigDecimal valor;

	@NotNull
	@Column(name = "tipo_animacao")
	@Enumerated(EnumType.STRING)
	private TipoAnimacao tipoAnimacao;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_audio")
	private TipoAudio tipoAudio;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "codigo_filme")
	private Filme filme;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "codigo_sala")
	private Sala sala;
	
	

	public Sessao(Long codigo, @NotNull LocalDate data, @NotNull LocalTime horaInicio, @NotNull BigDecimal valor,
			@NotNull TipoAnimacao tipoAnimacao, @NotNull TipoAudio tipoAudio, Filme filme, Sala sala) {
		super();
		this.codigo = codigo;
		this.data = data;
		this.horaInicio = horaInicio;
		this.valor = valor;
		this.tipoAnimacao = tipoAnimacao;
		this.tipoAudio = tipoAudio;
		this.filme = filme;
		this.sala = sala;
	}
	
	
}
