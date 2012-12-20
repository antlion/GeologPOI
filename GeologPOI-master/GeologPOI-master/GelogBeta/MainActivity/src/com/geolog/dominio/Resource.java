package com.geolog.dominio;


public class Resource  {
	
	private int id;
	private String url;
	private String description;
	private Poi poi;
	private ResourceType resourceType;
	
	
	public Resource(int id, String url, String description, Poi poi,
			ResourceType resourceType) {
		super();
		this.id = id;
		this.url = url;
		this.description = description;
		this.poi = poi;
		this.resourceType = resourceType;
	}

	public Resource(){}
	
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
