package com.geolog.dominio;

import java.util.Set;



public class Category {
	private String name;
	

	private int id;
	private String description;
	private int icon;
	private Set<Poi> pois;
	
	
	public Category(String nomeCategoria,int idTipo,int icon)
	{
		this.name = nomeCategoria;
		this.id = idTipo;
		this.icon = icon;
	}
	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	

	public String getNomeCategoria() {
		return name;
	}

	public void setNomeCategoria(String charSequence) {
		this.name = charSequence;
	}


}
