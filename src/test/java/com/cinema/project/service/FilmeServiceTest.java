package com.cinema.project.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cinema.project.builder.FilmeBuilder;
import com.cinema.project.exceptionhandler.filmeexception.FilmeNaoEncontrado;
import com.cinema.project.model.Filme;
import com.cinema.project.repository.FilmeRepository;
import com.cinema.project.repository.SessaoRepository;
import com.cinema.project.storage.S3;

@ExtendWith(MockitoExtension.class)
public class FilmeServiceTest {

	@InjectMocks
	private FilmeService filmeService;

	@Mock
	private FilmeRepository filmeRepository;

	@Mock
	private SessaoRepository sessaoRepository;

	@Mock
	private S3 s3;

	@Test
	public void quandoEnviarDadosDoFilmeDeveSerCriadoUmFilme() throws Exception {
		Filme filmeEsperado = FilmeBuilder.builder().build().toFilme();
		Filme filmeSalvo = filmeEsperado;

		when(filmeRepository.save(filmeSalvo)).thenReturn(filmeSalvo);

		Filme filmeCriado = filmeService.salvar(filmeSalvo);

		assertThat(filmeCriado.getCodigo(), is(equalTo(filmeEsperado.getCodigo())));
		assertThat(filmeCriado.getTitulo(), is(equalTo(filmeEsperado.getTitulo())));
		assertThat(filmeCriado.getDescricao(), is(equalTo(filmeEsperado.getDescricao())));
		assertThat(filmeCriado.getDuracao(), is(equalTo(filmeEsperado.getDuracao())));
		assertThat(filmeCriado.getImagem(), is(equalTo(filmeEsperado.getImagem())));
	}

	@Test
	public void deveRetornarNotFound_QuandoBuscarFilmePeloNomeInexistente() {
		Filme filmeEsperado = FilmeBuilder.builder().build().toFilme();

		when(filmeRepository.findByTitulo(filmeEsperado.getTitulo())).thenReturn(Optional.empty());

		assertThrows(FilmeNaoEncontrado.class, () -> filmeService.buscarFilmePeloTitulo(filmeEsperado.getTitulo()));
	}

	@Test
	public void deveRetornarStatusOk_QuandoBuscarFilmes() throws Exception {
		Filme filmeEsperado = FilmeBuilder.builder().build().toFilme();
		Filme filmeEncontradoEsperado = filmeEsperado;

		when(filmeRepository.findAll()).thenReturn(Collections.singletonList(filmeEncontradoEsperado));

		List<Filme> filmesEncontrados = filmeService.listarTodos();

		assertThat(filmesEncontrados, is(not(empty())));
		assertThat(filmesEncontrados.get(0), is(equalTo(filmeEncontradoEsperado)));
	}

	@Test
	public void deveRetornarStatusOk_QuandoBuscarFilmePeloCodigo() throws FilmeNaoEncontrado {
		Filme filmeEsperado = FilmeBuilder.builder().build().toFilme();
		Filme filmeEncontradoEsperado = filmeEsperado;

		when(filmeRepository.findById(filmeEncontradoEsperado.getCodigo()))
				.thenReturn(Optional.of(filmeEncontradoEsperado));

		Filme filmeEncontrado = filmeService.buscarFilmePeloCodigo(filmeEncontradoEsperado.getCodigo());

		assertThat(filmeEncontrado, is(equalTo(filmeEncontradoEsperado)));
	}

	@Test
	public void deveRetornarNotFound_QuandoBuscarFilmePeloCodigoInexistente() {
		Filme filmeEsperado = FilmeBuilder.builder().build().toFilme();

		when(filmeRepository.findById(filmeEsperado.getCodigo())).thenReturn(Optional.empty());

		assertThrows(FilmeNaoEncontrado.class, () -> filmeService.buscarFilmePeloCodigo(filmeEsperado.getCodigo()));
	}

	@Test
	void deveRetornarErro_QuandoDeletarFilmeComCodigoInvalido() {
		Filme filmeEsperado = FilmeBuilder.builder().build().toFilme();
		filmeEsperado.setCodigo(32156487L);

		assertThrows(FilmeNaoEncontrado.class, () -> filmeService.deletar(filmeEsperado.getCodigo()));
	}

}
