package com.cinema.project.exceptionhandler.filmeexception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FilmeNaoEncontrado extends Exception {
	
	private static final long serialVersionUID = 1L;

	public FilmeNaoEncontrado(String titulo) {
        super(String.format("Um filme com este título não foi encontrado no sistema.", titulo));
    }

    public FilmeNaoEncontrado() {
        super(String.format("Um filme com este código não foi encontrado no sistema."));
    }
    
}
