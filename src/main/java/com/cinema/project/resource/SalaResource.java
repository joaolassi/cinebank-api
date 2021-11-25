package com.cinema.project.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cinema.project.model.Sala;
import com.cinema.project.service.SalaService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/salas")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SalaResource {
	
	@Autowired
	private SalaService salaService;
	
	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_SALA') and #oauth2.hasScope('read')")
	public List<Sala> listar() {
		return salaService.listarTodos();
	}
}
