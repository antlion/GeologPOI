package com.geolog.dominio;


/**
 * @author  Lorenzo
 */
public interface IEntity extends Comparable<IEntity> {

	public int getId();
	public void setId(int id);
	
	/**
	 * @uml.property  name="label"
	 */
	public String getLabel();
	
}