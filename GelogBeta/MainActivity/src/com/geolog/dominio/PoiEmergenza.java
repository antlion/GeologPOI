/**
 * 
 */
package com.geolog.dominio;

import java.util.Date;

import android.location.Location;

/**
 * @author antlion
 *
 */
public class PoiEmergenza extends POIBase {

	/**
	 * 
	 */
	public PoiEmergenza(Categoria categoria,String nome, String descrizione,int idTipo,Location location) {
		setCategoria(categoria);
		setNome(nome);
		setDescrizione(descrizione);
		setIdTipo(idTipo);
		
		setLocation(location);
		
		// TODO Auto-generated constructor stub
	}
	public void chiamataRapida()
	{
		
	}

}
