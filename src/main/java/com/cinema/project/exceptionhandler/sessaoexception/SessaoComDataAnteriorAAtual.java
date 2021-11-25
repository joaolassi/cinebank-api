package com.cinema.project.exceptionhandler.sessaoexception;

public class SessaoComDataAnteriorAAtual extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SessaoComDataAnteriorAAtual() {
        super(String.format("Não pode ser salva uma data anterior à data atual"));
    }
}

