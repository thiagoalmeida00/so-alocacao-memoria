package br.com.unifacisa.so.entidades.algoritmos;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import br.com.unifacisa.so.entidades.comuns.GeradorDeProcessos;
import br.com.unifacisa.so.entidades.comuns.Memoria;
import br.com.unifacisa.so.entidades.comuns.Processo;
import br.com.unifacisa.so.entidades.comuns.enums.StatusEspacoEnum;

public class FirstFit {

	public static int totalProcessosGerados = 0;
	static int totalProcessosAlocados = 0;
	static int totalProcessosDescartados = 0;
	//static int totalEspacoLivre = Memoria.tamanho == null ? 0 : Memoria.tamanho;
	static int totalEspacoLivre = Memoria.getTamanho();

	public static void alocarProcesso() {
		System.out.println("INFO: Iniciando alocação.");

		if (GeradorDeProcessos.listaProcessosGerados.isEmpty()) {
			System.out.println("ALERTA: Ainda não existem processos gerados.");
			return;
		}

		Processo processo = GeradorDeProcessos.listaProcessosGerados.get(0);

		if (!Memoria.processosAlocados.isEmpty() && Memoria.getTamanho() < 1000) {
			int posicao = 0;
			List<Processo> auxList = new ArrayList<>();
			for (Processo espaco : Memoria.processosAlocados) {
				if (espaco.isLivre() && espaco.getTamanho() >= processo.getTamanho()) {
					processo.setStatusProcesso(StatusEspacoEnum.OCUPADO);

					Memoria.processosAlocados.add(posicao, processo);
					totalProcessosAlocados++;
					Memoria.tamanho -= processo.getTamanho();
					System.out.println("INFO: Processo " + "id: " + processo.getIdProcesso() + " alocado na memória.");
					posicao++;
					break;
				} else {
					auxList = Memoria.processosAlocados;
					processo.setStatusProcesso(StatusEspacoEnum.OCUPADO);
					totalProcessosAlocados++;
					auxList.add(processo);
					System.out.println("INFO: Processo " + "id: " + processo.getIdProcesso() + " alocado na memória.");
					Memoria.tamanho -= processo.getTamanho();
					posicao++;
					break;
				}
			}
			if(!auxList.isEmpty()){
				Memoria.processosAlocados = auxList;
			}
			totalProcessosDescartados++;
			GeradorDeProcessos.listaProcessosGerados.remove(processo);
		} else {
			if (Memoria.getTamanho() <= 1000) {
				processo.setStatusProcesso(StatusEspacoEnum.OCUPADO);
				Memoria.processosAlocados.add(processo);
				totalProcessosAlocados++;
				System.out.println("INFO: Processo " + "id: " + processo.getIdProcesso() + " alocado na memória.");

				GeradorDeProcessos.listaProcessosGerados.remove(processo);
				Memoria.tamanho -= processo.getTamanho();
			}
		}
		exibirMemoria();
	}

	public static void desalocarProcesso() {
		System.out.println("INFO: Iniciando desalocação.");

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
		System.out.println("INFO: Processo " + "id: " + processoRemovido.getIdProcesso() + " removido | Espaço livre.");
		exibirMemoria();
	}

	public static void exibirMemoria() {

		Processo processoAnterior = null;

		if (!Memoria.processosAlocados.isEmpty()) {
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

	public static Integer endereco(Processo processo) {

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
		new GeradorDeProcessos();

		Double tamanhoMedioProcessos = (double) totalEspacoLivre / totalProcessosGerados;
		Double ocupacaoMediaMemoria = 100 - ((double) totalEspacoLivre / 1000) * 100;
		Double taxaDescarte = (double) totalProcessosDescartados / totalProcessosGerados * 100;

		DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
		symbols.setDecimalSeparator('.');
		DecimalFormat df = new DecimalFormat("#.##", symbols);

		tamanhoMedioProcessos = Double.parseDouble(df.format(tamanhoMedioProcessos));
		ocupacaoMediaMemoria = Double.parseDouble(df.format(ocupacaoMediaMemoria));
		taxaDescarte = Double.parseDouble(df.format(taxaDescarte));

		System.out.println("\nResultados do First Fit:");
		System.out.println("Tamanho médio dos processos gerados: " + tamanhoMedioProcessos);
		System.out.println("Ocupação média da memória por segundo: " + ocupacaoMediaMemoria + "%");
		System.out.println("Taxa de descarte: " + taxaDescarte + "%");
	}

}
