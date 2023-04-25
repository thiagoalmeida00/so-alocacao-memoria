/**
 * Unifacisa - Sistemas de Informação
 * Sistemas Operacionais
 * Professora: Roberta Pereira
 * Equipe:
 * @author Lucas Lago
 * @author José Maykon
 * @author Thiago Azevedo
 * @author Thiago Almeida
 * 
 * Exercício 03 - Algoritmos de Alocação
 */
package br.com.unifacisa.so.aplicacao;

import java.util.Scanner;

import br.com.unifacisa.so.entidades.algoritmos.Algoritmos;
import br.com.unifacisa.so.entidades.enums.AlocacaoEnums;

public class Aplicacao {

	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
        String opcao = "0";
        
        do {
        	System.out.println("Escolha um dos algoritmos para executar o Sistema:\n");
        	System.out.println("1 - Algoritmo: Alocação First Fit");
        	System.out.println("2 - Algoritmo: Alocação Next Fit");
        	System.out.println("3 - Algoritmo: Alocação Best Fit");
        	System.out.println("4 - Algoritmo: Alocação Worst Fit");
        	System.out.println("5 - Sair");
        	
        	System.out.println("Digite a opcao: ");
        	opcao = scanner.nextLine();
        	
        	switch (opcao) {
			case "1":
				System.out.println("Opcao escolhida: Alocação First Fit...\n");
				Algoritmos.executar(AlocacaoEnums.FIRST_FIT.getDescricao());
				break;
			
			case "2":
				System.out.println("Opcao escolhida: Alocação Next Fit...\n");
				Algoritmos.executar(AlocacaoEnums.NEXT_FIT.getDescricao());
				break;
			
			case "3":
				System.out.println("Opcao escolhida: Alocação Best Fit...\n");
				Algoritmos.executar(AlocacaoEnums.BEST_FIT.getDescricao());
				break;
			
			case "4":
				System.out.println("Opcao escolhida: Alocação Worst Fit...\n");
				Algoritmos.executar(AlocacaoEnums.WORST_FIT.getDescricao());
				break;
			
			case "5":
				System.out.println("Encerrando o programa...");
				break;

			default:
				System.out.println("Opcao invalida. Digite novamente.");
			}
        	
        } while (!opcao.equals("5"));
        scanner.close();

	}

}
