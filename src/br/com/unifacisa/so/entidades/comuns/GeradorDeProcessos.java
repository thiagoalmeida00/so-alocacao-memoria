package br.com.unifacisa.so.entidades.comuns;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import br.com.unifacisa.so.entidades.algoritmos.FirstFit;
import br.com.unifacisa.so.entidades.comuns.enums.StatusEspacoEnum;

public class GeradorDeProcessos {

	ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	ScheduledExecutorService executorFluxoAlocacao = Executors.newScheduledThreadPool(1);
	ScheduledExecutorService executorFluxoDesalocacao = Executors.newScheduledThreadPool(1);
	Random random = new Random();

	public static List<Processo> listaProcessosGerados = new ArrayList<Processo>();
	private int ultimoId = 0;

	public GeradorDeProcessos() {
		/* Agendar a execução do método gerarProcesso() a cada 2 segundos */
		executor.scheduleAtFixedRate(this::gerarProcesso, 0, 1, TimeUnit.SECONDS);

		/* Agendar a execução do método alocarProcesso() a cada 2 segundos */
		executorFluxoAlocacao.scheduleAtFixedRate(() -> FirstFit.alocarProcesso(), 0, 1, TimeUnit.SECONDS);

		/* Agendar a execução do método desalocarProcesso() a cada 1 segundo */
		executorFluxoDesalocacao.scheduleAtFixedRate(() -> FirstFit.desalocarProcesso(), 0, 2, TimeUnit.SECONDS);
		
		try {
	        Thread.sleep(60000);
	    } catch (InterruptedException e) {
	        e.printStackTrace();
	    }

		/* Encerrar o executor após 1 minuto */
	    executor.shutdown();
	    executorFluxoAlocacao.shutdown();
	    executorFluxoDesalocacao.shutdown();
	}

	public Processo gerarProcesso() {
		int tamanho = (int) (Math.random() * 41) + 10;
		Processo processo = new Processo(ultimoId++, tamanho, StatusEspacoEnum.LIVRE);
		listaProcessosGerados.add(processo);
		FirstFit.totalProcessosGerados++;
		System.out.println("INFO - Processo id: " + ultimoId + " criado.");
		return processo;
	}
}
