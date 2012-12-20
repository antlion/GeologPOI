package com.geolog.dominio;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.geolog.R;
import com.geolog.util.ResourcesHandler;
import com.geolog.web.Services;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
	
	
	public Poi(Category category, String name, String description,
			Date creationDate, Location location) {
		super();
		this.category = category;
		this.name = name;
		this.description = description;
		this.creationDate = creationDate;
		this.latitude = location.getLatitude();
		this.longitude = location.getLongitude();
	}
	
	public Set<Resource> getResources() {
		return resources;
	}
	public void setResources(Set<Resource> resources) {
		this.resources = resources;
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
	
	public Drawable setImageFromResource(Context context)
	{
		if ( resources == null)
		{
			//no image aviable
			return context.getResources().getDrawable( R.drawable.no_image_icon);
		}
			
			Iterator<Resource> it = resources.iterator();

			while(it.hasNext())
			{
			Resource elemento = it.next();
			if (elemento.getResourceType().getName().equals("image/jpeg"))
			{
				if (ResourcesHandler.controlImageResource(elemento.getUrl(), context))
					return  Drawable.createFromPath(context.getFilesDir().toString()+"//"+ResourcesHandler.getNameFileFromUrl(elemento.getUrl()));
				
				
				
				Services services = new Services();
				Drawable d = services.downloadResource(elemento.getUrl(), context);
				if (d != null){
					
					return d;}
				else{
					return context.getResources().getDrawable( R.drawable.no_image_aviable);
					}
			}
			}
			return context.getResources().getDrawable( R.drawable.no_image_aviable);
		
	
	}
	
}
