package com.geolog;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.geolog.dominio.Category;
import com.geolog.dominio.Poi;
import com.geolog.dominio.Resource;
import com.geolog.dominio.ResourceType;
import com.geolog.dominio.Suggestion;
import com.geolog.util.ResourcesHandler;
import com.geolog.web.FindNearbyService;
import com.geolog.web.Services;
import com.geolog.web.WebService;
import com.geolog.web.domain.AddPOIResponse;
import com.geolog.web.domain.CategoryListResponse;
import com.geolog.web.domain.ConfrimResponse;
import com.geolog.web.domain.PoiListResponse;
import com.geolog.web.domain.SuggestionResponse;


public class HandlerPOI {

	
	public static Poi creaPOI(Location location,int idCategory,String nome,String descrizione,Date date)
	{
		
		Category category = CategoriesHandler.getGestoreCategorie().getCategoryFromId(idCategory);
		Poi newPOI = new Poi(category, nome, descrizione, date, location);
		return newPOI;
	}
	
	public static ConfrimResponse segnalaPOI(Poi poiBase,String descrizione,Date date,Context context){
		
		Suggestion suggestion = new Suggestion(0, "io", poiBase, date,descrizione);
		Services service = new Services();
		ConfrimResponse response =  service.addSuggestion(suggestion, context);
		return response;	
	}
	
	public static ConfrimResponse addPOI(Poi poi, Context context,String user)
	{
		Services service = new Services();
		ConfrimResponse confirmResponse = service.addPOI(poi, context, user);
		return confirmResponse;
	}
	
	public static ConfrimResponse uploadPoiResource(int idPOI,Context context,byte[] resource,String typeRespurce)
	{	Services service = new Services();
		ConfrimResponse responseWeb =  service.uploadResource(context, idPOI, typeRespurce, resource);
		if (responseWeb == null || responseWeb.getStatus() != 200)
			return null;
		else {
			return responseWeb;
		}
	}
	

	public static PoiListResponse cercaPOI(Location location,Context context) 
	{
		/*CategoryHandler categoryHandler = CategoryHandler.getGestoreCategorie();
		Services services = new Services();
		PoiListResponse poiListResponse = (PoiListResponse) services.findNearby(location,categoryHandler.getIdSelectedCategory(), context);
		//chiama il servizio del web service
		ArrayList<Poi> arrayPOI = new ArrayList<Poi>();
		
		if (poiListResponse == null){
			return null;
		}
		if ( poiListResponse.getStatus() != 200)
		{
			return null;
		}
		
		List<Poi> poiList = poiListResponse.getPois();
		for(Poi poi: poiList){
			arrayPOI.add(poi);
		}
		
		Location location2 = new Location("prova");
		location2.setLatitude(42.703422);
		location2.setLongitude(20.690868);
		ArrayList<Poi> arrayPOI = new ArrayList<Poi>();
    	Poi newPOI = new Poi(categoryHandler.getCategorie().get(0),"Ospedale PTV", "ospedale qui viicno",1,null, location2, R.drawable.ptv);
    	Location location1 = new Location("aaaa");
		location1.setLatitude(37.422006);
		location1.setLongitude(32.084095);
    	Poi newPOI2 = new Poi(new Category("risto",1,R.drawable.food_icon),"sushi", "schifo",1,null, location1, R.drawable.kfc);
    	
    	Resource resource = new Resource(0, "http://images.movieplayer.it/2009/04/29/megan-fox-e-ciliege-114949.jpg", "immagine di meganFox", newPOI, new ResourceType("image/jpeg"));
    	Set<Resource> res =  new HashSet<Resource>();
    	res.add(resource);
    	newPOI.setResources(res);
    	arrayPOI.add(newPOI);
    	arrayPOI.add(newPOI2);*/
		Services service = new Services();
		PoiListResponse response =service.findNearby(location, "0", context);
		
		return response;
		}
}
