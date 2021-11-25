package com.cinema.project.repository.listener;

import javax.persistence.PostLoad;

import org.springframework.util.StringUtils;

import com.cinema.project.CinemaApplication;
import com.cinema.project.model.Filme;
import com.cinema.project.storage.S3;

/**
 * Essa classe é um listener da imagem da aplicação.
 * @author JoaoLassi
 */
public class FilmeImagemListener {
	
	/**
	 * Esse método configura a url da imagem.
	 * @param filme é o filme a ser alterado a url
	 */
	@PostLoad
	public void postLoad(Filme filme) {
		if (StringUtils.hasText(filme.getImagem())) {
			S3 s3 = CinemaApplication.getBean(S3.class);
			filme.setUrlImagem(s3.configurarUrl(filme.getImagem()));
		}
	}
}
