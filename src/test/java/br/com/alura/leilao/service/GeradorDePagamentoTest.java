package br.com.alura.leilao.service;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.com.alura.leilao.dao.PagamentoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Usuario;

class GeradorDePagamentoTest {

	private GeradorDePagamento gerador;

	@Mock
	private PagamentoDao pagamentoDao;

	@BeforeEach
	public void beforeEach() {
		MockitoAnnotations.initMocks(this);
		this.gerador = new GeradorDePagamento(pagamentoDao);
	}

	@Test
	void deveriaCriarPagamentoParaVencedorDoLeilao() {
		Leilao leilao = leilao();
		Lance vencedor = leilao.getLanceVencedor();
		gerador.gerarPagamento(vencedor);
		
		Mockito.verify(pagamentoDao).salvar(Mockito.any());
	}

	private Leilao leilao() {
		Leilao leilao = new Leilao("Celular", new BigDecimal("500"), new Usuario("Fulano"));
		Lance lance = new Lance(new Usuario("Ciclano"), new BigDecimal("900"));
		leilao.propoe(lance);
		leilao.setLanceVencedor(lance);

		return leilao;
	}

}
