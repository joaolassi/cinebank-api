package com.cinema.project.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cinema.project.exceptionhandler.InvalidRequestException;
import com.cinema.project.exceptionhandler.filmeexception.FilmeComSessaoAtiva;
import com.cinema.project.exceptionhandler.filmeexception.FilmeComTituloExistente;
import com.cinema.project.exceptionhandler.filmeexception.FilmeNaoEncontrado;
import com.cinema.project.model.Filme;
import com.cinema.project.repository.FilmeRepository;
import com.cinema.project.repository.SessaoRepository;
import com.cinema.project.storage.S3;

import lombok.AllArgsConstructor;

/**
 * Essa classe contém as regras de negócio da entidade Filme.
 * 
 * @author JoaoLassi
 *
 */

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class FilmeService {

	@Autowired
	private FilmeRepository filmeRepository;
	
	@Autowired
	private SessaoRepository sessaoRepository;

	@Autowired
	private S3 s3;

	/**
	 * Método para salvar o filme no banco de dados.
	 * 
	 * @throws Exceção no caso de um filme com título existente.
	 * @param filme é o filme a ser salvo
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Filme salvar(@Valid Filme filme) throws FilmeComTituloExistente {
		verificarSeTemTituloIgual(filme.getTitulo());

		if (StringUtils.hasText(filme.getImagem())) {
			s3.salvar(filme.getImagem());
		} // caso tenha imagem ela é salva junto com o filme

		return filmeRepository.save(filme);
	}

	/**
	 * Método para listar todos os filmes.
	 *
	 */

	public List<Filme> listarTodos() {
		return filmeRepository.findAll();
	}

	/**
	 * Método para buscar o filme pelo código.
	 * 
	 * @throws Exceção no caso de um filme com código não encontrado.
	 * @param codigo é o código do filme
	 */

	public Filme buscarFilmePeloCodigo(Long codigo) throws FilmeNaoEncontrado {
		Filme filmeSalvo = filmeRepository.findById(codigo)
				.orElseThrow(() -> new FilmeNaoEncontrado());
		return filmeSalvo;
	}
	
	/**
	 * Método para buscar filme pelo título
	 * 
	 * @throws Exceção no caso de um filme não encontrado.
	 * @param titulo é o título do filme
	 */

	public Filme buscarFilmePeloTitulo(String titulo) throws FilmeNaoEncontrado {
		Filme filmeEncontrado = filmeRepository.findByTitulo(titulo)
				.orElseThrow(() -> new FilmeNaoEncontrado("Filme não encontrado"));
		return filmeEncontrado;
	}

	/**
	 * Método para atualizar os dados do filme.
	 * @throws FilmeComImagemNula 
	 * 
	 * @throws Exceção no caso de filme não encontrado.
	 * @throws Exceção no caso de um filme com título existente.
	 */

	public Filme atualizar(Long codigo, Filme filme) throws FilmeNaoEncontrado, FilmeComTituloExistente {

		Filme filmeSalvo = buscarFilmePeloCodigo(codigo);

		if (filmeSalvo == null || filmeSalvo.getCodigo() == null) {
			throw new InvalidRequestException("O filme não pode ser nulo");
		}

		Optional<Filme> optionalFilme = filmeRepository.findById(filmeSalvo.getCodigo());

		if (optionalFilme.isEmpty()) {
			throw new FilmeNaoEncontrado("Filme com o id: " + filmeSalvo.getCodigo() + "não existe.");
		}
		
		if (StringUtils.hasText(filme.getImagem())) {

			s3.remover(filmeSalvo.getImagem());

		} else if (StringUtils.hasText(filme.getImagem()) && !filme.getImagem().equals(filme.getImagem())) {

			s3.substituir(filme.getImagem(), filme.getImagem());
		}
		
		if(!filme.getTitulo().equals(filme.getTitulo())) {
		verificarSeTemTituloIgual(filme.getTitulo());
		}
		BeanUtils.copyProperties(filme, filmeSalvo, "codigo");

		return filmeRepository.save(filmeSalvo);
	}

	/**
	 * Método para salvar o filme no banco de dados.
	 * @throws FilmeComSessaoAtiva 
	 * @throws FilmeNaoEncontrado 
	 * 
	 * @throws Exception
	 * 
	 * @throws Exceção   no caso de um filme com título existente.
	 *
	 */
	public void deletar(Long codigo) throws FilmeComSessaoAtiva, FilmeNaoEncontrado{
		
		boolean filmeComSessao = sessaoRepository.existsByFilmeCodigo(codigo);
		
		boolean filmeInexistente = filmeRepository.existsById(codigo);
		
		if(!filmeInexistente) {
			throw new FilmeNaoEncontrado();
		}
		if(filmeComSessao) {
			throw new FilmeComSessaoAtiva();
		}
		
		filmeRepository.deleteById(codigo);
	}

	/**
	 * Método para verificar se há um filme com o título existente.
	 * 
	 * @throws Exceção no caso de filme não encontrado.
	 */

	private void verificarSeTemTituloIgual(String titulo) throws FilmeComTituloExistente {
		boolean tituloExistente = filmeRepository.existsByTituloIgnoreCase(titulo);
		if (tituloExistente) {
			throw new FilmeComTituloExistente(titulo);
		}
	}

}
