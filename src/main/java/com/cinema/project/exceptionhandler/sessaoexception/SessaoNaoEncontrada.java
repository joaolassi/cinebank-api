package com.cinema.project.exceptionhandler.sessaoexception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SessaoNaoEncontrada extends Exception{
	private static final long serialVersionUID = 1L;

    public SessaoNaoEncontrada() {
        super(String.format("Uma sessão com este código não foi encontrada no sistema."));
    }
}
