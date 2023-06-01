package br.com.unifacisa.so.entidades.algoritmos;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Random;

import br.com.unifacisa.so.entidades.comuns.Memoria;
import br.com.unifacisa.so.entidades.comuns.Processo;
import br.com.unifacisa.so.entidades.comuns.enums.StatusEspacoEnum;
import br.com.unifacisa.so.entidades.gerador.GeradorDeProcessosFF;

public class FirstFit {

	public static int totalProcessosGerados = 0;
	public static int totalProcessosAlocados = 0;
	public static int totalProcessosDescartados = 0;
	public static int somaTotalDeTodosProcessos = 0;
	public static double totalEspacoLivre = 0;

	public static Double tamanhoMedioProcessosFirst;
	public static Double ocupacaoMediaMemoriaFirst;
	public static Double taxaDescarteFirst;

	static final int ESPACO_TOTAL_MEMORIA = 1000;

	public synchronized static void alocarProcesso() {
		System.out.println("INFO: Iniciando alocação.");

		if (GeradorDeProcessosFF.listaProcessosGerados.isEmpty()) {
			System.out.println("ALERTA: Ainda não existem processos gerados.");
			return;
		}

		Processo processo = GeradorDeProcessosFF.listaProcessosGerados.get(0);

		if (!Memoria.processosAlocados.isEmpty() && (Memoria.getTamanho() - processo.getTamanho()) >= 0) {
			int posicao = 0;
			for (Processo espaco : Memoria.processosAlocados) {
				if (espaco.isLivre() && espaco.getTamanho() >= processo.getTamanho()
					&& posicao <= 1000) {
					processo.setStatusProcesso(StatusEspacoEnum.OCUPADO);
					espaco.setTamanho(espaco.getTamanho()-processo.getTamanho());
					Memoria.processosAlocados.add(posicao, processo);
					totalProcessosAlocados++;
					Memoria.tamanho -= processo.getTamanho();
					totalEspacoLivre += Memoria.getTamanho();
					System.out.println("INFO: Processo " + "id: " + processo.getIdProcesso()
							+ " | tamanho: " + processo.getTamanho()
							+ " alocado na memória.");
					break;
				}
				posicao++;
			}
			if(!Memoria.processosAlocados.contains(processo)){
				processo.setStatusProcesso(StatusEspacoEnum.OCUPADO);
				totalProcessosAlocados++;
				Memoria.processosAlocados.add(processo);
				System.out.println("INFO: Processo " + "id: " + processo.getIdProcesso()
						+ " | tamanho: " + processo.getTamanho()
						+ " alocado na memória.");
				Memoria.tamanho -= processo.getTamanho();
				totalEspacoLivre += Memoria.getTamanho();
			}
			GeradorDeProcessosFF.listaProcessosGerados.remove(processo);
		} else if((Memoria.getTamanho() - processo.getTamanho()) <= 0){
			System.out.println("INFO: Processo " + "id: " + processo.getIdProcesso() + " Descartado.");
			totalProcessosDescartados++;
		} else {
			if ((Memoria.getTamanho()) <= 1000) {
				processo.setStatusProcesso(StatusEspacoEnum.OCUPADO);
				Memoria.processosAlocados.add(processo);
				totalProcessosAlocados++;
				System.out.println("INFO: Processo " + "id: " + processo.getIdProcesso() + " alocado na memória.");

				GeradorDeProcessosFF.listaProcessosGerados.remove(processo);
				Memoria.tamanho -= processo.getTamanho();
				totalEspacoLivre += Memoria.getTamanho();
			}
		}
		exibirMemoria();
	}

	public synchronized static void desalocarProcesso() {
		
		if (Memoria.processosAlocados.isEmpty()) {
			System.err.println("ALERTA: Não há processos alocados para remover.");
			return;
		}

		Random random = new Random();
		int index = random.nextInt(Memoria.processosAlocados.size());

		Processo processoRemovido = new Processo(Memoria.processosAlocados.get(index).getIdProcesso(),
				Memoria.processosAlocados.get(index).getTamanho(),
				Memoria.processosAlocados.get(index).getStatusProcesso());

		if (StatusEspacoEnum.OCUPADO.equals(processoRemovido.getStatusProcesso())) {
			System.err.println("INFO: Iniciando desalocação.");
			
			processoRemovido.setStatusProcesso(StatusEspacoEnum.LIVRE);

			Memoria.processosAlocados.set(index, processoRemovido);
			Memoria.tamanho += processoRemovido.getTamanho();
			System.err.println("\nINFO: Processo " + "id: " + processoRemovido.getIdProcesso()
					+ " | tamanho: " + processoRemovido.getTamanho()
					+ " removido | Espaço livre.\n");
			exibirMemoria();
		}
	}

	public synchronized static void exibirMemoria() {

		Processo processoAnterior = null;

		if (!Memoria.processosAlocados.isEmpty()) {
			DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
			symbols.setDecimalSeparator('.');
			DecimalFormat df = new DecimalFormat("#.##", symbols);

			double memoria = Double.parseDouble(df.format(((double)Memoria.getTamanho() / ESPACO_TOTAL_MEMORIA) * 100));
			System.out.println("Espaço Livre da Memória: "+ memoria + "% | ");
			for (Processo processo : Memoria.processosAlocados) {

				String status = processo.isLivre() ? "L" : "O";
				Integer comecaEm = endereco(processo);
				Integer tamanho = processo.getTamanho();

				if (processoAnterior != null) {

					String statusProcessoAnterior = processoAnterior.isLivre() ? "L" : "O";
					Integer comecaEmProcessoAnterior = endereco(processoAnterior);
					Integer tamanhoProcessoAnterior = processoAnterior.getTamanho();

					System.out.print("[" + statusProcessoAnterior + " | " + comecaEmProcessoAnterior + " | "
							+ tamanhoProcessoAnterior + "] ");
					System.out.print("[" + status + " | " + comecaEm + " | " + tamanho + "]");
					processoAnterior = null;
				} else {
					processoAnterior = processo;
				}

			}
			if (processoAnterior != null) {
				String status = processoAnterior.isLivre() ? "L" : "O";
				Integer comecaEm = endereco(processoAnterior);
				Integer tamanho = processoAnterior.getTamanho();
				System.out.print("[" + status + " | " + comecaEm + " | " + tamanho + "] ");
			}
		}
		System.out.println();
	}

	public synchronized static Integer endereco(Processo processo) {

		Integer enderecoAtual = 0;
		Integer enderecoAnterior = 0;

		if (!Memoria.processosAlocados.isEmpty()) {
			if (Memoria.processosAlocados.size() == 1 && Memoria.processosAlocados.contains(processo)) {
				enderecoAtual = 0;
			} else {
				for (Processo registro : Memoria.processosAlocados) {
					if (processo.getIdProcesso().equals(registro.getIdProcesso())) {
						enderecoAtual = enderecoAnterior;
						break;
					}
					enderecoAnterior += registro.getTamanho();
				}
			}
		}

		return enderecoAtual;
	}

	public static void executar() {
		new GeradorDeProcessosFF();

		tamanhoMedioProcessosFirst = (double) somaTotalDeTodosProcessos / totalProcessosGerados;
		ocupacaoMediaMemoriaFirst =  ((double)(totalEspacoLivre / (totalProcessosAlocados * ESPACO_TOTAL_MEMORIA)) * 100);
		taxaDescarteFirst = (double) totalProcessosDescartados / totalProcessosGerados * 100;
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
		symbols.setDecimalSeparator('.');
		DecimalFormat df = new DecimalFormat("#.##", symbols);

		tamanhoMedioProcessosFirst = Double.parseDouble(df.format(tamanhoMedioProcessosFirst));
		ocupacaoMediaMemoriaFirst = Double.parseDouble(df.format(ocupacaoMediaMemoriaFirst));
		taxaDescarteFirst = Double.parseDouble(df.format(taxaDescarteFirst));

		System.out.println("\nResultados do First Fit:");
		System.out.println("Tamanho médio dos processos gerados: " + tamanhoMedioProcessosFirst);
		System.out.println("Ocupação média da memória por segundo: " + ocupacaoMediaMemoriaFirst + "%");
		System.out.println("Taxa de descarte: " + taxaDescarteFirst + "%");
	}

}
