package br.com.alura.leilao.service;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.alura.leilao.dao.PagamentoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Pagamento;

@Service
public class GeradorDePagamento {

	private PagamentoDao pagamentos;
	
	// É uma lib do Java que passa o relógio do sistema
	private Clock clock;
	
	@Autowired
	public GeradorDePagamento(PagamentoDao pagamentos, Clock clock) {
		this.pagamentos = pagamentos;
		this.clock = clock;
	}

	public void gerarPagamento(Lance lanceVencedor) {
		LocalDate vencimento = LocalDate.now(clock).plusDays(1);
		
		// Na aula de @Captor, esse "Pagamento" que está sendo capturado na linha do ".salvar(pagamento)"
		Pagamento pagamento = new Pagamento(lanceVencedor, proximoDiaUtil(vencimento));
		this.pagamentos.salvar(pagamento);
	}

	/*
	 * Na última aula foi criado este método para caso o dia útil caisse num sábado ou num domingo, porque
	 * teria que cair na verdade numa segunda-feira. Porém nos nossos testes, se o teste for
	 * rodado numa sexta-feira, ele falhará, e é isso que corrigiremos nos testes.
	 */
	private LocalDate proximoDiaUtil(LocalDate dataBase) {
		DayOfWeek diaDaSemana = dataBase.getDayOfWeek();
		if(diaDaSemana == DayOfWeek.SATURDAY) {
			return dataBase.plusDays(2);
		} else if(diaDaSemana == DayOfWeek.SUNDAY) {
			return dataBase.plusDays(1);
		}
		return dataBase;
	}
}
