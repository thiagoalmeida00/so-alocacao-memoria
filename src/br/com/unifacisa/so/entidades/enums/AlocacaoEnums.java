package br.com.unifacisa.so.entidades.enums;

public enum AlocacaoEnums {

	FIRST_FIT("First Fit"),
	NEXT_FIT("Next Fit"),
	BEST_FIT("Best Fit"),
	WORST_FIT("Worst Fit");

	private String descricao;
	
	AlocacaoEnums(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return this.descricao;
	}
	
}
