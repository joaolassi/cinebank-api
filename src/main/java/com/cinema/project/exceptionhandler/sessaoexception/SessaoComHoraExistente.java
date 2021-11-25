package com.cinema.project.exceptionhandler.sessaoexception;

import java.time.LocalTime;

public class SessaoComHoraExistente extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SessaoComHoraExistente(LocalTime hora) {
        super(String.format("Uma sessao com essa hora já está marcada para a sala desejada."));
    }
}
