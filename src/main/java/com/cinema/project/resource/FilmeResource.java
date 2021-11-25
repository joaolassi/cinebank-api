package com.cinema.project.resource;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cinema.project.event.RecursoCriadoEvent;
import com.cinema.project.exceptionhandler.CinemaHandlerException.Erro;
import com.cinema.project.exceptionhandler.filmeexception.FilmeComSessaoAtiva;
import com.cinema.project.exceptionhandler.filmeexception.FilmeComTituloExistente;
import com.cinema.project.exceptionhandler.filmeexception.FilmeNaoEncontrado;
import com.cinema.project.model.Filme;
import com.cinema.project.model.Imagem;
import com.cinema.project.repository.FilmeRepository;
import com.cinema.project.service.FilmeService;
import com.cinema.project.storage.S3;

import lombok.AllArgsConstructor;

/**
 * Essa classe contém os controladores e mapeamento de Filmes.
 * 
 * @author JoaoLassi
 *
 */
@RestController
@RequestMapping("/filmes")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class FilmeResource {

	@Autowired
	private FilmeService filmeService;
	
	@Autowired
	private FilmeRepository filmeRepository;

	@Autowired
	private ApplicationEventPublisher publisher;

	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private S3 s3;

	/**
	 * Método que chama a listagem da classe FilmeService.
	 */
	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_FILME') and #oauth2.hasScope('read')")
	public List<Filme> listar() {
		return filmeRepository.findAll();
	}

	/**
	 * Método que chama a buscagem pelo código da classe FilmeService.
	 */
	@GetMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_FILME') and #oauth2.hasScope('read')")
	public ResponseEntity<Filme> buscarPeloCodigo(@PathVariable Long codigo) throws FilmeNaoEncontrado {
		Filme filme = filmeService.buscarFilmePeloCodigo(codigo);

		return filme != null ? ResponseEntity.ok().body(filme) : ResponseEntity.notFound().build();
	}

	/**
	 * Método que chama a buscagem pelo título da classe FilmeService.
	 */
	@GetMapping("/titulo/{titulo}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_FILME') and #oauth2.hasScope('read')")
	public Filme findByName(@PathVariable String titulo) throws FilmeNaoEncontrado {
		return filmeService.buscarFilmePeloTitulo(titulo);
	}

	/**
	 * Método que chama a criação de filme da classe FilmeService.
	 */
	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_FILME') and #oauth2.hasScope('read')")
	public ResponseEntity<Filme> criar(@Valid @RequestBody Filme filme, HttpServletResponse response) throws Exception {
		Filme filmeSalvo = filmeService.salvar(filme);

		publisher.publishEvent(new RecursoCriadoEvent(this, response, filmeSalvo.getCodigo()));
		return filme.getTitulo().isEmpty() ? ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
				: ResponseEntity.status(HttpStatus.CREATED).body(filmeSalvo);
	}

	/**
	 * Método chama a realização de upload da classe S3.
	 */
	@PostMapping("/imagem")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_FILME') and #oauth2.hasScope('read')")
	public Imagem uploadImagem(@RequestParam MultipartFile imagem) throws IOException {
		String nome = s3.salvarTemporariamente(imagem);
		return new Imagem(nome, s3.configurarUrl(nome));
	}

	/**
	 * Método que chama a atualização pelo código da classe FilmeService.
	 * @throws FilmeComImagemNula 
	 */
	@PutMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_FILME') and #oauth2.hasScope('read')")
	public ResponseEntity<Filme> atualizar(@PathVariable Long codigo, @Valid @RequestBody Filme filme)
			throws FilmeNaoEncontrado, FilmeComTituloExistente {

		Filme filmeSalvo = filmeService.atualizar(codigo, filme);
		return ResponseEntity.ok(filmeSalvo);
	}

	/**
	 * Método que chama a deleção de um filme pelo código da classe FilmeService.
	 * 
	 * @throws Exception
	 */
	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('ROLE_REMOVER_FILME') and #oauth2.hasScope('read')")
	public void remover(@PathVariable Long codigo) throws FilmeNaoEncontrado, FilmeComSessaoAtiva {
		filmeService.deletar(codigo);
	}
	
	/**
	 * Método que gerencia a exceção "FilmeNaoEncontrado", já que é a esceção que mais aparece no sistema.
	 * 
	 * @throws Exception
	 */
	@ExceptionHandler({ FilmeNaoEncontrado.class })
	public ResponseEntity<Object> handleFilmeInexistenteOuInativoException(FilmeNaoEncontrado ex) {
		String mensagemUsuario = messageSource.getMessage("filme.inexistente", null, LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		return ((BodyBuilder) ResponseEntity.notFound()).body(erros);
	}

}
