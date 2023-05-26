package br.com.unifacisa.so.entidades.gerador;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import br.com.unifacisa.so.entidades.algoritmos.WorstFit;
import br.com.unifacisa.so.entidades.comuns.Processo;
import br.com.unifacisa.so.entidades.comuns.enums.StatusEspacoEnum;

public class GeradorDeProcessosWF {

	ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	ScheduledExecutorService executorFluxoAlocacao = Executors.newScheduledThreadPool(1);
	ScheduledExecutorService executorFluxoDesalocacao = Executors.newScheduledThreadPool(1);
	Random random = new Random();

	public static List<Processo> listaProcessosGerados = new ArrayList<Processo>();
	private int ultimoId = 0;

	public GeradorDeProcessosWF() {
		System.out.println("INFO: FirstFit iniciado!");
		
		limparDados();

		/* Agendar a execução do método gerarProcesso() a cada meio segundo */
		executor.scheduleAtFixedRate(this::gerarProcesso, 0, 500, TimeUnit.MILLISECONDS);

		try {
	        Thread.sleep(1000);
	    } catch (InterruptedException e) {
	        e.printStackTrace();
	    }
		/* Agendar a execução do método alocarProcesso() a cada 1 segundos */
		executorFluxoAlocacao.scheduleAtFixedRate(() -> WorstFit.alocarProcesso(), 0, 1, TimeUnit.SECONDS);

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		/* Agendar a execução do método desalocarProcesso() a cada 1 segundos */
		executorFluxoDesalocacao.scheduleAtFixedRate(() -> WorstFit.desalocarProcesso(), 0, 1, TimeUnit.SECONDS);

		try {
			Thread.sleep(50000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		/* Encerrar os executores após 1 minuto */
		executor.shutdown();
		executorFluxoAlocacao.shutdown();
		executorFluxoDesalocacao.shutdown();
	}

	public Processo gerarProcesso() {
		int tamanho = (int) (Math.random() * 41) + 10;
		Processo processo = new Processo(ultimoId++, tamanho, StatusEspacoEnum.LIVRE);
		listaProcessosGerados.add(processo);
		WorstFit.totalProcessosGerados++;
		WorstFit.somaTotalDeTodosProcessos += processo.getTamanho();
		System.out.println("INFO - Processo id: " + (ultimoId-1) + " criado | " + "tamanho: " + tamanho);
		return processo;
	}
	
	public void limparDados() {
		WorstFit.totalProcessosGerados = 0;
		WorstFit.totalProcessosAlocados = 0;
		WorstFit.totalProcessosDescartados = 0;
		WorstFit.somaTotalDeTodosProcessos = 0;
		WorstFit.totalEspacoLivre = 0;
	}
}
