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
	public PoiEmergenza(String tipo,String nome, String descrizione,int idTipo,Location location) {
		setTipo(tipo);
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
