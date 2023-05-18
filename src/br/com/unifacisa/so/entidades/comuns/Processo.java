package br.com.unifacisa.so.entidades.comuns;

import br.com.unifacisa.so.entidades.comuns.enums.StatusEspacoEnum;

public class Processo {

	private Integer idProcesso;
	private Integer tamanho;
	private StatusEspacoEnum statusProcesso;

	public Processo(Integer idProcesso, Integer tamanho, StatusEspacoEnum statusProcesso) {
		this.idProcesso = idProcesso;
		this.tamanho = tamanho;
		this.statusProcesso = statusProcesso;
	}

	public StatusEspacoEnum getStatusProcesso() {
		return statusProcesso;
	}

	public void setStatusProcesso(StatusEspacoEnum statusProcesso) {
		this.statusProcesso = statusProcesso;
	}

	public Integer getIdProcesso() {
		return idProcesso;
	}

	public Integer getTamanho() {
		return tamanho;
	}

	public void setTamanho(Integer tamanho){
		this.tamanho = tamanho;
	}

	public boolean isLivre() {
		if (StatusEspacoEnum.LIVRE.equals(this.statusProcesso)) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "Processo [idProcesso = " + idProcesso + ", tamanho = " + tamanho + ", statusProcesso = "
				+ statusProcesso + "]";
	}

}
