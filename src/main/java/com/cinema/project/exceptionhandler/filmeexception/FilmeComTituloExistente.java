package com.cinema.project.exceptionhandler.filmeexception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FilmeComTituloExistente extends Exception{
	private static final long serialVersionUID = 1L;
	
	public FilmeComTituloExistente(String titulo) {
        super(String.format("Um filme com este título já foi registado no sistema.", titulo));
    }
}
