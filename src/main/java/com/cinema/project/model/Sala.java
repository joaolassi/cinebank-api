package com.cinema.project.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Essa classe contém a modelagem da entidade sala
 * os getters e os setters são feitos automaticamente pela biblioteca "Lombok".
 * @author JoaoLassi
 *
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sala")
public class Sala {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;
	
	private String nome;
		
	private int qtdAssentos;
}
