package br.com.unifacisa.so.entidades.algoritmos;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Random;

import br.com.unifacisa.so.entidades.comuns.Memoria;
import br.com.unifacisa.so.entidades.comuns.Processo;
import br.com.unifacisa.so.entidades.comuns.enums.StatusEspacoEnum;
import br.com.unifacisa.so.entidades.gerador.GeradorDeProcessosWF;

public class WorstFit {

	public static int totalProcessosGerados = 0;
	public static int totalProcessosAlocados = 0;
	public static int totalProcessosDescartados = 0;
	public static int somaTotalDeTodosProcessos = 0;
	public static double totalEspacoLivre = 0;

	static final int ESPACO_TOTAL_MEMORIA = 1000;

	public static Double tamanhoMedioProcessosWorst;
	public static Double ocupacaoMediaMemoriaWorst;
	public static Double taxaDescarteWorst;

	public synchronized static void alocarProcesso() {
		System.out.println("INFO: Iniciando aloca��o.");

		if (GeradorDeProcessosWF.listaProcessosGerados.isEmpty()) {
			System.out.println("ALERTA: Ainda n�o existem processos gerados.");
			return;
		}

		Processo processo = GeradorDeProcessosWF.listaProcessosGerados.get(0);

		if (!Memoria.processosAlocados.isEmpty() && (Memoria.getTamanho() - processo.getTamanho()) >= 0) {

			int piorPosicao = 0;
			int resultMelhorAtual = 0;
			Processo melhorProcesso = Memoria.processosAlocados.get(piorPosicao);
			int tamProcessoMelhor = melhorProcesso.getTamanho();
			for (int i = 0; i < Memoria.processosAlocados.size(); i++) {
				Processo processoAtual = Memoria.processosAlocados.get(i);
				int tamProcesso = processo.getTamanho();
				int tamProcessoAtual = processoAtual.getTamanho();
				int result = 0;
				if(processoAtual.isLivre()&& tamProcessoAtual >= processo.getTamanho()){
					result = tamProcessoAtual - tamProcesso;
				}
				if(tamProcessoAtual == 50 && processoAtual.isLivre()){
					piorPosicao = i;
					break;
				}
				if(result >= resultMelhorAtual
						&& processoAtual.isLivre()
						&& tamProcessoAtual >= processo.getTamanho()){

					piorPosicao = i;
					melhorProcesso = Memoria.processosAlocados.get(piorPosicao);
					tamProcessoMelhor = processoAtual.getTamanho();
					resultMelhorAtual = result;
				}
			}

			if(resultMelhorAtual != 0){
				Processo espaco = Memoria.processosAlocados.get(piorPosicao);
				processo.setStatusProcesso(StatusEspacoEnum.OCUPADO);
				if(espaco.isLivre()) {
					espaco.setTamanho(espaco.getTamanho() - processo.getTamanho());
				}
				Memoria.processosAlocados.add(piorPosicao, processo);
				totalProcessosAlocados++;
				Memoria.tamanho -= processo.getTamanho();
				totalEspacoLivre += Memoria.getTamanho();
				System.out.println("INFO: Processo " + "id: " + processo.getIdProcesso()
						+ " | tamanho: " + processo.getTamanho()
						+ " alocado na mem�ria.");
			}

			if(!Memoria.processosAlocados.contains(processo)){
				processo.setStatusProcesso(StatusEspacoEnum.OCUPADO);
				totalProcessosAlocados++;
				Memoria.processosAlocados.add(processo);
				System.out.println("INFO: Processo " + "id: " + processo.getIdProcesso()
						+ " | tamanho: " + processo.getTamanho()
						+ " alocado na mem�ria.");
				Memoria.tamanho -= processo.getTamanho();
				totalEspacoLivre += Memoria.getTamanho();
			}
			GeradorDeProcessosWF.listaProcessosGerados.remove(processo);
		} else if((Memoria.getTamanho() - processo.getTamanho()) <= 0){
			System.out.println("INFO: Processo " + "id: " + processo.getIdProcesso() + " Descartado.");
			totalProcessosDescartados++;
		} else {
			if ((Memoria.getTamanho()) <= 1000) {
				processo.setStatusProcesso(StatusEspacoEnum.OCUPADO);
				Memoria.processosAlocados.add(processo);
				totalProcessosAlocados++;
				System.out.println("INFO: Processo " + "id: " + processo.getIdProcesso() + " alocado na mem�ria.");

				GeradorDeProcessosWF.listaProcessosGerados.remove(processo);
				Memoria.tamanho -= processo.getTamanho();
				totalEspacoLivre += Memoria.getTamanho();
			}
		}
		exibirMemoria();
	}

	public synchronized static void desalocarProcesso() {

		if (Memoria.processosAlocados.isEmpty()) {
			System.err.println("ALERTA: N�o h� processos alocados para remover.");
			return;
		}

		Random random = new Random();
		int index = random.nextInt(Memoria.processosAlocados.size());

		Processo processoRemovido = new Processo(Memoria.processosAlocados.get(index).getIdProcesso(),
				Memoria.processosAlocados.get(index).getTamanho(),
				Memoria.processosAlocados.get(index).getStatusProcesso());

		if (StatusEspacoEnum.OCUPADO.equals(processoRemovido.getStatusProcesso())) {
			System.err.println("INFO: Iniciando desaloca��o.");
			
			processoRemovido.setStatusProcesso(StatusEspacoEnum.LIVRE);

			Memoria.processosAlocados.set(index, processoRemovido);
			Memoria.tamanho += processoRemovido.getTamanho();
			System.err.println("\nINFO: Processo " + "id: " + processoRemovido.getIdProcesso()
					+ " | tamanho: " + processoRemovido.getTamanho()
					+ " removido | Espa�o livre.\n");
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
			System.out.println("Espa�o Livre da Mem�ria: "+ memoria + "% | ");
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
		new GeradorDeProcessosWF();

		tamanhoMedioProcessosWorst = (double) somaTotalDeTodosProcessos / totalProcessosGerados;
		ocupacaoMediaMemoriaWorst =  ((double)(totalEspacoLivre / (totalProcessosAlocados * ESPACO_TOTAL_MEMORIA)) * 100);
		taxaDescarteWorst = (double) totalProcessosDescartados / totalProcessosGerados * 100;
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
		symbols.setDecimalSeparator('.');
		DecimalFormat df = new DecimalFormat("#.##", symbols);

		tamanhoMedioProcessosWorst = Double.parseDouble(df.format(tamanhoMedioProcessosWorst));
		ocupacaoMediaMemoriaWorst = Double.parseDouble(df.format(ocupacaoMediaMemoriaWorst));
		taxaDescarteWorst = Double.parseDouble(df.format(taxaDescarteWorst));

		System.out.println("\nResultados do Worst Fit:");
		System.out.println("Tamanho m�dio dos processos gerados: " + tamanhoMedioProcessosWorst);
		System.out.println("Ocupa��o m�dia da mem�ria por segundo: " + ocupacaoMediaMemoriaWorst + "%");
		System.out.println("Taxa de descarte: " + taxaDescarteWorst + "%");
	}
	
}
