package com.geolog.dominio;

import com.geolog.activity.R;

import geolog.managers.ResourcesManager;
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
	/**
	 * @uml.property  name="name"
	 */
	private String name;
	// Id della categoria
	/**
	 * @uml.property  name="id"
	 */
	private int id;
	// descrizione della categoria
	/**
	 * @uml.property  name="description"
	 */
	private String description;
	// icona della categoria
	/**
	 * @uml.property  name="icon"
	 */
	private String icon;


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
	public Category(String nomeCategoria, int idTipo, String icon) {
		this.name = nomeCategoria;
		this.id = idTipo;

	}

	/**
	 * @return
	 * @uml.property  name="id"
	 */
	public int getId() {
		return id;
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
	 * @uml.property  name="description"
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 * @uml.property  name="description"
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return
	 * @uml.property  name="icon"
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * @param icon
	 * @uml.property  name="icon"
	 */
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
		// Se l'icona � stata gi� scaricata, viene resituita, altrimenti viene
		// scaricata
		/*if (ResourcesManager.controlImageResource(icon, context))
			return Drawable.createFromPath(context.getFilesDir().toString()
					+ "//" + ResourcesManager.getNameFileFromUrl(icon));*/
		Drawable d = ResourcesManager.getDrawableFromUri(icon, context);
		// Se si � verificato un errore durante lo scaricamento, viene
		// restituita un icona base,altrimenti l'icona della categoria
		if (d == null)
			return context.getResources().getDrawable(R.drawable.no_image_icon);
		return d;
	}

	
}
