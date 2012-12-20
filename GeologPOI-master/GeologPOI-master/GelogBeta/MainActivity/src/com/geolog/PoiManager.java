package com.geolog;



import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.location.Location;

import com.geolog.R;
import com.geolog.dominio.Category;
import com.geolog.dominio.Poi;
import com.geolog.dominio.Resource;
import com.geolog.dominio.ResourceType;
import com.geolog.dominio.Suggestion;
import com.geolog.web.Services;
import com.geolog.web.domain.ConfrimResponse;
import com.geolog.web.domain.PoiListResponse;



/**
 * @author Lorenzo
 *
 * La classe si occupa della gestione dei poi. In particolare modo, gestisce tutte le richiesti di: segnalazione di un poi, aggiunta di un poi, creazione di un nuovo poi,
 * upload delle risorse relative ad un poi e della ricerca di punti di interesse.
 * 
 * 
 */
public class PoiManager {

	
	/**
	 * Metodo che crea un nuovo punto di interesse
	 * 
	 * 
	 * @param location posizione del poi che deve essere creato
	 * @param idCategory identificativo della categoria a cui appartiene il poi
	 * @param nome nome del poi
	 * @param descrizione descrizione del poi
	 * @param date data di creazione del poi
	 * @return Poi ritorna un nuovo punto di interesse
	 */
	public static Poi createNewPoi(Location location,int idCategory,String nome,String descrizione,Date date)
	{
		//Ottengo la categoria a partire dall'id
		Category category = CategoriesManager.getCategoriesManager().getCategoryFromId(idCategory);
		
		//Creo un nuovo poi con le informazioni passate come parametro
		Poi newPOI = new Poi(category, nome, descrizione, date, location);
		
		//Restituisco il poi creato
		return newPOI;
	}

	/**
	 *  Metodo che invia una nuova segnalazione al servizio Web.
	 *  
	 * @param poiBase poi su cui è stata fatta la segnalazione
	 * @param descrizione descrizione della segnalazione
	 * @param date data di creazione della segnalazione
	 * @param context contesto dell'applicazione
	 * @param user utente che ha effeuttato la segnalazione
	 *
	 * 
	 * @return ConfirmResponse risposta di conferma dell'aggiunta della segnalazione
	 */
	public static ConfrimResponse suggestionPoi(Poi poiBase,String descrizione,Date date,String user,Context context){
		
		//Creo una nuova segnalazione
		Suggestion suggestion = new Suggestion(0, user, poiBase, date,descrizione);
		
		//Inzializzo il servizio web
		Services service = new Services();
		
		//Ottengo la risposta del servizio web
		ConfrimResponse response =  service.addSuggestion(suggestion, context);
		
		//Resitituisco la risposta
		return response;	
	}

	/**
	 * Metodo che aggiunge un poi al sistema attraverso il servizio web.
	 * @param poi poi che deve essere aggiunto al sistma
	 * @param context contesto dell'attività che richiama il servizio
	 * @param user utente che aggiunge il poi
	 * 
	 * @return ConfrimResponse risposta di conferma da parte del servizio web per l'aggiunta di un poi
	 * 
	 */
	public static ConfrimResponse addPOI(Poi poi, Context context,String user)
	{
		Services service = new Services();
		ConfrimResponse confirmResponse = service.addPOI(poi, context, user);
		return confirmResponse;
	}

	
	/**
	 * 
	 * Metodo che provvede all'upload di una risorsa(immagine,audio,video,ecc) per un poi specifico.
	 *  
	 * @param idPOI identificatio del poi a cui si sta aggiungendo uuna risorsa
	 * @param context contesto dell'attività che richiama il servizio
	 * @param resource risorsa da aggiungere al poi
	 * @param typeRespurce tipo di risorsa
	 * 
	 * @return ConfrimResponse risposta di conferma da parte del servizio web per l'upload della risorsa
	 */
	public static ConfrimResponse uploadPoiResource(int idPOI,Context context,byte[] resource,String typeRespurce)
	{	
		//Inzializzo il servizio Web
		Services service = new Services();
		
		//Ottengo la risposta per l'upload della risorsa
		ConfrimResponse responseWeb =  service.uploadResource(context, idPOI, typeRespurce, resource);
		
		//restituisco la risposta ottenuta
		return responseWeb;
	
	}


	/**
	 * 
	 * Metodo che richiede tutti i poi di una,o più categorie, situati vicino alla posizione dell'utente
	 * 
	 * @param location location in cui si trova l'utente
	 * @param context contesto dell'attività che richiede il servizio
	 * 
	 * 
	 * 
	 * @return PoiListresponse 
	 */
	public static PoiListResponse searchPoi(Location location,Context context) 
	{
		CategoriesManager categoryHandler = CategoriesManager.getCategoriesManager();
		
	
		

		

		Location location2 = new Location("prova");
		location2.setLatitude(42.703422);
		location2.setLongitude(20.690868);
		ArrayList<Poi> arrayPOI = new ArrayList<Poi>();
    	Poi newPOI = new Poi(categoryHandler.getCategories().get(0),"Ospedale PTV", "ospedale qui viicno",1,null, location2, R.drawable.ptv);
    	Location location1 = new Location("aaaa");
		location1.setLatitude(37.422006);
		location1.setLongitude(32.084095);
    	Poi newPOI2 = new Poi(new Category("risto",1,R.drawable.food_icon),"sushi", "schifo",1,null, location1, R.drawable.kfc);

    	Resource resource = new Resource(0, "http://images.movieplayer.it/2009/04/29/megan-fox-e-ciliege-114949.jpg", "immagine di meganFox", newPOI, new ResourceType("image/jpeg"));
    	Set<Resource> res =  new HashSet<Resource>();
    	res.add(resource);
    	newPOI.setResources(res);
    	arrayPOI.add(newPOI);
    	arrayPOI.add(newPOI2);
		Services service = new Services();
		PoiListResponse response =new PoiListResponse();
		response.setPois(arrayPOI);
		response.setStatus(200);
		return response;
	}
}
