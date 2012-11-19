package com.geolog.dominio;

public class Categoria {
	private String nomeCategoria;
	private int idTipo;
	
	public Categoria(String nomeCategoria,int idTipo)
	{
		this.nomeCategoria = nomeCategoria;
		this.idTipo = idTipo;
	}

	public String getNomeCategoria() {
		return nomeCategoria;
	}

	public void setNomeCategoria(String charSequence) {
		this.nomeCategoria = charSequence;
	}
	

}
