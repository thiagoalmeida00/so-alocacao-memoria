package br.com.unifacisa.so.entidades.algoritmos;

import br.com.unifacisa.so.entidades.comuns.GeradorDeProcessos;
import br.com.unifacisa.so.entidades.comuns.Memoria;
import br.com.unifacisa.so.entidades.comuns.NoProcesso;
import br.com.unifacisa.so.entidades.comuns.Processo;

public class NextFit {

	public static void executar() {
        Memoria memoria = new Memoria(1000);
        GeradorDeProcessos gerador = new GeradorDeProcessos();
        int totalProcessosGerados = 0;
        int totalProcessosAlocados = 0;
        int totalProcessosDescartados = 0;
        int totalEspacoLivre = memoria.getTamanho();

        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 2; j++) {
                Processo novoProcesso = gerador.gerarProcesso();
                totalProcessosGerados++;
                boolean alocado = false;
                int melhorEspaco = Integer.MAX_VALUE;
                NoProcesso melhorPosicao = null;

                for (NoProcesso noAtual : memoria.getListaProcessos()) {
                    if (noAtual.getProcesso() == null && noAtual.getTamanho() == novoProcesso.getTamanho()) {
                        noAtual.setProcesso(novoProcesso);
                        totalProcessosAlocados++;
                        totalEspacoLivre -= novoProcesso.getTamanho();
                        alocado = true;
                        break;
                    } else if (noAtual.getProcesso() == null && noAtual.getTamanho() > novoProcesso.getTamanho() && noAtual.getTamanho() < melhorEspaco) {
                        melhorEspaco = noAtual.getTamanho();
                        melhorPosicao = noAtual;
                    }
                }

                if (!alocado && melhorPosicao != null) {
                    melhorPosicao.setProcesso(novoProcesso);
                    totalProcessosAlocados++;
                    totalEspacoLivre -= novoProcesso.getTamanho();
                    alocado = true;
                }

                if (!alocado) {
                    System.out.println("Processo " + novoProcesso.getId() + " não alocado");
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

        System.out.println("\nResultados do Best Fit:");
        System.out.println("Tamanho médio dos processos gerados: " + tamanhoMedioProcessos);
        System.out.println("Ocupação média da memória por segundo: " + ocupacaoMediaMemoria + "%");
        System.out.println("Taxa de descarte: " + taxaDescarte + "%");
	}
}
