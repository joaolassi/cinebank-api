package com.cinema.project.resource;

import static com.cinema.project.utils.JsonUtils.toJson;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.cinema.project.builder.FilmeBuilder;
import com.cinema.project.exceptionhandler.filmeexception.FilmeNaoEncontrado;
import com.cinema.project.model.Filme;
import com.cinema.project.repository.FilmeRepository;
import com.cinema.project.repository.SessaoRepository;
import com.cinema.project.service.FilmeService;

@ExtendWith(MockitoExtension.class)
public class FilmeResourceTest {

	@Autowired
	private MockMvc mockMvc;

	@InjectMocks
	private FilmeResource filmeResource;

	@Mock
	private FilmeService filmeService;

	@Mock
	private FilmeRepository filmeRepository;

	@Mock
	private SessaoRepository sessaoRepository;
	
	@Mock
	private ApplicationEventPublisher publisher;
	
	@Mock
	private MessageSource messageSource;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(filmeResource)
				.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
				.setViewResolvers((s, locale) -> new MappingJackson2JsonView()).build();
	}

	@Test
	public void deveRetornarSucesso_QuandoAdicionarUmFilme() throws Exception {
		Filme filme = FilmeBuilder.builder().build().toFilme();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");

		when(filmeService.salvar(filme)).thenReturn(filme);

		mockMvc.perform(post("/filmes").contentType(MediaType.APPLICATION_JSON).content(toJson(filme)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.titulo", is(filme.getTitulo())))
				.andExpect(jsonPath("$.descricao", is(filme.getDescricao())))
				.andExpect(jsonPath("$.duracao", is(filme.getDuracao().format(dtf))))
				.andExpect(jsonPath("$.imagem", is(filme.getImagem())));
	}
	
	@Test
	public void deveRetornarBadRequest_QuandoAdicionarUmFilmeSemDescricao() throws Exception {
		Filme filme = FilmeBuilder.builder().build().toFilme();
		filme.setDescricao(null);

		mockMvc.perform(post("/filmes").contentType(MediaType.APPLICATION_JSON).content(toJson(filme)))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void deveRetornarBadRequest_QuandoAdicionarUmFilmeSemDuracao() throws Exception {
		Filme filme = FilmeBuilder.builder().build().toFilme();
		filme.setDuracao(null);

		mockMvc.perform(post("/filmes").contentType(MediaType.APPLICATION_JSON).content(toJson(filme)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void deveRetornarStatusOk_QuandoAtualizarUmFilme() throws Exception {

		Filme filme = FilmeBuilder.builder().build().toFilme();
		filme.setTitulo("O Labirinto de Fauno");
		filme.setDescricao("Um filme dirigido por Guilherme Del Toro");
		filme.setDuracao(LocalTime.parse("12:58"));
		filme.setImagem("5bbb9f21-15d8-44d9-b720-dbdb889f3675_montanha.jpg");

		when(filmeService.atualizar(filme.getCodigo(), filme)).thenReturn(filme);

		mockMvc.perform(
				put("/filmes/" + filme.getCodigo()).contentType(MediaType.APPLICATION_JSON).content(toJson(filme)))
				.andExpect(status().isOk()).andExpect(jsonPath("$.titulo", is(filme.getTitulo())))
				.andExpect(jsonPath("$.descricao", is(filme.getDescricao())))
				.andExpect(jsonPath("$.duracao", is(filme.getDuracao().toString())))
				.andExpect(jsonPath("$.imagem", is(filme.getImagem())));
	}

	@Test
	void deveRetornarBadRequest_QuandoAtualizarUmFilmeComTituloVazio() throws Exception {

		Filme filme = FilmeBuilder.builder().build().toFilme();
		filme.setTitulo("");
		filme.setDescricao("Um filme dirigido por Guilherme Del Toro");
		filme.setDuracao(LocalTime.parse("12:58"));

		mockMvc.perform(
				put("/filmes/" + filme.getCodigo()).contentType(MediaType.APPLICATION_JSON)
				.content(toJson(filme)))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void deveRetornarNoContent_QuandoDeletarUmFilme() throws Exception {
		Filme filmeEsperado = FilmeBuilder.builder().build().toFilme();
		
		doNothing().when(filmeService).deletar(filmeEsperado.getCodigo());
		
		mockMvc.perform(delete("/filmes/" + filmeEsperado.getCodigo())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
	}
	
	@Test
	public void deveRetornarBadRequest_QuandoDeletarUmFilmeInexistente() throws Exception {
		doThrow(FilmeNaoEncontrado.class).when(filmeService).deletar(25552155L);
		
		mockMvc.perform(delete("/filmes/" + 25552155L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
	}

	
}
