package com.cinema.project.repository;

import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cinema.project.model.Sala;
import com.cinema.project.model.Sessao;

/**
 * Essa interface contém o gerenciamento da entidade "Sessao" no banco de dados
 * 
 * @author JoaoLassi
 *
 */
@Repository
public interface SessaoRepository extends JpaRepository<Sessao, Long>{
	public List<Sessao> findByHoraInicio(LocalTime hora);
	
	public List<Sessao> findBySalaCodigo(Long codigo);
	
	Sessao getByHoraInicio(LocalTime hora);
	
	Sala getSalaByHoraInicio(LocalTime hora);
	
	Sessao getByHoraInicioAndSalaCodigo(LocalTime horaInicio, Long codigo);
	
	boolean existsByHoraInicioAndSalaCodigo(LocalTime hora, Long codigo);
	boolean existsByHoraFimAndSalaCodigo(LocalTime localDateTime, Long codigo);
	boolean existsByFilmeCodigo(Long codigo);
	
	
	
}
