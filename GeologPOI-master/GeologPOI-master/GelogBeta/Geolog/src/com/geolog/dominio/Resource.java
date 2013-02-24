package com.geolog.dominio;


/**
 * Risorse dei poi. Le risorse vengono scaricate dal web service e salvate nel dispositivo mobile.
 * @author Lorenzo
 *
 */
public class Resource  {

	//Identificativo della risorsa
	/**
	 * @uml.property  name="id"
	 */
	private int id;
	//Url dove si trova la risorsa
	/**
	 * @uml.property  name="url"
	 */
	private String url;
	//descrizione della risorsa
	/**
	 * @uml.property  name="description"
	 */
	private String description;
	//Il poi a cui � assoicata la risorsa
	/**
	 * @uml.property  name="poi"
	 * @uml.associationEnd  multiplicity="(1 1)" inverse="resources:com.geolog.dominio.Poi"
	 */
	private Poi poi;
	//Il tipo di risora
	/**
	 * @uml.property  name="resourceType"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private String resourceType;
	
	
	/**
	 * Costruttore della classe
	 * @param id identificativo della risorsa
	 * @param url url della risorsa
	 * @param description descrizione della riosrsa
	 * @param poi poi a cui � associata la risorsa
	 * @param resourceType tipo di riosrsa
	 */
	public Resource(int id, String url, String description, Poi poi,
			String resourceType) {
		super();
		this.id = id;
		this.url = url;
		this.description = description;
		this.poi = poi;
		this.resourceType = resourceType;
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
		this.id=id;
	} 
	
	/**
	 * @return
	 * @uml.property  name="url"
	 */
	public String getUrl(){
		return this.url;
	}
	/**
	 * @param url
	 * @uml.property  name="url"
	 */
	public void setUrl(String url){
		this.url = url;
	}
	
	/**
	 * @return
	 * @uml.property  name="description"
	 */
	public String getDescription(){
		return this.description;
	}
	/**
	 * @param description
	 * @uml.property  name="description"
	 */
	public void setDescription(String description){
		this.description = description;
	}
	
	/**
	 * @return
	 * @uml.property  name="poi"
	 */
	public Poi getPoi(){
		return this.poi;
	}
	/**
	 * @param poi
	 * @uml.property  name="poi"
	 */
	public void setPoi(Poi poi){
		this.poi = poi;
	}
	


	public String getLabel() {
		return getUrl();
	}



	public String getResourceType() {
		return resourceType;
	}



	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	
	
}
