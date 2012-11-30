package com.geolog.dominio;


public interface IEntity extends Comparable<IEntity> {

	public int getId();
	public void setId(int id);
	
	public String getLabel();
	
}