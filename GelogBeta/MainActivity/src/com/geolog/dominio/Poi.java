package com.geolog.dominio;

import java.util.Date;
import java.util.Set;



import android.graphics.Bitmap;
import android.location.Location;

public class Poi {

	
	private Category category;
	private String name;
	private String description;
	private int id;
	private Date creationDate;
	private Set<Resource> resources;
	private double latitude;
	private double longitude;
	private int image;
	
	
	public Poi(Category categoria, String nome, String descrizione, int idTipo,
			Date dataCreazione, Location location, int image) {
		super();
		this.category = categoria;
		this.name = nome;
		this.description = descrizione;
		this.id = idTipo;
		this.creationDate = dataCreazione;
		this.latitude =  location.getLatitude();
		this.longitude = location.getLatitude();
		this.image = image;
	}
	public Poi(Category categoria, String nome, String descrizione, int idTipo,
			Date dataCreazione,double lat,double log, int image) {
		super();
		this.category = categoria;
		this.name = nome;
		this.description = descrizione;
		this.id = idTipo;
		this.creationDate = dataCreazione;
		this.latitude = lat;
		this.longitude = log;
		this.image = image;
	}
	
	
	public Category getCategoria() {
		return category;
	}
	public void setCategoria(Category categoria) {
		this.category = categoria;
	}
	
	public Location getPOILocation()
	{
		Location poiLocation = new Location("poi");
		poiLocation.setLatitude(getLatitude());
		poiLocation.setLongitude(getLongitude());
		return poiLocation;
	}
	
	public int getImage() {
		return image;
	}
	public void setImage(int image) {
		this.image = image;
	}
	
	
	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getNome() {
		return name;
	}
	public void setNome(String nome) {
		this.name = nome;
	}
	public String getDescrizione() {
		return description;
	}
	public void setDescrizione(String descrizione) {
		this.description = descrizione;
	}
	public int getIdTipo() {
		return id;
	}
	public void setIdTipo(int idTipo) {
		this.id = idTipo;
	}
	public Date getDataCreazione() {
		return creationDate;
	}
	public void setDataCreazione(Date dataCreazione) {
		this.creationDate = dataCreazione;
	}
	
}
