package com.geolog;

import java.util.ArrayList;

import android.location.Location;

import com.geolog.dominio.POIBase;
import com.geolog.util.CategoryHandler;
import com.geolog.web.WebService;

public class GestorePOI {

	
	public static POIBase creaPOI()
	{
		
		return null;
	}
	
	public static void segnalaPOI(POIBase poiBase,String descrizione){
		System.out.println(descrizione);
		WebService.segnalaPOI(poiBase, descrizione);
	}
	
	public static ArrayList<POIBase> cercaPOI(Location location)
	{
		CategoryHandler gC = new CategoryHandler();
		gC.richiediCategorieSelezionate();
		//chiama il servizio del web service
		return WebService.richiediCategorie(location, gC.richiediCategorieSelezionate());
		
		
	}
}
