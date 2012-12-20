package com.geolog;

import geolog.util.ParametersBridge;

import java.util.ArrayList;

import com.geolog.ar.PoiAugmentedRealityManager;
import com.geolog.dominio.Poi;

import android.content.Context;
import android.content.Intent;
import android.location.Location;

/**
 * Implementazione del patter Factory. Utilizzo il pattern per la creazione
 * delle visulizzazione dei poi.
 * 
 * @author Lorenzo
 * 
 */
public class PoiViewFactory {

	/**
	 * Creazione dell'attività corrispondente al tipo di visulizzazione che si
	 * vuole ottenere.
	 * 
	 * @param typeOfView
	 *            tipo di visulizzazione richiesta
	 * @param context
	 *            contesto dell'attività che richiede l'avvio della nuova
	 *            visualizzazione
	 * @param Location
	 *            location dell'utente
	 * @param pois
	 *            arrya di poi
	 * @return Intent l'attività corrispondente alla visualizzazione richiesta
	 */
	public static Intent getView(String typeOfView, Context context,
			Location Location, ArrayList<Poi> pois) {
		// Se viene richiesta la mappa
		if (typeOfView.equals("Map")) {
			Intent intent = new Intent(context, PoiMapManager.class);
			ParametersBridge.getInstance().addParameter("listaPOI", pois);
			ParametersBridge.getInstance().addParameter("location", Location);
			return intent;
		}
		// Se viene richesta la lista
		if (typeOfView.equals("List")) {
			Intent intent = new Intent(context, PoiListManager.class);
			ParametersBridge.getInstance().addParameter("listaPOI", pois);
			ParametersBridge.getInstance().addParameter("location", Location);
			return intent;
		}
		// Se viene richesta la realtà aumentata
		if (typeOfView.equals("Ar")) {
			Intent intent = new Intent(context, PoiAugmentedRealityManager.class);
			ParametersBridge.getInstance().addParameter("listaPOI", pois);
			ParametersBridge.getInstance().addParameter("location", Location);
			return intent;
		}
		// altrimenti ritorna null
		return null;
	}
}
