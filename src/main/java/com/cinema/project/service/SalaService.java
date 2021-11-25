package com.cinema.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cinema.project.model.Sala;
import com.cinema.project.repository.SalaRepository;

import lombok.AllArgsConstructor;

/**
 * Essa classe contém as regras de negócio da entidade Sala.
 * 
 * @author JoaoLassi
 *
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SalaService {
	
	@Autowired
	private SalaRepository salaRepository;
	
	/*
	 * Método que exibe todas as salas
	 */
	public List<Sala> listarTodos() {
		return salaRepository.findAll();
	}		
}
