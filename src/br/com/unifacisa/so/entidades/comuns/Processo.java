package br.com.unifacisa.so.entidades.comuns;

public class Processo {

	private Integer idProcesso;
	private Integer tamanhoAlocacao;
	
	public Processo(Integer idProcesso, Integer tamanhoAlocacao) {
		this.idProcesso = idProcesso;
		this.tamanhoAlocacao = tamanhoAlocacao;
	}

	public Integer getIdProcesso() {
		return idProcesso;
	}

	public Integer getTamanhoAlocacao() {
		return tamanhoAlocacao;
	}

	//TODO implementar regra
	/*public void executarInstrucao() {
        if (quantidadeInstrucoes > 0) {
        	quantidadeInstrucoes--;
        }
    }

	public boolean isFinalizado() {
        return quantidadeInstrucoes == 0;
    }*/
	
    @Override
	public String toString() {
		return "Processo [id = " + idProcesso + "| Tamanho Alocação = " + tamanhoAlocacao + "]";
    }
}
