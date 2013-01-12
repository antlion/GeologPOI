package geolog.activities;

import geolog.managers.CategoriesManager;
import geolog.managers.PoiManager;
import geolog.poi.visualization.ItypeOfViewPoi;
import geolog.poi.visualization.PoiViewManager;

import geolog.util.ParametersBridge;
import geolog.util.ui.MenuCategory;
import geolog.util.ui.PositionOverlay;
import geolog.util.ui.UtilDialog;

import java.util.ArrayList;

import java.util.List;

import com.geolog.activity.R;
import com.geolog.dominio.*;
import com.geolog.web.domain.PoiListResponse;

import android.app.AlertDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

/**
 * 
 * Gestione della visualizzazione dei Poi sulla mappa. Vengono aggiunti gli
 * overlay della posizione dell'utente e dei poi ricercati. Inoltre è possibile
 * selezionare le categorie di rcierca da un apposito menu.
 * 
 * @author Lorenzo
 * 
 */
public class PoiMapManager extends MapActivity implements ItypeOfViewPoi,
		OnClickListener {

	// Controller della mappa
	/**
	 * @uml.property name="mapController"
	 * @uml.associationEnd
	 */
	private MapController mapController;
	// Lista dei poi
	/**
	 * @uml.property name="poi"
	 */
	private ArrayList<Poi> poi;
	// Mappa
	/**
	 * @uml.property name="mapView"
	 * @uml.associationEnd
	 */
	private MapView mapView;

	// Locazione dell'utente
	/**
	 * @uml.property name="mylocation"
	 * @uml.associationEnd
	 */
	private Location mylocation;
	// Overlays della mappa
	/**
	 * @uml.property name="mapOverlays"
	 */
	private List<Overlay> mapOverlays;
	// Manager delle location
	/**
	 * @uml.property name="locationManager"
	 * @uml.associationEnd
	 */
	private LocationManager locationManager;
	// Listenr delle location
	/**
	 * @uml.property name="myLocationListener"
	 * @uml.associationEnd
	 */
	private LocationListener myLocationListener;
	// Contesto dell'applicazione
	/**
	 * @uml.property name="context"
	 * @uml.associationEnd
	 */
	private Context context;
	// Menu delle categorie
	/**
	 * @uml.property name="menuCategory"
	 * @uml.associationEnd
	 */
	private MenuCategory menuCategory;
	// CheckBox per la vista da satellite della mappa
	/**
	 * @uml.property name="satelliteCheckBox"
	 * @uml.associationEnd
	 */
	private CheckBox satelliteCheckBox;
	// CheckBox per la street View della mappa
	/**
	 * @uml.property name="streetViewCheckBox"
	 * @uml.associationEnd
	 */
	private CheckBox streetViewCheckBox;
	// CheckBox per la vista del traffico della mappa
	/**
	 * @uml.property name="trrafficCheckBox"
	 * @uml.associationEnd
	 */
	private CheckBox trrafficCheckBox;

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

		// Inizializzo la mappa
		mapView = (MapView) findViewById(R.id.mapView);
		mapView.setClickable(true);
		mapView.setBuiltInZoomControls(true);
		// Inzializzo il mapController
		mapController = mapView.getController();

		mapView.setBuiltInZoomControls(true);
		mapController.setZoom(8);
		
		// Inzializzo gli overlays della mappa
		mapOverlays = mapView.getOverlays();

		// Aggiorno la posizione dell'utente
		updateLocationData(mylocation);

		// Inzializzo il menu delle categorie
		menuCategory = new MenuCategory(false,
				(ListView) findViewById(R.id.listViewCategories),
				CategoriesManager.getCategoriesManager(), context);
		// Il menù delle categorie non + inizialmente visibile
		menuCategory.setVisibilityListCategory(false);

		// Configuro i buttons visibili sull'iterfaccia grafica
		// Button per la ricerca
		ImageButton searchButton = (ImageButton) findViewById(R.id.searchButton);
		searchButton.setOnClickListener(this);
		// Button per il men delle categorie
		ImageButton categoryButton = (ImageButton) findViewById(R.id.categoryButton);
		categoryButton.setOnClickListener(this);
		// Button per i livelli della mappa
		ImageButton levelsMap = (ImageButton) findViewById(R.id.levelsButton);
		levelsMap.setOnClickListener(this);
		// Button per l'update
		ImageButton updateButton = (ImageButton) findViewById(R.id.addPoiButton);
		updateButton.setOnClickListener(this);

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

	@SuppressWarnings("unchecked")
	public void onResume() {
		super.onResume();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);

		// Aggiorno il menu delle categorie
		menuCategory = new MenuCategory(false,
				(ListView) findViewById(R.id.listViewCategories),
				CategoriesManager.getCategoriesManager(), context);

		// Recupero la lista dei poi dai parametri passati all'attività
		poi = ((ArrayList<Poi>) ParametersBridge.getInstance().getParameter(
				"listaPOI"));

		// Aggiorno la posizione dell'utente
		mylocation = (Location) ParametersBridge.getInstance().getParameter(
				"location");

		// aggiorno la lista deiPOI
		updateLocationData(mylocation);

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
		OverlayItem overlayitem = new OverlayItem(geoPoint, "Mi Trovo qui", "");
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
			Drawable drawable = pois.getCategory().getIconFromResource(context);
			// creo un nuovo itemized overlay

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
			private PoiListResponse response;

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

				// Controllo che ci sia almeno una categoria di ricerca
				if (CategoriesManager.getCategoriesManager().getCategories()
						.size() > 0) {
					// Chiedo al gestore dei poi di ricercare i poi
					response = PoiManager.searchPoi(mylocation, context,
							CategoriesManager.getCategoriesManager()
									.getCategoriesSelected());

				}
				return null;
			}

			protected void onPostExecute(String result) {
				dialog.dismiss();
				// Se la risposta è nulla o si è verificato un errore,
				// termino
				// con un messaggio di errore, altrimenti aggiorno la lista
				// dei
				// Poi
				if (response == null || response.getStatus() != 200) {
					dialog.dismiss();
					UtilDialog.alertDialog(context,
							"impossibile recuperare i poi").show();
				} else {
					// Prendo la lista dei poi dalla risposta del Poi
					// manager
					poi = (ArrayList<Poi>) response.getPois();
					// aggiungo i poi trovati e la mia locazione al gestore
					// della visualizzazione
					PoiViewManager view = new PoiViewManager(context);
					view.setPois(poi);
					view.setMylocation(mylocation);
					ParametersBridge.getInstance()
							.addParameter("listaPOI", poi);
					ParametersBridge.getInstance().addParameter("location",
							mylocation);

					// se non sono stati trovati i poi termino con un
					// messaggio
					// d'errore, altrimenti eseguo lupdate della locazione
					if (poi.size() < 0) {
						dialog.dismiss();
						UtilDialog.createBaseToast("nessun poi trovato",
								context);
					} else {

						updateLocationData(mylocation);
					}

				}
			}

		};
		task.execute((Void[]) null);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		// Se viene cliccato il bottone di aggiungi poi, viene avviatà
		// l'attività di aggiungi poi.
		if (v.getId() == R.id.addPoiButton) {
			Intent intent = new Intent(context, AddPoiActivity.class);
			context.startActivity(intent);
		}
		// Se è stato premuto il bottone di ricerca effettuo la ricerca dei poi
		if (v.getId() == R.id.searchButton) {
			searchPoi(context);

		}
		// Se è stato premuto il bottone delle categorie, apro il menu delle
		// categorie
		if (v.getId() == R.id.categoryButton) {

			ImageButton searchButton = (ImageButton) findViewById(R.id.searchButton);
			ImageButton levelsMap = (ImageButton) findViewById(R.id.levelsButton);
			ImageButton updateButton = (ImageButton) findViewById(R.id.addPoiButton);
			// Se il menù è stato aperto, verrà chiuso
			if (menuCategory.checkMenuCategory()) {
				searchButton.setVisibility(View.VISIBLE);
				updateButton.setVisibility(View.VISIBLE);
				levelsMap.setVisibility(View.VISIBLE);

			} else {
				searchButton.setVisibility(View.GONE);
				updateButton.setVisibility(View.GONE);
				levelsMap.setVisibility(View.GONE);

			}
		}
		// Se è stato premuto il bottone dei livelli,visualizzo un dialogo per i
		// livelli
		if (v.getId() == R.id.levelsButton) {
			// Creo un nuovo dialogo, dove vengono visualizzate le checkBox per
			// la visualizzazione
			// dell view della mappa
			AlertDialog.Builder adb = new AlertDialog.Builder(this);
			LayoutInflater adbInflater = LayoutInflater.from(this);
			View eulaLayout = adbInflater.inflate(R.layout.map_levels_layout,
					null);
			adb.setView(eulaLayout);
			satelliteCheckBox = (CheckBox) eulaLayout
					.findViewById(R.id.satelliteCheckBox);
			Typeface myTypeface = Typeface.createFromAsset(getAssets(),
					"fonts/bebe.otf");

			satelliteCheckBox.setTypeface(myTypeface);
			satelliteCheckBox
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							// TODO Auto-generated method stub
							if (streetViewCheckBox.isChecked()) {
								streetViewCheckBox.setChecked(false);
								mapView.setSatellite(isChecked);

							}
							mapView.setSatellite(isChecked);
							satelliteCheckBox.setChecked(isChecked);
						}
					});
			streetViewCheckBox = (CheckBox) eulaLayout
					.findViewById(R.id.streetViewCheckBox);
			streetViewCheckBox.setTypeface(myTypeface);
			streetViewCheckBox
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							// TODO Auto-generated method stub
							if (satelliteCheckBox.isChecked()) {
								mapView.setSatellite(false);
								satelliteCheckBox.setChecked(false);
								mapView.setStreetView(isChecked);
								streetViewCheckBox.setChecked(isChecked);

							} else {
								mapView.setStreetView(isChecked);
								streetViewCheckBox.setChecked(isChecked);

							}
						}
					});
			trrafficCheckBox = (CheckBox) eulaLayout
					.findViewById(R.id.trafficCheckBox);
			trrafficCheckBox.setTypeface(myTypeface);
			trrafficCheckBox
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							// TODO Auto-generated method stub
							mapView.setTraffic(isChecked);
							trrafficCheckBox.setChecked(isChecked);

						}
					});
			adb.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

						}
					});

			adb.show();
			if (mapView.isStreetView() == false)
				streetViewCheckBox.setChecked(true);
			if (mapView.isSatellite())
				satelliteCheckBox.setChecked(true);
			if (mapView.isTraffic())
				trrafficCheckBox.setChecked(true);
		}
	}

}
