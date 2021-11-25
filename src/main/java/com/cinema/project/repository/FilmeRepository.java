package com.cinema.project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cinema.project.model.Filme;

/**
 * Essa interface contém o gerenciamento da entidade "Filme" no banco de dados
 * 
 * @author JoaoLassi
 *
 */
public interface FilmeRepository extends JpaRepository<Filme, Long> {
	public Optional<Filme> findByTitulo(String titulo);
	
	boolean existsByTituloIgnoreCase(String titulo);
	
	boolean existsById(Long codigo);
}

