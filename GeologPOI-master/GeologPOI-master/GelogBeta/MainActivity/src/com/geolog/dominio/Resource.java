package com.geolog.dominio;


/**
 * Risorse dei poi. Le risorse vengono scaricate dal web service e salvate nel dispositivo mobile.
 * @author Lorenzo
 *
 */
public class Resource  {

	//Identificativo della risorsa
	private int id;
	//Url dove si trova la risorsa
	private String url;
	//descrizione della risorsa
	private String description;
	//Il poi a cui è assoicata la risorsa
	private Poi poi;
	//Il tipo di risora
	private ResourceType resourceType;
	
	
	/**
	 * Costruttore della classe
	 * @param id identificativo della risorsa
	 * @param url url della risorsa
	 * @param description descrizione della riosrsa
	 * @param poi poi a cui è associata la risorsa
	 * @param resourceType tipo di riosrsa
	 */
	public Resource(int id, String url, String description, Poi poi,
			ResourceType resourceType) {
		super();
		this.id = id;
		this.url = url;
		this.description = description;
		this.poi = poi;
		this.resourceType = resourceType;
	}

	
	
	public int getId() {
		return this.id;
	}
	public void setId(int id) {
		this.id=id;
	} 
	
	public String getUrl(){
		return this.url;
	}
	public void setUrl(String url){
		this.url = url;
	}
	
	public String getDescription(){
		return this.description;
	}
	public void setDescription(String description){
		this.description = description;
	}
	
	public Poi getPoi(){
		return this.poi;
	}
	public void setPoi(Poi poi){
		this.poi = poi;
	}
	
	public ResourceType getResourceType(){
		return this.resourceType;
	}
	public void setResourceType(ResourceType type){
		this.resourceType = type;
	}

	public String getLabel() {
		return getUrl();
	}
	
	public int compareTo(IEntity o) {
		return 0;
	}
}
