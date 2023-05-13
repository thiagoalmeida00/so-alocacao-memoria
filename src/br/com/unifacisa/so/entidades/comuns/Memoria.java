package br.com.unifacisa.so.entidades.comuns;

import java.util.ArrayList;
import java.util.List;

public class Memoria {
	
	private int tamanho;
    private List<NoProcesso> listaProcessos;

    public Memoria(int tamanho) {
        this.tamanho = tamanho;
        this.listaProcessos = new ArrayList<>();
        NoProcesso primeiroNo = new NoProcesso(tamanho);
        this.listaProcessos.add(primeiroNo);
    }

    public int getTamanho() {
        return tamanho;
    }

    public List<NoProcesso> getListaProcessos() {
        return listaProcessos;
    }
}
