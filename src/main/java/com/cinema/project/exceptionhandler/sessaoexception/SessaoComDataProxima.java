package com.cinema.project.exceptionhandler.sessaoexception;

public class SessaoComDataProxima extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SessaoComDataProxima(String erro) {
		super(String.format(erro));
    }

}
