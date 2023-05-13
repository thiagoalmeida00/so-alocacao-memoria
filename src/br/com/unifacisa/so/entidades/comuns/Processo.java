package br.com.unifacisa.so.entidades.comuns;

public class Processo {

	private int id;
    private int tamanho;

    public Processo(int id, int tamanho) {
        this.id = id;
        this.tamanho = tamanho;
    }

    public int getId() {
        return id;
    }

    public int getTamanho() {
        return tamanho;
    }

    public void setTamanho(int tamanho) {
        this.tamanho = tamanho;
    }

}
