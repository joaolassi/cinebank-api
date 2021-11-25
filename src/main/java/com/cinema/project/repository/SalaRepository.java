package com.cinema.project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cinema.project.model.Sala;

/**
 * Essa interface contém o gerenciamento da entidade "Sala" no banco de dados
 * 
 * @author JoaoLassi
 *
 */
public interface SalaRepository extends JpaRepository<Sala, Long>{
	
	public Optional<Sala> existsByNome(String nomeSala);
	boolean existsByCodigo(Long codigo);
	
	public Sala getById(Long codigo);
}
