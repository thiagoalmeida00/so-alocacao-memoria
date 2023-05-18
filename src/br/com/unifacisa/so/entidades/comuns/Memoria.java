package br.com.unifacisa.so.entidades.comuns;

import java.util.ArrayList;
import java.util.List;

public class Memoria {

	public static Integer tamanho = 600;
	public static List<Processo> processosAlocados = new ArrayList<Processo>();

	public static Integer getTamanho() {
		return tamanho;
	}

}
