package br.com.alura.leilao.service;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.com.alura.leilao.dao.PagamentoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Pagamento;
import br.com.alura.leilao.model.Usuario;

class GeradorDePagamentoTest {

	private GeradorDePagamento gerador;

	@Mock
	private PagamentoDao pagamentoDao;
	
	@Captor
	private ArgumentCaptor<Pagamento> captor;
	
	@Mock
	private Clock clock;

	@BeforeEach
	public void beforeEach() {
		MockitoAnnotations.initMocks(this);
		this.gerador = new GeradorDePagamento(pagamentoDao, clock);
	}

	@Test
	void deveriaCriarPagamentoParaVencedorDoLeilao() {
		Leilao leilao = leilao();
		Lance vencedor = leilao.getLanceVencedor();
		
		// aqui está no mesmo dia do teste, mas tem que fazer com valores dinâmicos
		LocalDate data = LocalDate.of(2022, 12, 1);	
		Instant instant = data.atStartOfDay(ZoneId.systemDefault()).toInstant();
		
		Mockito.when(clock.instant()).thenReturn(instant);
		Mockito.when(clock.getZone()).thenReturn(ZoneId.systemDefault());
		gerador.gerarPagamento(vencedor);
		
		/*
		 * Como recuperar um objeto que está sendo criado na classe a ser testada? Nós fazemos
		 * por um conceito do Mockito chamado "Captor" que é usado para capturar determinado 
		 * objeto, e é isso que iremos usar. Ver documento da aula. Veja que anotaremos ele 
		 * com um @Captor nos atributos.
		 */
//		Mockito.verify(pagamentoDao).salvar(pagamento);
		Mockito.verify(pagamentoDao).salvar(captor.capture());
		
		// Esse captor.getValue() pegará o objeto que foi capturado na linha acima para os asserts
		Pagamento pagamento = captor.getValue();
		
		// Então o pagamento precisa ter todas as informações abaixo para estar correto
		/*
		 *  Vamos precisar corrigir a linha abaixo porque, se for sábado, vai dar problema, 
		 *  e o problema está no método estático LocalDate.now(), que é ruim para os testes automatizados
		 */
//		Assert.assertEquals(LocalDate.now().plusDays(1), pagamento.getVencimento());
		Assert.assertEquals(LocalDate.now().plusDays(1), pagamento.getVencimento());
		
		Assert.assertEquals(vencedor.getValor(), pagamento.getValor());
		Assert.assertFalse(pagamento.getPago());
		Assert.assertEquals(vencedor.getUsuario(), pagamento.getUsuario());
		Assert.assertEquals(leilao, pagamento.getLeilao());
	}

	private Leilao leilao() {
		Leilao leilao = new Leilao("Celular", new BigDecimal("500"), new Usuario("Fulano"));
		Lance lance = new Lance(new Usuario("Ciclano"), new BigDecimal("900"));
		leilao.propoe(lance);
		leilao.setLanceVencedor(lance);

		return leilao;
	}

}
