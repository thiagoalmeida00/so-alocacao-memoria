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
 * Exercício 03 - FirstFit de Alocação
 */
package br.com.unifacisa.so.aplicacao;

import java.util.Scanner;

import br.com.unifacisa.so.entidades.algoritmos.BestFit;
import br.com.unifacisa.so.entidades.algoritmos.FirstFit;
import br.com.unifacisa.so.entidades.algoritmos.NextFit;
import br.com.unifacisa.so.entidades.algoritmos.WorstFit;

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
				FirstFit.executar();
				break;
			
			case "2":
				System.out.println("Opcao escolhida: Alocação Next Fit...\n");
				NextFit.executar();
				break;
			
			case "3":
				System.out.println("Opcao escolhida: Alocação Best Fit...\n");
				BestFit.executar();
				break;
			
			case "4":
				System.out.println("Opcao escolhida: Alocação Worst Fit...\n");
				WorstFit.executar();
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
