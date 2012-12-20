package com.geolog;

import java.util.ArrayList;

import java.util.List;

import com.geolog.R;
import com.geolog.dominio.*;
import com.geolog.util.CategoriesAdapter;
import com.geolog.util.ParametersBridge;
import com.geolog.util.PositionOverlay;
import com.geolog.util.UtilDialog;
import com.geolog.web.domain.PoiListResponse;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

/**
 * 
 * Gestione della visualizzazione dei Poi sulla mappa. Vengono aggiunti gli overlay della posizione dell'utente e dei poi ricercati. Inoltre è possibile selezionare le
 * categorie di rcierca da un apposito menu.
 * @author Lorenzo
 * 
 */
public class PoiMapManager extends MapActivity implements ItypeOfViewPoi {

	// Controller della mappa
	private MapController mapController;
	// Lista dei poi
	private ArrayList<Poi> poi;
	// Mappa
	private MapView mapView;
	// Gestore delle categorie
	private CategoriesManager categoriesManager;
	// Stato di apertura del menu delle categorie
	private boolean isExpandedMenucategories;
	// Locazione dell'utente
	private Location mylocation;
	// Overlays della mappa
	private List<Overlay> mapOverlays;
	// Manager delle location
	private LocationManager locationManager;
	// Listenr delle location
	private LocationListener myLocationListener;
	// adattatore delle categorie
	private CategoriesAdapter categoryAdapter;
	// Contesto dell'applicazione
	private Context context;

	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.poi_map_layout);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);

		// Salvo il contesto dell'applicazione
		context = this;

		// Accedo al passaggio di parametri tra attività
		ParametersBridge bridge = ParametersBridge.getInstance();

		// Inzializzo l'array di poi
		poi = new ArrayList<Poi>();

		// Prendo la lista dei poi dai parametri dell'attività
		poi = (ArrayList<Poi>) bridge.getParameter("listaPOI");

		// Prendo la locazione dai parametri dell'attività
		mylocation = (Location) ParametersBridge.getInstance().getParameter(
				"location");

		// Inizilizzo i lgestore delle categorie
		categoriesManager = CategoriesManager.getCategoriesManager();

		// Inizializzo la mappa
		mapView = (MapView) findViewById(R.id.mapView);
		mapView.setClickable(true);
		mapView.setBuiltInZoomControls(true);
		// Inzializzo il mapController
		mapController = mapView.getController();
		mapController.setCenter(createNewGeoPoint(mylocation));
		mapView.setBuiltInZoomControls(true);
		mapController.setZoom(10);
		// Inzializzo gli overlays della mappa
		mapOverlays = mapView.getOverlays();

		// Aggiorno la posizione dell'utente
		updateLocationData(mylocation);

		// Ottengo il riferimento alla listView delle categorie
		ListView listViewCategories = (ListView) findViewById(R.id.listViewCategories);
		// Inzializzo il categoryAdapeter
		categoryAdapter = new CategoriesAdapter(getApplicationContext(),
				categoriesManager.getCategories());
		// Setto le categorie di scelta
		categoriesManager.setSelectionCategory(listViewCategories,
				getApplicationContext(), categoryAdapter);
		// Il menu delle categorie non è inzialmente visibile
		listViewCategories.setVisibility(View.GONE);

		// Inzializzo il location manager
		locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		// Inzializzo il location listner
		myLocationListener = new LocationListener() {

			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub

				// Aggiorno la mia locazione
				mylocation = location;
				// update della location
				updateLocationData(mylocation);

				/*
				 * aggiungiMiaPosizione(mapOverlays,createNewGeoPoint(location));
				 * if (poi != null){ addPOIOverlay(mapOverlays);}
				 */
			}

			public void onProviderDisabled(String arg0) {
				// TODO Auto-generated method stub
			}

			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub
			}
		};

		// Richiedo aggiornamento della posizione
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5,
				1, myLocationListener);

	}

	public void onPause() {
		super.onPause();
		// Rimuovo il location manager dal listner
		locationManager.removeUpdates(myLocationListener);
	}

	public void onResume() {
		super.onResume();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);

		// richiedo l'aggiornamento della posizione
		locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		Location location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location != null) {
			mylocation = location;
			// updateLocationData(location);
		}
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5,
				1, myLocationListener);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// se è stato premuto il bottone del menù, controllo se il menu delle
		// categorie è aperto
		if (keyCode == KeyEvent.KEYCODE_MENU) {

			// Ottengo il riferimento alla lista delel categorie
			ListView listViewCategories = (ListView) findViewById(R.id.listViewCategories);

			// Se il menù è stato aperto, verrà chiuso
			if (isExpandedMenucategories) {

				listViewCategories.setVisibility(View.GONE);
				// aggiorno lo stato del menù
				isExpandedMenucategories = false;

				// Controllo la selezione delle categorie da parte dell'utente
				categoriesManager.checkMenuCategory(categoryAdapter);

				// Ricerco i poi in base alle categorie
				searchPoi(context);

				// Se ho ottenuto dei poi aggiorno la mia locazione
				if (poi != null) {
					updateLocationData(mylocation);
				}

			} else {
				isExpandedMenucategories = true;
				listViewCategories.setVisibility(View.VISIBLE);

			}
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	public void updateLocationData(Location location) {

		// se la locazione è diversa da null, aggiungo la mia posizione sugli
		// overlays della mappa
		if (mylocation != null) {
			addUserPositionToMap(mapOverlays, createNewGeoPoint(mylocation));
			mapController.setCenter(createNewGeoPoint(mylocation));
		}
		// Se la lista dei poi è diversa da null, aggiungo i poi sugli overlays
		// della mappa,altrimenti mostro un messaggio d'errore
		if (poi != null) {
			addPOIOverlay(mapOverlays);
		} else {
			UtilDialog.createBaseToast("impossibile recuperare poi", this)
					.show();
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Aggiunge la posizione dell'utente sulla mappa
	 * 
	 * @param mapOverlays
	 *            la lista degli overlays dell mappa
	 * @param geoPoint
	 *            geolocalizzazione della posizione dell'utente
	 */
	private void addUserPositionToMap(List<Overlay> mapOverlays,
			GeoPoint geoPoint) {
		// Rimuovo tutti gli overlays della mapa
		mapView.getOverlays().clear();

		// Ottengo l'icona che rappresenta la posizione dell'utente
		Drawable drawable = this.getResources().getDrawable(
				R.drawable.user_position_on_map);

		// Creo un nuovo PositionOVerlay
		// PositionOverlay itemizedoverlay = new PositionOverlay(drawable,
		// this,null);
		PositionOverlay itemizedoverlay = new PositionOverlay(drawable, this,
				null, mapOverlays, mapView, mylocation, mapController);
		// creo un nuovo overlay item
		OverlayItem overlayitem = new OverlayItem(geoPoint, "Mi Trvo qui", "");
		// aggiungo il nuovo overlay alla lista degli overlay
		itemizedoverlay.addOverlay(overlayitem);
		// aggiungo l'itemized overlay alla lista degli overlay della mappa
		mapOverlays.add(itemizedoverlay);

		// setto il centro della mappa

		mapController.setCenter(geoPoint);
	}

	/**
	 * Crea un nuovo GeoPoint a partire da una locazione
	 * 
	 * @param location
	 *            locazione
	 * @return GeoPoint il punto GeoPoint della location passata come paramentro
	 */
	private GeoPoint createNewGeoPoint(Location location) {
		double latitude = location.getLatitude();
		double longitude = location.getLongitude();
		int latitudeE6 = (int) Math.floor(latitude * 1.0E6);
		int longitudeE6 = (int) Math.floor(longitude * 1.0E6);
		GeoPoint geoPoint = new GeoPoint(latitudeE6, longitudeE6);
		return geoPoint;
	}

	/**
	 * Aggiunge per ogni poi ricercato, un overlay sulla mappa
	 * 
	 * @param mapOverlays
	 *            lista degli overlays della mappa
	 */
	private void addPOIOverlay(List<Overlay> mapOverlays) {
		// Per ogni poi dell'aray list dei poi, viene creato un nuovo overlay e
		// aggiuinto alla mappa
		for (Poi pois : poi) {
			// Ottengo il geopoint del POI
			GeoPoint point = createNewGeoPoint(pois.getPOILocation());
			// ottengo l'icona rappresentativa del poi
			Drawable drawable = pois.getCategoria()
					.getIconFromResource(context);
			// creo un nuovo itemized overlay
			// PositionOverlay itemizedoverlay = new PositionOverlay(drawable,
			// this,pois);
			PositionOverlay itemizedoverlay = new PositionOverlay(drawable,
					this, pois, mapOverlays, mapView, mylocation, mapController);
			// creo un nuovo overlay item
			OverlayItem overlayitem = new OverlayItem(point, pois.getNome(),
					pois.getDescrizione());
			itemizedoverlay.addOverlay(overlayitem);
			// aggiungo l'overlay item alla mappa
			mapOverlays.add(itemizedoverlay);
		}

	}

	/**
	 * Ricerca dei poi in base alle categorie scelte dall'utente.
	 * 
	 * @param context
	 *            contesto dell'attività che richiede il servizio
	 */
	@Override
	public void searchPoi(final Context context) {
		// TODO Auto-generated method stub
		AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
			// Definisco una nuova progress Dialog
			ProgressDialog dialog;

			@Override
			protected void onPreExecute() {
				// Richiedo la posizione dell'utente
				locationManager.requestLocationUpdates(
						LocationManager.GPS_PROVIDER, 5, 1, myLocationListener);

				// Mostro la barra di progresso
				dialog = ProgressDialog.show(context, "Attendere...",
						"Ricerca poi in corso...");

			}

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub

				// Chiedo al gestore dei poi di ricercare i poi
				PoiListResponse response = PoiManager.searchPoi(mylocation,
						context);

				// Se la risposta è nulla o si è verificato un errore, termino
				// con un messaggio di errore, altrimenti aggiorno la lista dei
				// Poi
				if (response == null || response.getStatus() != 200) {
					dialog.dismiss();
					UtilDialog.alertDialog(context,
							"impossibile recuperare i poi").show();
				} else {
					// Prendo la lista dei poi dalla risposta del Poi manager
					poi = (ArrayList<Poi>) response.getPois();
					// aggiungo i poi trovati e la mia locazione al gestore
					// della visualizzazione
					PoiViewManager view = new PoiViewManager(context);
					view.setPois(poi);
					view.setMylocation(mylocation);

					// se non sono stati trovati i poi termino con un messaggio
					// d'errore, altrimenti eseguo lupdate della locazione
					if (poi.size() < 0) {
						dialog.dismiss();
						UtilDialog.createBaseToast("nessun poi trovato",
								context);
					} else {
						updateLocationData(mylocation);
					}

				}
				return null;
			}

			protected void onPostExecute(String result) {
				dialog.dismiss();

			}

		};
		task.execute((Void[]) null);

	}

}
