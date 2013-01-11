package com.geolog.dominio;

import geolog.managers.ResourcesManager;
import geolog.web.WebService;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import com.geolog.activity.R;



import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;

/**
 * Punti di interesse.Gestione di punti di interesse. Ogni punto di interesse
 * appartiene ad una categoria, ha un nome,descrizione,id univoco,una data di
 * creazione e le cordinate geografiche. Ha un isieme di risorse che
 * rappresentano i contenuti multimediali associati al poi:
 * video,immagini,ecc...
 * 
 * @author Lorenzo
 * 
 */
public class Poi {

	// Categoria di appartenza al poi
	/**
	 * @uml.property  name="category"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private Category category;
	// Nome del poi
	/**
	 * @uml.property  name="name"
	 */
	private String name;
	// Descrizione
	/**
	 * @uml.property  name="description"
	 */
	private String description;
	// Identificativo del po
	/**
	 * @uml.property  name="id"
	 */
	private int id;
	// Data di creazione del poi
	/**
	 * @uml.property  name="creationDate"
	 */
	private Date creationDate;
	// Insieme di risorse multimediali del poi
	/**
	 * @uml.property  name="resources"
	 * @uml.associationEnd  multiplicity="(0 -1)" inverse="poi:com.geolog.dominio.Resource"
	 */
	private Set<Resource> resources;
	// Latitudine del poi
	/**
	 * @uml.property  name="latitude"
	 */
	private double latitude;
	// Longitudine del poi
	/**
	 * @uml.property  name="longitude"
	 */
	private double longitude;
	
	
	private int category_id;
	/**
	 * Costruttore della classe
	 * 
	 * @param categoria
	 *            categoria di appartenza del poi
	 * @param nome
	 *            nome del poi
	 * @param descrizione
	 *            descrizione del poi
	 * @param idTipo
	 *            identificativo del poi
	 * @param dataCreazione
	 *            data di creazione del poi
	 * @param location
	 *            Location del poi
	 */
	public Poi(Category categoria, String nome, String descrizione, int idTipo,
			Date dataCreazione, Location location, int image) {
		super();
		this.category = categoria;
		this.name = nome;
		this.description = descrizione;
		this.id = idTipo;
		this.creationDate = dataCreazione;
		this.latitude = location.getLatitude();
		this.longitude = location.getLatitude();

	}

	/**
	 * Costruttore della classe
	 * 
	 * @param categoria
	 *            categoria di appartenza del poi
	 * @param nome
	 *            nome del poi
	 * @param descrizione
	 *            descrizione del poi
	 * @param idTipo
	 *            identificativo del poi
	 * @param dataCreazione
	 *            data di creazione del poi
	 * @param lat
	 *            latituidine del poi
	 * @param log
	 *            longitudine del poi
	 */
	public Poi(Category categoria, String nome, String descrizione, int idTipo,
			Date dataCreazione, double lat, double log, int image) {
		super();
		this.category = categoria;
		this.name = nome;
		this.description = descrizione;
		this.id = idTipo;
		this.creationDate = dataCreazione;
		this.latitude = lat;
		this.longitude = log;

	}

	/**
	 * Costruttore della classe
	 * 
	 * @param categoria
	 *            categoria di appartenza del poi
	 * @param nome
	 *            nome del poi
	 * @param descrizione
	 *            descrizione del poi
	 * @param idTipo
	 *            identificativo del poi
	 * @param dataCreazione
	 *            data di creazione del poi
	 * @param location
	 *            Location del poi
	 */
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

	
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	

	public Location getPOILocation() {
		Location poiLocation = new Location("poi");
		poiLocation.setLatitude(getLatitude());
		poiLocation.setLongitude(getLongitude());
		return poiLocation;
	}

	/**
	 * @return
	 * @uml.property  name="latitude"
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude
	 * @uml.property  name="latitude"
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return
	 * @uml.property  name="longitude"
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude
	 * @uml.property  name="longitude"
	 */
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

	/**
	 * Creazione di un drawable a partire da un risorsa presente nel poi.
	 * 
	 * @param context
	 *            contesto dell'attività che chiama il metodo
	 * @return Drawable la risorsa sotto forma di drawable
	 */
	public Drawable setImageFromResource(Context context) {
		// Controllo che il poi abbia delle risorse
		if (resources == null) {
			// Se nomo somo presenti le risorse resituisco una risorsa base
			return context.getResources().getDrawable(R.drawable.no_image_icon);
		}
		// Altrimenti scandisco le risorse finchè non trovo quella desiderata
		// creo un nuovo iteratore
		Iterator<Resource> it = resources.iterator();
		// Cerco la risorsa
		while (it.hasNext()) {
			Resource elemento = it.next();
			if (elemento.getResourceType().getName().equals("image/jpeg")) {
				// Controllo che la risorsa non sià già presente sul sistema. In
				// caso positivo la restituisco, in caso negativo la scarico
				if (ResourcesManager.controlImageResource(elemento.getUrl(),
						context))
					return Drawable.createFromPath(context.getFilesDir()
							.toString()
							+ "//"
							+ ResourcesManager.getNameFileFromUrl(elemento
									.getUrl()));
				// Creo un nuovo punto di accesso al servizio web
				WebService services = new WebService();
				// Richiedo la risorsa al servizo web
				Drawable d = services.downloadResource(elemento.getUrl(),
						context);
				// Controllo che la risorsa sia stata scaricata correttamente,
				// in caso positivo la restituisco
				// altrimenti restituisco un ummagine di base
				if (d != null) {

					return d;
				} else {
					return context.getResources().getDrawable(
							R.drawable.no_image_aviable);
				}
			}
		}
		return context.getResources().getDrawable(R.drawable.no_image_aviable);

	}

	public int getCategory_id() {
		return category_id;
	}

	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}

	
}
