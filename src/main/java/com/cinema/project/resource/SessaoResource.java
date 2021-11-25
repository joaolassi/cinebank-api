package com.cinema.project.resource;

import java.util.List;



import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cinema.project.event.RecursoCriadoEvent;
import com.cinema.project.exceptionhandler.filmeexception.FilmeComTituloExistente;
import com.cinema.project.exceptionhandler.filmeexception.FilmeNaoEncontrado;
import com.cinema.project.exceptionhandler.sessaoexception.SessaoNaoEncontrada;
import com.cinema.project.model.Filme;
import com.cinema.project.model.Sessao;
import com.cinema.project.service.SessaoService;

import lombok.AllArgsConstructor;

/**
 * Essa classe contém os controladores e mapeamento de Sessões.
 * 
 * @author JoaoLassi
 *
 */
@RestController
@RequestMapping("/sessoes")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SessaoResource {
	
	@Autowired
	private SessaoService sessaoService;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	/**
	 * Método que exibe todas as sessões
	 */
	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_SESSAO') and #oauth2.hasScope('read')")
	public List<Sessao> listar() {
		return sessaoService.listarTodos();
	}
	
	/**
	 * Método que busca as sessões pelo código
	 */
	@GetMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_SESSAO') and #oauth2.hasScope('read')")
	public ResponseEntity<Sessao> buscarPeloCodigo(@PathVariable Long codigo) throws SessaoNaoEncontrada {
		Sessao sessao = sessaoService.buscarSessaoPeloCodigo(codigo);

		return sessao != null ? ResponseEntity.ok().body(sessao) : ResponseEntity.notFound().build();
	}
	
	/**
	 * Método que chama a atualização pelo código da classe SessaoService.
	 * @throws SessaoNaoEncontrada 
	 * @throws FilmeComImagemNula 
	 */
	@PutMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_SESSAO') and #oauth2.hasScope('read')")
	public ResponseEntity<Sessao> atualizar(@PathVariable Long codigo, @Valid @RequestBody Sessao sessao)
			throws FilmeNaoEncontrado, FilmeComTituloExistente, SessaoNaoEncontrada {

		Sessao sessaoSalvo = sessaoService.atualizar(codigo, sessao);
		return ResponseEntity.ok(sessaoSalvo);
	}
		
	/**
	 * Método que cria uma nova sessão e salva no banco de dados
	 * @throws exception
	 */
	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_SESSAO') and #oauth2.hasScope('read')")
	public ResponseEntity<Sessao> criar(@Valid @RequestBody Sessao sessao, HttpServletResponse response) throws Exception {
		Sessao sessaoSalva = sessaoService.salvar(sessao);
	
		publisher.publishEvent(new RecursoCriadoEvent(this, response, sessaoSalva.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(sessaoSalva);
	}
	
	/**
	 * Método que remove uma sessão e a remove no banco de dados
	 * @throws exception
	 */
	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('ROLE_REMOVER_SESSAO') and #oauth2.hasScope('read')")
	public void remover(@PathVariable Long codigo) throws Exception {
		sessaoService.deletar(codigo);
	}
	
}
