package com.geolog.dominio;

import java.util.Set;

/**
 * Tipi delle risorse.Ogni risorsa puà avere solo un tipo.
 * 
 * @author Lorenzo
 * 
 */
public class ResourceType implements IEntity {

	// Identificativo della risorsa
	private int id;
	// Nome della risorsa
	private String name;
	// Risorse associate al tipo di rosorsa
	private Set<Resource> resources;

	/**
	 * Costruttore della classe
	 * 
	 * @param name
	 *            nome della risorsa
	 */
	public ResourceType(String name) {
		super();
		this.name = name;
	}

	public int compareTo(IEntity o) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Resource> getResource() {
		return this.resources;
	}

	public void setResource(Set<Resource> resources) {
		this.resources = resources;
	}

	public String getLabel() {
		return getName();
	}

}
