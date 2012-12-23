package com.geolog.dominio;

import com.geolog.R;

import geolog.util.ResourcesManager;
import android.content.Context;

import android.graphics.drawable.Drawable;



/**
 * Categoria di poi. La categoria rappresenta un isieme di poi che condividono
 * le stesse informazioni.Ogni categoria ha un nome,un id univoco una
 * descrizione e un icona rappresentativa.
 * 
 * @author Lorenzo
 * 
 */
public class Category {

	// nome della categoria
	private String name;
	// Id della categoria
	private int id;
	// descrizione della categoria
	private String description;
	// icona della categoria
	private String icon;

	//Icona per la realtà aumentata
	private int iconAR;
	/**
	 * Costruttore
	 * 
	 * @param nomeCategoria
	 *            nome della categoria
	 * @param idTipo
	 *            id della categoria
	 * @param icon
	 *            icona della categoria
	 */
	public Category(String nomeCategoria, int idTipo, int icon) {
		this.name = nomeCategoria;
		this.id = idTipo;

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getNomeCategoria() {
		return name;
	}

	public void setNomeCategoria(String charSequence) {
		this.name = charSequence;
	}

	/**
	 * Creazione di un oggetto di tipo drawable che rappresenta l'icona della
	 * categoria
	 * 
	 * @param context
	 *            contesto dell'applicazione che richiama il metodo
	 * @return Drawable l'icona della categoria sotto forma di drawable
	 */
	public Drawable getIconFromResource(Context context) {
		// Se l'icona è stata già scaricata, viene resituita, altrimenti viene
		// scaricata
		if (ResourcesManager.controlImageResource(icon, context))
			return Drawable.createFromPath(context.getFilesDir().toString()
					+ "//" + ResourcesManager.getNameFileFromUrl(icon));
		Drawable d = ResourcesManager.getDrawableFromUri(icon, context);
		// Se si è verificato un errore durante lo scaricamento, viene
		// restituita un icona base,altrimenti l'icona della categoria
		if (d == null)
			return context.getResources().getDrawable(R.drawable.no_image_icon);
		return ResourcesManager.getDrawableFromUri(icon, context);
	}

	
}
