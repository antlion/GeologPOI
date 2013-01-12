package geolog.web;


import geolog.managers.ResourcesManager;
import geolog.util.web.Services;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;
import org.json.simple.JSONObject;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.util.Log;

import com.geolog.dominio.Poi;
import com.geolog.dominio.Suggestion;
import com.geolog.web.domain.CategoryListResponse;
import com.geolog.web.domain.ConfrimResponse;
import com.geolog.web.domain.PoiListResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Interfacciamento con il servizio web.Questa classe esponde i metodi per
 * raggiungere i servizi esposti dal webService.Viene usato il pro tocollo soap
 * per gestire le richieste
 * 
 * @author Lorenzo
 * 
 */
public class WebService {

	// Indirizzo di acesso del servizo web
	private static final String WEB_SERVICE_URL = "http://192.168.0.105:8080/GeologWeb/services/WS";

	// Servizi offerti dal Servizioweb
	/**
	 * @uml.property  name="webServices"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private Services webServices;

	/**
	 * Costruttore della classe
	 */
	public WebService() {
		// creo l'accesso ai servizi
		webServices = new Services();
		// imposto l'indirizzo per accedere ai servizi
		webServices.setUrl(WEB_SERVICE_URL);

	}

	/**
	 * Richiesta al web server di ricercare i poi nelle vicinanze dell'utente. I
	 * poi vengono ricercati in base alle categorie scelte dall'utente
	 * 
	 * @param location
	 *            location del'utente
	 * @param id
	 *            id delle categorie di ricerca
	 * @param context
	 *            contesto dell'attivita
	 * @return riposta del web server
	 */
	public PoiListResponse findNearby(final Location location, String id,
			final Context context) {
		// Creo una nuova risposta per il servizio
		PoiListResponse response = null;
		try {
			// richiedo al servizo di ricercare i poi
			String responseWeb = webServices.findNearby(location.getLatitude(),
					location.getLongitude(), 0);
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			response = gson.fromJson(responseWeb, PoiListResponse.class);
			return response;
		} catch (Exception e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}
		return response;

	}

	/**
	 * Richiesta di inserimento di una segnalazione al servizioe web.
	 * 
	 * @param suggestion
	 *            segnalazione di un poi
	 * @param context
	 *            contesto dell'attività
	 * @return ConfirmResponse risposta di conferma del web service
	 */
	@SuppressWarnings("unchecked")
	public ConfrimResponse addSuggestion(Suggestion suggestion, Context context) {
		// Creo una nuova risposta
		ConfrimResponse response = null;

		// creo gli oggeti json
		JSONObject result = new JSONObject();
		JSONObject categoria = new JSONObject();
		categoria.put("id", suggestion.getPoi().getCategory().getId());
		JSONObject poi = new JSONObject();
		poi.put("nome", suggestion.getPoi().getNome());
		poi.put("descrizione", suggestion.getPoi().getDescrizione());
		poi.put("id", suggestion.getPoi().getIdTipo());
		poi.put("latitude", suggestion.getPoi().getLatitude());
		poi.put("longitude", suggestion.getPoi().getLongitude());
		poi.put("category", categoria);
		result.put("id", suggestion.getId());
		result.put("user", suggestion.getUser());

		result.put("creationDate", suggestion.getCreationDate());
		result.put("description", suggestion.getDescription());
		result.put("poi", poi);

		try {
			// Invio la segnlazione al web service
			String responseWeb = webServices.reportPoi(suggestion.getPoi()
					.getIdTipo(), suggestion.getDescription(), suggestion
					.getUser());
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			response = gson.fromJson(responseWeb, ConfrimResponse.class);
			// ritorno la risposta
			return response;
		} catch (Exception e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}
		return response;
	}

	/**
	 * Scarica la risorsa a partire dall'url e la resituisce sotto forma di
	 * drawable
	 * 
	 * @param url
	 *            url della risorsa da scaricare
	 * @param context
	 *            contesto dell'atività
	 * @return Drawable la risorsa rappresentata da un drawable
	 */
	public Drawable downloadResource(String url, Context context) {
		// Percorso in cui salvare la risorsa
		String path = (context.getFilesDir().toString());
		try {
			// Creo un nuovo url
			URL web = new URL(url);

			// Crea un nuovo file con il nome della risorsa
			File file = new File(path, ResourcesManager.getNameFileFromUrl(url));

			// Apre una nuova connessione
			URLConnection ucon = web.openConnection();

			// Ottiene l'inputstream
			InputStream is = ucon.getInputStream();
			// Cotruisco il buffer per leggere i dati
			BufferedInputStream bis = new BufferedInputStream(is);

			// Definisco un buffer
			ByteArrayBuffer baf = new ByteArrayBuffer(50);
			int current = 0;
			// fin tanto che ci sono dati da legere il buffer viene riempito
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}

			// Creo un OutputStream
			FileOutputStream fos = new FileOutputStream(file);
			// Scrivi i dati nel file
			fos.write(baf.toByteArray());
			// chiudo la scrittura
			fos.close();

			// Creo un nuvoo Drawable a partire dal file scaricato
			Drawable drawableResource = new BitmapDrawable(
					context.getResources(), path + "//"
							+ ResourcesManager.getNameFileFromUrl(url));
			// restituisco il drawable
			return drawableResource;
		}

		catch (IOException e) {
			Log.d("ImageManager", "Error: " + e);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Upoload di una resource relativa al sistema attraverso il servizioWeb
	 * 
	 * @param context
	 *            contesto dell'attività
	 * @param idPOI
	 *            id del poi a cui si riferisce la risorsa
	 * @param typeResource
	 *            tipo di risorsa
	 * @param data
	 *            data di upload
	 * @return ConfrimResponse risposta del server
	 */
	public ConfrimResponse uploadResource(final Context context,
			final int idPOI, final String typeResource, final byte[] data) {
		// Creo la risposta del servizio web
		ConfrimResponse confirm = null;

		try {
			// Upload della risorsa atraverso il servizio web
			String response = webServices.upload(idPOI, typeResource, data);
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			confirm = gson.fromJson(response, ConfrimResponse.class);

			// Ritorno della risposta
			return confirm;

		} catch (Exception e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}
		return confirm;
	}

	/**
	 * Aggiunge un nuovo poi al sistema
	 * 
	 * @param poi
	 *            poi da aggiungere al sistema
	 * @param context
	 *            contesto dell'applicazione
	 * @param user
	 *            utente che aggiunge il poi
	 * @return ConfrimResponse riposta del webService
	 */
	@SuppressWarnings("unchecked")
	public ConfrimResponse addPOI(Poi poi, Context context, String user) {
		// Creo una nuova riposta del servizio web
		ConfrimResponse confirmResponse = null;
		final JSONObject result = new JSONObject();

		result.put("name", poi.getNome());
		result.put("longitude", poi.getLongitude());
		result.put("latitude", poi.getLongitude());
		result.put("description", poi.getDescrizione());
		// attenzione questo valore
		result.put("category_id", 8);
		try {

			// Invio il poi al sistema attraverso il servizioWEb
			String response = webServices.addPoi(result.toJSONString(), user);
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			confirmResponse = gson.fromJson(response, ConfrimResponse.class);

			// ritorno la risposta
			return confirmResponse;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return confirmResponse;
	}

	/**
	 * Richiesta delle categorie di ricerca poi al servizio web.
	 * 
	 * @return CategoryListResponse la risposta del server alla richiesta
	 */
	public CategoryListResponse getListCategory() {
		// Creo una nuova riposta del web server
		CategoryListResponse responseWeb = null;

		try {
			// Setto il timeOut per la richiesta
			webServices.setTimeOut(7);
			
			// Richedo la lista delle categorie
			String response = webServices.listCategories();
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			responseWeb = gson.fromJson(response, CategoryListResponse.class);
			// ritorno la risposta
			return responseWeb;

		} catch (Exception e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}
		return responseWeb;
	}
}
