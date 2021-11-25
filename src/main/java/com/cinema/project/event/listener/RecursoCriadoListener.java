package com.cinema.project.event.listener;

import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.cinema.project.event.RecursoCriadoEvent;

@Component
public class RecursoCriadoListener implements ApplicationListener<RecursoCriadoEvent>{

	// para não repetir código
	@Override
	public void onApplicationEvent(RecursoCriadoEvent recursoCriadoEvent) {
		HttpServletResponse response = recursoCriadoEvent.getResponse();
		Long codigo = recursoCriadoEvent.getCodigo();
		
		adicionarHeaderLocation(response, codigo);
	}

	private void adicionarHeaderLocation(HttpServletResponse response, Long codigo) {
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("{/codigo}")
				.buildAndExpand(codigo).toUri();
		response.setHeader("Location", uri.toASCIIString());
	}
	
}

