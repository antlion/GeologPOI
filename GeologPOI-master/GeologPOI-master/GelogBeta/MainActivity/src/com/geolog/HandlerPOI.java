package com.geolog;

import java.util.ArrayList;
import java.util.Date;
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
import com.geolog.web.FindNearbyService;
import com.geolog.web.Services;
import com.geolog.web.WebService;
import com.geolog.web.domain.AddPOIResponse;
import com.geolog.web.domain.CategoryListResponse;
import com.geolog.web.domain.PoiListResponse;
import com.geolog.web.domain.SuggestionResponse;


public class HandlerPOI {

	
	public static Poi creaPOI(Location location,Category cateogry,String nome,String descrizione,int idTipo,Date date)
	{
		Poi newPOI = new Poi(cateogry, nome, descrizione, idTipo, date, location, 0);
		return newPOI;
	}
	
	public static boolean segnalaPOI(Poi poiBase,String descrizione,Date date,Context context){
		
		Suggestion suggestion = new Suggestion(0, "io", poiBase, date,descrizione);
		Services service = new Services();
		SuggestionResponse response = (SuggestionResponse) service.addSuggestion(suggestion, context);
		if(response == null)
			return false;
		if(response.getStatus() != 200 )
			return false;
		
		return true;
		
		
		//Suggestion suggestionPOI = new Suggestion();
	
	}
	
	public static boolean addPOI(Poi poi, Context context)
	{
		Services service = new Services();
		
		AddPOIResponse poiResponse =(AddPOIResponse) service.addPOI(poi, context);
		if (poiResponse == null)
			return false;
		if(poiResponse.getStatus() != 200)
			return false;
		return true;
	}
	
	public static List<Category> getListCategory(Context context)
	{
		Services services = new Services();
		CategoryListResponse response = (CategoryListResponse) services.getListCategory(context);
		if ( response == null)
			return null;
		if ( response.getStatus() != 200 )
			return null;
		return response.getCategory();
					
	}
	public static ArrayList<Poi> cercaPOI(Location location,Context context) 
	{
		CategoryHandler categoryHandler = new CategoryHandler();
		/*Services services = new Services();
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
		}*/
		
		Location location2 = new Location("prova");
		location2.setLatitude(42.703422);
		location2.setLongitude(20.690868);
		ArrayList<Poi> arrayPOI = new ArrayList<Poi>();
    	Poi newPOI = new Poi(new Category("Emergenza",1,R.drawable.croce_rossa),"Ospedale PTV", "ospedale qui viicno",1,null, location2, R.drawable.ptv);
    	Location location1 = new Location("aaaa");
		location1.setLatitude(37.422006);
		location1.setLongitude(32.084095);
    	Poi newPOI2 = new Poi(new Category("risto",1,R.drawable.food_icon),"sushi", "schifo",1,null, location1, R.drawable.kfc);
    	
    	Resource resource = new Resource(0, "http://www.direttanews.it/wp-content/uploads/megan-fox-544.jpg", "immagine di meganFox", newPOI, new ResourceType("image/jpeg"));
    	List<Resource> res =  new ArrayList<Resource>();
    	res.add(resource);
    	newPOI.setResources(res);
    	arrayPOI.add(newPOI);
    	arrayPOI.add(newPOI2);
		
		return arrayPOI;
		}
}
