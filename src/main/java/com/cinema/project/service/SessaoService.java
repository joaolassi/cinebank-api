package com.cinema.project.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cinema.project.exceptionhandler.InvalidRequestException;
import com.cinema.project.exceptionhandler.filmeexception.FilmeComTituloExistente;
import com.cinema.project.exceptionhandler.filmeexception.FilmeNaoEncontrado;
import com.cinema.project.exceptionhandler.sessaoexception.SessaoComDataAnteriorAAtual;
import com.cinema.project.exceptionhandler.sessaoexception.SessaoComDataProxima;
import com.cinema.project.exceptionhandler.sessaoexception.SessaoComHoraExistente;
import com.cinema.project.exceptionhandler.sessaoexception.SessaoNaoEncontrada;
import com.cinema.project.model.Filme;
import com.cinema.project.model.Sessao;
import com.cinema.project.repository.FilmeRepository;
import com.cinema.project.repository.SessaoRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SessaoService {

	@Autowired
	private SessaoRepository sessaoRepository;

	@Autowired
	private FilmeRepository filmeRepository;

	@Transactional
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Sessao salvar(@Valid Sessao sessao)
			throws SessaoComHoraExistente, SessaoNaoEncontrada, SessaoComDataAnteriorAAtual {
		Filme filme = filmeRepository.findById(sessao.getFilme().getCodigo()).orElse(null);

		horaFinal(sessao, filme);
		validarDatasSalvar(sessao);

		boolean horaInicioNaSala = sessaoRepository.existsByHoraInicioAndSalaCodigo(sessao.getHoraInicio(),
				sessao.getSala().getCodigo());
		boolean horaFimNaSala = sessaoRepository.existsByHoraFimAndSalaCodigo(sessao.getHoraInicio(),
				sessao.getSala().getCodigo());		

		System.out.println();
		System.out.println();
		System.out.println();

		if (horaInicioNaSala || horaFimNaSala) {
			throw new SessaoComHoraExistente(sessao.getHoraInicio());
		}
		if (sessao.getHoraInicio().equals(null)) {
			System.out.println();
			System.out.println("Feijoada");
			System.out.println();
		}


		return sessaoRepository.save(sessao);
	}

	public List<Sessao> listarTodos() {
		return sessaoRepository.findAll();
	}

	public Sessao buscarSessaoPeloCodigo(Long codigo) throws SessaoNaoEncontrada {
		Sessao sessaoSalva = sessaoRepository.findById(codigo)
				.orElseThrow(() -> new SessaoNaoEncontrada());
		return sessaoSalva;
	}

	public void deletar(Long codigo) throws SessaoComDataProxima, SessaoNaoEncontrada {
		Sessao sessao = buscarSessaoPeloCodigo(codigo);

		validarDatasDeletar(sessao);

		sessaoRepository.deleteById(codigo);
	}
	
	public Sessao atualizar(Long codigo, Sessao sessao) throws FilmeNaoEncontrado, FilmeComTituloExistente, SessaoNaoEncontrada {

		Sessao sessaoSalvo = buscarSessaoPeloCodigo(codigo);

		if (sessaoSalvo == null || sessaoSalvo.getCodigo() == null) {
			throw new InvalidRequestException("A Sesão não pode ser nula");
		}

		Optional<Sessao> optionalSessao = sessaoRepository.findById(sessaoSalvo.getCodigo());

		if (optionalSessao.isEmpty()) {
			throw new FilmeNaoEncontrado("Sessao com o id: " + sessaoSalvo.getCodigo() + "não existe.");
		}
		
		
		BeanUtils.copyProperties(sessao, sessaoSalvo, "codigo");

		return sessaoRepository.save(sessaoSalvo);
	}

	private void horaFinal(Sessao sessao, Filme filme) throws SessaoNaoEncontrada {
		LocalTime sessaoFinal = sessao.getHoraInicio().plusHours(filme.getDuracao().getHour())
				.plusMinutes(filme.getDuracao().getMinute());
		LocalTime sessaoHoraInicio = sessao.getHoraInicio();

		sessao.setHoraFim(sessaoFinal);
	}

	private void validarDatasDeletar(Sessao sessao) throws SessaoComDataProxima {
		int dataSessao = sessao.getData().getDayOfMonth();
		int anoSessao = sessao.getData().getYear();
		int mesSessao = sessao.getData().getMonthValue();
		
		int dataAtual = LocalDate.now().getDayOfMonth();
		int anoAtual = LocalDate.now().getYear();
		int mesAtual = LocalDate.now().getMonthValue();

		
		System.out.println();
		System.out.println(dataSessao);
		System.out.println(dataAtual);
		System.out.println();

		if ((dataSessao - dataAtual < 10) && mesSessao < mesAtual && anoSessao < anoAtual) {
			throw new SessaoComDataProxima("Não é possível apagar uma sessão com menos de 10 dias");
		}
	}

	private void validarDatasSalvar(Sessao sessao) throws SessaoComDataAnteriorAAtual {
		int dataSessao = sessao.getData().getDayOfMonth();
		int anoSessao = sessao.getData().getYear();
		int mesSessao = sessao.getData().getMonthValue();
		
		int dataAtual = LocalDate.now().getDayOfMonth();
		int anoAtual = LocalDate.now().getYear();
		int mesAtual = LocalDate.now().getMonthValue();
		

		if ((dataSessao - dataAtual < 10) && mesSessao < mesAtual && anoSessao < anoAtual) {
			throw new SessaoComDataAnteriorAAtual();
		}
	}

}
