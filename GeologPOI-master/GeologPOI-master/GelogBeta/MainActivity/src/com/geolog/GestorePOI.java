package com.geolog;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.geolog.dominio.Poi;
import com.geolog.util.CategoryHandler;
import com.geolog.web.FindNearbyService;
import com.geolog.web.PoiListResponse;
import com.geolog.web.WebService;


public class GestorePOI {

	
	public static Poi creaPOI()
	{
		
		return null;
	}
	
	public static void segnalaPOI(Poi poiBase,String descrizione){
		System.out.println(descrizione);
	
	}
	
	public static ArrayList<Poi> cercaPOI(Location location,Context context) 
	{
		CategoryHandler categoryHandler = new CategoryHandler();
		
		//chiama il servizio del web service
		ArrayList<Poi> arrayPOI = new ArrayList<Poi>();
		WebService services = new WebService(context);
		PoiListResponse poiListResponse = services.findNearby(location,categoryHandler.getIdSelectedCategory());
		
		if (poiListResponse == null){
			return null;
		}
		if ( poiListResponse.getMessage() == "fault" && poiListResponse.getPois() == null)
		{
			return null;
		}
		
		List<Poi> poiList = poiListResponse.getPois();
		for(Poi poi: poiList){
			arrayPOI.add(poi);
		}
		return arrayPOI;
		}
}
