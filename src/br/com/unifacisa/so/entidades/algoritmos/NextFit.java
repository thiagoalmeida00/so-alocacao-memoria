package br.com.unifacisa.so.entidades.algoritmos;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Random;

import br.com.unifacisa.so.entidades.comuns.Memoria;
import br.com.unifacisa.so.entidades.comuns.Processo;
import br.com.unifacisa.so.entidades.comuns.enums.StatusEspacoEnum;
import br.com.unifacisa.so.entidades.gerador.GeradorDeProcessosNF;

public class NextFit {

	public static int totalProcessosGerados = 0;
	public static int totalProcessosAlocados = 0;
	public static int totalProcessosDescartados = 0;
	public static int somaTotalDeTodosProcessos = 0;
	public static double totalEspacoLivre = 0;
	public static int posicaoUltimoDesalocado = 0;

	static final int ESPACO_TOTAL_MEMORIA = 1000;

	public synchronized static void alocarProcesso() {
		System.out.println("INFO: Iniciando aloca��o.");

		if (GeradorDeProcessosNF.listaProcessosGerados.isEmpty()) {
			System.out.println("ALERTA: Ainda n�o existem processos gerados.");
			return;
		}

		Processo processo = GeradorDeProcessosNF.listaProcessosGerados.get(0);
		
		if (Memoria.processosAlocados.isEmpty()) { //Primeiro Processo vai alocar aqui

			processo.setStatusProcesso(StatusEspacoEnum.OCUPADO);
			Memoria.processosAlocados.add(processo);
			totalProcessosAlocados++;
			System.out.println("INFO: Processo " + "id: " + processo.getIdProcesso() + " | tamanho: "
					+ processo.getTamanho() + " | posicao: " + (Memoria.processosAlocados.size() - 1) + " | alocado na mem�ria.");

			GeradorDeProcessosNF.listaProcessosGerados.remove(processo);
			Memoria.tamanho -= processo.getTamanho();
			totalEspacoLivre += Memoria.getTamanho();
			
		} else if (!Memoria.processosAlocados.isEmpty() && (Memoria.getTamanho() - processo.getTamanho()) >= 0) {
			int posicao = 0;
			for (Processo espaco : Memoria.processosAlocados) {
				if (espaco.isLivre() && espaco.getTamanho() >= processo.getTamanho() && posicaoUltimoDesalocado == posicao) {
					processo.setStatusProcesso(StatusEspacoEnum.OCUPADO);
					espaco.setTamanho(espaco.getTamanho() - processo.getTamanho());
					Memoria.processosAlocados.add(posicao, processo);
					totalProcessosAlocados++;
					Memoria.tamanho -= processo.getTamanho();
					totalEspacoLivre += Memoria.getTamanho();
					System.out.println("INFO: Processo " + "id: " + processo.getIdProcesso() + " | tamanho: "
							+ processo.getTamanho() + " | posicao: " + posicaoUltimoDesalocado + " | alocado na mem�ria.");
					break;
				}
				posicao++;
			}

			GeradorDeProcessosNF.listaProcessosGerados.remove(processo);

		} else if ((Memoria.getTamanho() - processo.getTamanho()) <= 0) {
			System.out.println("INFO: Processo " + "id: " + processo.getIdProcesso() + " Descartado.");
			totalProcessosDescartados++;
		}
		
		exibirMemoria();
	}

	public synchronized static void desalocarProcesso() {
		System.out.println("INFO: Iniciando desaloca��o.");

		if (Memoria.processosAlocados.isEmpty()) {
			System.out.println("ALERTA: N�o h� processos alocados para remover.");
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
		
		posicaoUltimoDesalocado = index;
		
		System.out.println("INFO: Processo " + "id: " + processoRemovido.getIdProcesso() + " | tamanho: "
				+ processoRemovido.getTamanho() + " | posicao: " + posicaoUltimoDesalocado + " | removido | Espa�o livre.");
		
		System.out.println("INFO: Posi��o desaloca��o: " + posicaoUltimoDesalocado);
		
		exibirMemoria();
	}

	public synchronized static void exibirMemoria() {

		Processo processoAnterior = null;

		if (!Memoria.processosAlocados.isEmpty()) {
			DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
			symbols.setDecimalSeparator('.');
			DecimalFormat df = new DecimalFormat("#.##", symbols);

			double memoria = Double
					.parseDouble(df.format(((double) Memoria.getTamanho() / ESPACO_TOTAL_MEMORIA) * 100));
			System.out.println("Espa�o Livre da Mem�ria: " + memoria + "% | ");
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
		new GeradorDeProcessosNF();

		Double tamanhoMedioProcessos = (double) somaTotalDeTodosProcessos / totalProcessosGerados;
		Double ocupacaoMediaMemoria = ((double) (totalEspacoLivre / (totalProcessosAlocados * ESPACO_TOTAL_MEMORIA)) * 100);
		Double taxaDescarte = (double) totalProcessosDescartados / totalProcessosGerados * 100;
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
		symbols.setDecimalSeparator('.');
		DecimalFormat df = new DecimalFormat("#.##", symbols);

		tamanhoMedioProcessos = Double.parseDouble(df.format(tamanhoMedioProcessos));
		ocupacaoMediaMemoria = Double.parseDouble(df.format(ocupacaoMediaMemoria));
		taxaDescarte = Double.parseDouble(df.format(taxaDescarte));

		System.out.println("\nResultados do First Fit:");
		System.out.println("Tamanho m�dio dos processos gerados: " + tamanhoMedioProcessos);
		System.out.println("Ocupa��o m�dia da mem�ria por segundo: " + ocupacaoMediaMemoria + "%");
		System.out.println("Taxa de descarte: " + taxaDescarte + "%");
	}

}
