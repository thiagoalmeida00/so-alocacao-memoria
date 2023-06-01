package br.com.unifacisa.so.entidades.algoritmos;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class AllAlgoritms {

    private static final int MAX_EXECUCOES = 10;
    private static double tamanhoMedioFirst = 0.0;
    private static double ocupacaoMediaMemoriaFirst = 0.0;
    private static double taxaDeDescarteFirst = 0.0;
    private static double tamanhoMedioBest = 0.0;
    private static double ocupacaoMediaMemoriaBest = 0.0;
    private static double taxaDeDescarteBest = 0.0;
    private static double tamanhoMedioWorst = 0.0;
    private static double ocupacaoMediaMemoriaWorst = 0.0;
    private static double taxaDeDescarteWorst = 0.0;
    private static double tamanhoMedioNext = 0.0;
    private static double ocupacaoMediaMemoriaNext = 0.0;
    private static double taxaDeDescarteNext = 0.0;


    public static void executarTodos(){

        boolean isContinue = true;
        while(isContinue){
            for (int i = 0; i < MAX_EXECUCOES; i++) {
                FirstFit.executar();
                tamanhoMedioFirst += FirstFit.tamanhoMedioProcessosFirst;
                ocupacaoMediaMemoriaFirst += FirstFit.ocupacaoMediaMemoriaFirst;
                taxaDeDescarteFirst += FirstFit.taxaDescarteFirst;
            }
            tamanhoMedioFirst = tamanhoMedioFirst/MAX_EXECUCOES;
            ocupacaoMediaMemoriaFirst = ocupacaoMediaMemoriaFirst/MAX_EXECUCOES;
            taxaDeDescarteFirst = taxaDeDescarteFirst/MAX_EXECUCOES;
            for (int i = 0; i < MAX_EXECUCOES; i++) {
                BestFit.executar();
                tamanhoMedioBest += BestFit.tamanhoMedioProcessosBest;
                ocupacaoMediaMemoriaBest += BestFit.ocupacaoMediaMemoriaBest;
                taxaDeDescarteBest += BestFit.taxaDescarteBest;
            }
            tamanhoMedioBest = tamanhoMedioBest/MAX_EXECUCOES;
            ocupacaoMediaMemoriaBest = ocupacaoMediaMemoriaBest/MAX_EXECUCOES;
            taxaDeDescarteBest = taxaDeDescarteBest/MAX_EXECUCOES;
            for (int i = 0; i < MAX_EXECUCOES; i++) {
                WorstFit.executar();
                tamanhoMedioWorst += WorstFit.tamanhoMedioProcessosWorst;
                ocupacaoMediaMemoriaWorst += WorstFit.ocupacaoMediaMemoriaWorst;
                taxaDeDescarteWorst += WorstFit.taxaDescarteWorst;
            }
            tamanhoMedioWorst = tamanhoMedioWorst/MAX_EXECUCOES;
            ocupacaoMediaMemoriaWorst = ocupacaoMediaMemoriaWorst/MAX_EXECUCOES;
            taxaDeDescarteWorst = taxaDeDescarteWorst/MAX_EXECUCOES;
            for (int i = 0; i < MAX_EXECUCOES; i++) {
                NextFit.executar();
                tamanhoMedioNext += NextFit.tamanhoMedioProcessosNext;
                ocupacaoMediaMemoriaNext += NextFit.ocupacaoMediaMemoriaNext;
                taxaDeDescarteNext += NextFit.taxaDescarteNext;
            }
            tamanhoMedioNext = tamanhoMedioNext/MAX_EXECUCOES;
            ocupacaoMediaMemoriaNext = ocupacaoMediaMemoriaNext/MAX_EXECUCOES;
            taxaDeDescarteNext = taxaDeDescarteNext/MAX_EXECUCOES;
            exibirMaiorMediaDeTamanho();
            exibirMaiorOcupacaoMediaMemoria();
            exibirMaiorTaxaDeDescarte();

            isContinue = false;
        }

    }

    private static void exibirMaiorTaxaDeDescarte() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("#.##", symbols);
        if(taxaDeDescarteFirst > taxaDeDescarteWorst
                && taxaDeDescarteFirst > taxaDeDescarteBest
                && taxaDeDescarteFirst > taxaDeDescarteNext){
            System.out.println("INFO: Maior Media de Taxa de Descarte \n" +
                    " FirstFit: "+ df.format(taxaDeDescarteFirst));
        }else if(taxaDeDescarteWorst > taxaDeDescarteFirst
                && taxaDeDescarteWorst > taxaDeDescarteNext
                && taxaDeDescarteWorst > taxaDeDescarteBest){
            System.out.println("INFO: Maior Media de Taxa de Descarte \n" +
                    " WorstFit: "+ df.format(taxaDeDescarteWorst));
        }else if(taxaDeDescarteBest > taxaDeDescarteWorst
                && taxaDeDescarteBest > taxaDeDescarteFirst
                && taxaDeDescarteBest > taxaDeDescarteNext){
            System.out.println("INFO: Maior Media de Taxa de Descarte \n" +
                    " BestFit: "+ df.format(taxaDeDescarteBest));
        } else{
            System.out.println("INFO: Maior Media de Taxa de Descarte \n" +
                    " NextFit: "+ df.format(taxaDeDescarteNext));
        }
    }

    private static void exibirMaiorOcupacaoMediaMemoria() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("#.##", symbols);
        if(ocupacaoMediaMemoriaFirst > ocupacaoMediaMemoriaWorst
                && ocupacaoMediaMemoriaFirst > ocupacaoMediaMemoriaBest
                && ocupacaoMediaMemoriaFirst > ocupacaoMediaMemoriaNext){
            System.out.println("INFO: Maior Ocupacao Media da Memória \n" +
                    " FirstFit: "+ df.format(ocupacaoMediaMemoriaFirst));
        }else if(ocupacaoMediaMemoriaWorst > ocupacaoMediaMemoriaFirst
                && ocupacaoMediaMemoriaWorst > ocupacaoMediaMemoriaNext
                && ocupacaoMediaMemoriaWorst > ocupacaoMediaMemoriaBest){
            System.out.println("INFO: Maior Ocupacao Media da Memória \n" +
                    " WorstFit: "+ df.format(ocupacaoMediaMemoriaWorst));
        }else if(ocupacaoMediaMemoriaBest > ocupacaoMediaMemoriaWorst
                && ocupacaoMediaMemoriaBest > ocupacaoMediaMemoriaFirst
                && ocupacaoMediaMemoriaBest > ocupacaoMediaMemoriaNext){
            System.out.println("INFO: Maior Ocupacao Media da Memória \n" +
                    " BestFit: "+ df.format(ocupacaoMediaMemoriaBest));
        } else{
            System.out.println("INFO: Maior Ocupacao Media da Memória \n" +
                    " NextFit: "+ df.format(ocupacaoMediaMemoriaNext));
        }
    }

    private static void exibirMaiorMediaDeTamanho() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("#.##", symbols);
        if(tamanhoMedioFirst > tamanhoMedioWorst
                && tamanhoMedioFirst > tamanhoMedioBest
                && tamanhoMedioFirst > tamanhoMedioNext){
            System.out.println("INFO: Maior Média de Tamanho dos Processos Gerados \n" +
                    " FirstFit: "+ df.format(tamanhoMedioFirst));
        }else if(tamanhoMedioWorst > tamanhoMedioFirst
                && tamanhoMedioWorst > tamanhoMedioNext
                && tamanhoMedioWorst > tamanhoMedioBest){
            System.out.println("INFO: Maior Média de Tamanho dos Processos Gerados \n" +
                    " WorstFit: "+ df.format(tamanhoMedioWorst));
        }else if(tamanhoMedioBest > tamanhoMedioWorst
                && tamanhoMedioBest > tamanhoMedioFirst
                && tamanhoMedioBest > tamanhoMedioNext){
            System.out.println("INFO: Maior Média de Tamanho dos Processos Gerados \n" +
                    " BestFit: "+ df.format(tamanhoMedioBest));
        } else{
            System.out.println("INFO: Maior Média de Tamanho dos Processos Gerados \n" +
                    " NextFit: "+ df.format(tamanhoMedioNext));
        }
    }
}
