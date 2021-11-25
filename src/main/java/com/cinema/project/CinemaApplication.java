package com.cinema.project;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * Essa classe abriga o método principal da aplicação, por ele tudo é iniciado.
 * @author JoaoLassi
 */
@SpringBootApplication
public class CinemaApplication {
	
	private static ApplicationContext APPLICATION_CONTEXT;
	
	@PostConstruct
	void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
	}

	/**
	 * Esse método principal.
	 */
	public static void main(String[] args) {
		APPLICATION_CONTEXT = SpringApplication.run(CinemaApplication.class, args);
	}
	
	/**
	 * Esse método permite que a aplicação pegue qualquer instância da própria aplicação.
	 */
	public static <T> T getBean(Class<T> type) {
		return APPLICATION_CONTEXT.getBean(type);
	}
}
