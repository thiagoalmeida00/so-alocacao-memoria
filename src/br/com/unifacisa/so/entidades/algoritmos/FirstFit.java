package br.com.unifacisa.so.entidades.algoritmos;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;

import br.com.unifacisa.so.entidades.comuns.GeradorDeProcessos;
import br.com.unifacisa.so.entidades.comuns.Memoria;
import br.com.unifacisa.so.entidades.comuns.Processo;
import br.com.unifacisa.so.entidades.comuns.enums.StatusEspacoEnum;

public class FirstFit {

	public static int totalProcessosGerados = 0;
	static int totalProcessosAlocados = 0;
	static int totalProcessosDescartados = 0;
	static int totalEspacoLivre = Memoria.getTamanho();

	public static void alocarProcesso() {

		if (GeradorDeProcessos.listaProcessosGerados.isEmpty()) {
			System.out.println("ALERTA: Ainda não existem processos gerados.");
			return;
		}

		Processo processo = GeradorDeProcessos.listaProcessosGerados.get(0);

		if (!Memoria.processosAlocados.isEmpty()) {
			int posicao = 0;
			for (Processo espaco : Memoria.processosAlocados) {
				if (espaco.isLivre() && espaco.getTamanho() >= processo.getTamanho()) {
					processo.setStatusProcesso(StatusEspacoEnum.OCUPADO);

					Memoria.processosAlocados.add(posicao, processo);
					break;
				}
				posicao++;
			}
			totalProcessosDescartados++;
		} else {
			Memoria.processosAlocados.add(processo);
		}

		totalProcessosAlocados++;
		Memoria.tamanho -= processo.getTamanho();

		System.out.println("INFO: Processo alocado na memória.");
		exibirMemoria();
	}

	public static void desalocarProcesso() {

		if (Memoria.processosAlocados.isEmpty()) {
			System.out.println("ALERTA: Não há processos alocados para remover.");
			return;
		}

		Random random = new Random();
		int index = random.nextInt(Memoria.processosAlocados.size());

		Processo processoRemovido = new Processo(Memoria.processosAlocados.get(index).getIdProcesso(),
				Memoria.processosAlocados.get(index).getTamanho(),
				Memoria.processosAlocados.get(index).getStatusProcesso());

		processoRemovido.setStatusProcesso(StatusEspacoEnum.LIVRE);

		Memoria.processosAlocados.set(index, processoRemovido);
		Memoria.tamanho += processoRemovido.getTamanho();
		System.out.println("INFO: Processo removido | Espaço livre.");
		exibirMemoria();
	}

	public static void exibirMemoria() {

		if (!Memoria.processosAlocados.isEmpty()) {
			for (Processo processo : Memoria.processosAlocados) {

				String status = processo.isLivre() ? "L" : "O";
				Integer comecaEm = endereco(processo);
				Integer tamanho = processo.getTamanho();

				System.out.println("\nMemoria:");
				System.out.println(status + " | " + comecaEm + " | " + tamanho + " # ");
			}
		} else {
			System.out.println("ALERTA: Nenhum processo alocado na Memória.");
		}
	}

	public static Integer endereco(Processo processo) {

		return 0;
	}

	public static void executar() {
		new GeradorDeProcessos();

		System.out.println("INFO: FirstFit iniciado!");

		Instant startTime = Instant.now();
		Duration duration = Duration.ofMinutes(1);

		while (Duration.between(startTime, Instant.now()).compareTo(duration) < 0) {
		}

		double tamanhoMedioProcessos = (double) totalEspacoLivre / totalProcessosGerados;
		double ocupacaoMediaMemoria = 100 - ((double) totalEspacoLivre / Memoria.getTamanho()) * 100;
		double taxaDescarte = (double) totalProcessosDescartados / totalProcessosGerados * 100;

		System.out.println("\nResultados do First Fit:");
		System.out.println("Tamanho médio dos processos gerados: " + tamanhoMedioProcessos);
		System.out.println("Ocupação média da memória por segundo: " + ocupacaoMediaMemoria + "%");
		System.out.println("Taxa de descarte: " + taxaDescarte + "%");
	}

}
