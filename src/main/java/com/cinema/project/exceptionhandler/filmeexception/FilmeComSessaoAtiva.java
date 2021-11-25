package com.cinema.project.exceptionhandler.filmeexception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FilmeComSessaoAtiva extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	public FilmeComSessaoAtiva() {
        super(String.format("Não é possível apagar um filme que está vinculado a uma sessão."));
    }
}
