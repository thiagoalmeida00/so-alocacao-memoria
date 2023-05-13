package br.com.unifacisa.so.entidades.comuns;

public class GeradorDeProcessos {

	private int ultimoId = 0;

    public Processo gerarProcesso() {
        int tamanho = (int) (Math.random() * 41) + 10;
        Processo processo = new Processo(ultimoId++, tamanho);
        return processo;
    }
}
