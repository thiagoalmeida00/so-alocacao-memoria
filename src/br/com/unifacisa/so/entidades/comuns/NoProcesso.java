package br.com.unifacisa.so.entidades.comuns;

public class NoProcesso {

	private int tamanho;
    private Processo processo;
    private NoProcesso proximo;

    public NoProcesso(int tamanho) {
        this.tamanho = tamanho;
        this.processo = null;
        this.proximo = null;
    }

    public int getTamanho() {
        return tamanho;
    }

    public void setTamanho(int tamanho) {
        this.tamanho = tamanho;
    }

    public Processo getProcesso() {
        return processo;
    }

    public void setProcesso(Processo processo) {
        this.processo = processo;
    }

    public NoProcesso getProximo() {
        return proximo;
    }

    public void setProximo(NoProcesso proximo) {
        this.proximo = proximo;
    }

}
