package br.com.unifacisa.so.entidades.gerador;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import br.com.unifacisa.so.entidades.algoritmos.FirstFit;
import br.com.unifacisa.so.entidades.comuns.Memoria;
import br.com.unifacisa.so.entidades.comuns.Processo;
import br.com.unifacisa.so.entidades.comuns.enums.StatusEspacoEnum;

public class GeradorDeProcessosFF {

	ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	ScheduledExecutorService executorFluxoAlocacao = Executors.newScheduledThreadPool(1);
	ScheduledExecutorService executorFluxoDesalocacao = Executors.newScheduledThreadPool(1);
	Random random = new Random();

	public static List<Processo> listaProcessosGerados = new ArrayList<Processo>();
	private int ultimoId = 0;

	public GeradorDeProcessosFF() {
		System.out.println("INFO: FirstFit iniciado!");
		
		limparDados();

		/* Agendar a execu��o do m�todo gerarProcesso() a cada meio segundo */
		executor.scheduleAtFixedRate(this::gerarProcesso, 0, 500, TimeUnit.MILLISECONDS);

		//ESPERA 1 SEGUNDO PARA COMECAR A ALOCAR PROCESSOS
		try {
	        Thread.sleep(1000);
	    } catch (InterruptedException e) {
	        e.printStackTrace();
	    }
		/* Agendar a execu��o do m�todo alocarProcesso() a cada 1 segundos */
		executorFluxoAlocacao.scheduleAtFixedRate(() -> FirstFit.alocarProcesso(), 0, 1, TimeUnit.SECONDS);

		//ESPERA 5.5 SEGUNDOS PARA COMECAR A DESALOCAR PROCESSOS
		try {
			Thread.sleep(5500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		/* Agendar a execu��o do m�todo desalocarProcesso() a cada 1.5 segundos */
		executorFluxoDesalocacao.scheduleAtFixedRate(() -> FirstFit.desalocarProcesso(), 0, 1500, TimeUnit.MILLISECONDS);

		try {
			Thread.sleep(50000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		/* Encerrar os executores ap�s 50 segundos */
		executor.shutdown();
		executorFluxoAlocacao.shutdown();
		executorFluxoDesalocacao.shutdown();
	}

	public Processo gerarProcesso() {
		int tamanho = (int) (Math.random() * 41) + 10;
		Processo processo = new Processo(ultimoId++, tamanho, StatusEspacoEnum.LIVRE);
		listaProcessosGerados.add(processo);
		FirstFit.totalProcessosGerados++;
		FirstFit.somaTotalDeTodosProcessos += processo.getTamanho();
		System.out.println("INFO - Processo id: " + (ultimoId-1) + " criado | " + "tamanho: " + tamanho);
		return processo;
	}
	
	public void limparDados() {
		Memoria.tamanho = 1000;
		Memoria.processosAlocados.clear();
		FirstFit.totalProcessosGerados = 0;
		FirstFit.totalProcessosAlocados = 0;
		FirstFit.totalProcessosDescartados = 0;
		FirstFit.somaTotalDeTodosProcessos = 0;
		FirstFit.totalEspacoLivre = 0;
	}
}
