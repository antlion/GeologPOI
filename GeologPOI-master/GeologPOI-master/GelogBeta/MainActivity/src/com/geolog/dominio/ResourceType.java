package com.geolog.dominio;

import java.util.Set;

public class ResourceType implements IEntity {

	private int id;
	private String name;
	private Set<Resource> resources;
	
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
		this.id=id;
	}
	
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;		
	}
	
	public Set<Resource> getResource(){
		return this.resources;
	}
	public void setResource(Set<Resource> resources){
		this.resources = resources;
	}

	public String getLabel() {
		return getName();
	}

}
