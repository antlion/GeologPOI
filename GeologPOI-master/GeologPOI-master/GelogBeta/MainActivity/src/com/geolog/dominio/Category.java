package com.geolog.dominio;

import java.util.Set;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.geolog.R;
import com.geolog.util.ResourcesHandler;



public class Category {
	private String name;
	

	private int id;
	private String description;
	
	private String icon;
	private Set<Poi> pois;
	
	
	public Category(String nomeCategoria,int idTipo,int icon)
	{
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

	public Drawable getIconFromResource(Context context)
	{
		if (ResourcesHandler.controlImageResource(String.valueOf(getId()), context))
			return  Drawable.createFromPath(context.getFilesDir().toString()+"//"+String.valueOf(getId()));
		Drawable d = ResourcesHandler.getDrawableFromUri(icon, context,String.valueOf(getId()));
		if ( d == null)
			return  context.getResources().getDrawable( R.drawable.no_image_icon);
		return ResourcesHandler.getDrawableFromUri(icon, context,String.valueOf(getId()));
	}

}
