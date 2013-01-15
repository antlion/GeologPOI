package com.geolog.dominio;

import java.util.Set;

/**
 * Tipi delle risorse.Ogni risorsa pu� avere solo un tipo.
 * 
 * @author Lorenzo
 * 
 */
public class ResourceType  {

	// Identificativo della risorsa
	/**
	 * @uml.property  name="id"
	 */
	private int id;
	// Nome della risorsa
	/**
	 * @uml.property  name="name"
	 */
	private String name;
	// Risorse associate al tipo di rosorsa
	/**
	 * @uml.property  name="resources"
	 */
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

	

	/**
	 * @return
	 * @uml.property  name="id"
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * @param id
	 * @uml.property  name="id"
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return
	 * @uml.property  name="name"
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 * @uml.property  name="name"
	 */
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
