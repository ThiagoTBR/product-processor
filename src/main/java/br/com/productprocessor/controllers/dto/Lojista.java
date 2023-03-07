package br.com.productprocessor.controllers.dto;

import lombok.Data;

@Data
public class Lojista {
	
	public String loja;
	
	String product;

	int qtde;
	
	double financeiro;
	
	double precoMedio;
	
	

	public Lojista(String loja, String product, int qtde, double financeiro, double precoMedio) {
		super();
		this.loja = loja;
		this.product = product;
		this.qtde = qtde;
		this.financeiro = financeiro;
		this.precoMedio = precoMedio;
	}



	public Lojista() {
	}
	
}
