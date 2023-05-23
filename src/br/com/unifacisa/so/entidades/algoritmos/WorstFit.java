package br.com.unifacisa.so.entidades.algoritmos;

import br.com.unifacisa.so.entidades.comuns.Memoria;
import br.com.unifacisa.so.entidades.comuns.NoProcesso;
import br.com.unifacisa.so.entidades.comuns.Processo;
import br.com.unifacisa.so.entidades.gerador.GeradorDeProcessosFF;

public class WorstFit {

	public static void executar() {
        Memoria memoria = new Memoria(1000);
        GeradorDeProcessosFF gerador = new GeradorDeProcessosFF();
        int totalProcessosGerados = 0;
        int totalProcessosAlocados = 0;
        int totalProcessosDescartados = 0;
        int totalEspacoLivre = memoria.getTamanho();

        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 2; j++) {
                Processo novoProcesso = gerador.gerarProcesso();
                totalProcessosGerados++;
                boolean alocado = false;
                int maiorEspaco = -1;
                NoProcesso melhorPosicao = null;

                for (NoProcesso noAtual : memoria.getListaProcessos()) {
                    if (noAtual.getProcesso() == null && noAtual.getTamanho() >= novoProcesso.getTamanho() && noAtual.getTamanho() > maiorEspaco) {
                        maiorEspaco = noAtual.getTamanho();
                        melhorPosicao = noAtual;
                    }
                }

                if (melhorPosicao != null) {
                    melhorPosicao.setProcesso(novoProcesso);
                    totalProcessosAlocados++;
                    totalEspacoLivre -= novoProcesso.getTamanho();
                    alocado = true;
                }

                if (!alocado) {
                    System.out.println("Processo " + novoProcesso.getId() + " n�o alocado");
                    totalProcessosDescartados++;
                }
            }

            NoProcesso noAtual = memoria.getListaProcessos().get(0);
            NoProcesso anterior = null;

            while (noAtual != null) {
                if (noAtual.getProcesso() != null) {
                    noAtual.getProcesso().setTamanho(noAtual.getTamanho());
                } else if (anterior != null && anterior.getProcesso() != null) {
                    noAtual.setProcesso(anterior.getProcesso());
                    anterior.setProcesso(null);
                }
                anterior = noAtual;
                noAtual = noAtual.getProximo();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        double tamanhoMedioProcessos = (double) totalEspacoLivre / totalProcessosGerados;
        double ocupacaoMediaMemoria = 100 - ((double) totalEspacoLivre / memoria.getTamanho()) * 100;
        double taxaDescarte = (double) totalProcessosDescartados / totalProcessosGerados * 100;

        System.out.println("\nResultados do Worst Fit:");
        System.out.println("Tamanho m�dio dos processos gerados: " + tamanhoMedioProcessos);
        System.out.println("Ocupa��o m�dia da mem�ria por segundo: " + ocupacaoMediaMemoria + "%");
        System.out.println("Taxa de descarte: " + taxaDescarte + "%");
    }
	
}
