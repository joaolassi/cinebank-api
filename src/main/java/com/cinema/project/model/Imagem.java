package com.cinema.project.model;

/**
 * Essa classe é a modelagem das imagens
 * @author JoaoLassi
 *
 */
public class Imagem {
	private String nome;
	
	private String url;

	public Imagem(String nome, String url) {
		super();
		this.nome = nome;
		this.url = url;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
