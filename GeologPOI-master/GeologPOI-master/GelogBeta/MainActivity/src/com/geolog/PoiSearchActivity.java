package com.geolog;


import geolog.util.UtilDialog;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;


import android.widget.TabHost;
import android.widget.TabHost.TabSpec;


import com.geolog.dominio.Poi;
import com.geolog.web.domain.PoiListResponse;

/**
 * Attività per la ricerca dei punti di interesse. Vengono create le tab che rappresentano le varie visualizzazioni dei poi
 * se il gps è attivo vengono ricercati i poi e passate alle varie visualizzazioni
 * 
 * @author Lorenzo
 * 
 */
public class PoiSearchActivity extends TabActivity {

	// ArrayList dei poi ricercati
	private ArrayList<Poi> pois;

	// Gestore visualizzazione poi
	private PoiViewManager viewPoiManager;

	// Gestore delle locazioni
	private LocationManager locationManager;

	// Listenr delle locazione
	private LocationListener myLocationListener;

	// Locazione dell'utente
	private Location mylocation;

	// Stato della locazione dell'utente
	private boolean hasLocation;

	// Contesto dell'applicazione
	private Context context;

	// Stato delle tabelle
	private boolean setTab;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.poi_search_layout);
		// All'inizio le tab non sono state visulizzate
		setTab = false;

		// Salvo il contesto dell'applicazione
		context = this;

		// Inizializzo la locazione dell'utente
		mylocation = null;

		// Aggiorno lo stato della locazione
		hasLocation = false;

		// Inizalizzo l'array di poi
		pois = new ArrayList<Poi>();

		// Inzializzo il gestore delle visuallizzazione dei poi
		viewPoiManager = new PoiViewManager(this);

		// Inizializzo il locationManager
		locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		// Inzializzo il listner dell locazioni
		myLocationListener = new LocationListener() {
			public void onStatusChanged(String provider, int status,
					Bundle extras) {

			}

			public void onProviderEnabled(String provider) {

			}

			public void onProviderDisabled(String provider) {

			}

			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				// Aggiorno lo stato della locazione dell'utente
				hasLocation = true;
				// aggiorno la locazione dell'utente
				mylocation = location;

			}
		};

		// se il provider gps non è supportato dal device, viene resituito un
		// messaggio d'errore e viene terminata l'attività
		if (locationManager.getProvider(LocationManager.GPS_PROVIDER) == null) {
			UtilDialog.createAlertNoGps(context).show();
			finish();
		}

		// se il gps è abilitato si procede alla ricerca dei punti di
		// interesse,altrimenti viene richiesto di attivare il gps
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) == true) {
			searchPOI(this);

		} else {
			UtilDialog.createAlertNoGps(this).show();
			finish();
		}
	}

	public void onPause() {
		super.onPause();
		// metto in pausa il listner dell location
		LocationManager locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		locationManager.removeUpdates(myLocationListener);
	}

	public void onResume() {
		super.onResume();
		// richiedo l'aggiornamento della posizione dell'utente
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
				&& locationManager.getProvider(LocationManager.GPS_PROVIDER) != null) {
			Location location = locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (location != null) {
				mylocation = location;

			}
		}

	}

	/**
	 * Crea le tabelle che rappresentano le varie visualizzazioni dei poi : su
	 * lista,su mappa,su realtà aumentata
	 * 
	 */
	private void setTab() {
		// Recupero il riferimento all'TabHost
		TabHost tabHost = getTabHost();

		// Creo la tabella per la mappa
		TabSpec poiMap = tabHost.newTabSpec("Mappa");
		poiMap.setIndicator("", getResources()
				.getDrawable(R.drawable.maps_icon));
		poiMap.setContent(viewPoiManager.createNewViewIntent("Map"));

		// Tab per la lista dei poi
		TabSpec poiList = tabHost.newTabSpec("Lista");
		poiList.setIndicator("Lista", null);
		poiList.setContent(viewPoiManager.createNewViewIntent("List"));

		// Tab per la realtà aumentata
		TabSpec poiAr = tabHost.newTabSpec("AR");
		poiAr.setIndicator("", getResources().getDrawable(R.drawable.ar_icon));
		poiAr.setContent(viewPoiManager.createNewViewIntent("Ar"));

		// Aggiungo le tab al gestore delle tab
		tabHost.addTab(poiMap);
		tabHost.addTab(poiList);
		tabHost.addTab(poiAr);

		// Inserisco lo sfondo delle tab
		setTabsBackground();

	}

	/**
	 * 
	 * Metodo che ricerca i punti di interesse in base alle categorie scelte
	 * dall'utente
	 * 
	 * @param context
	 *            contesto dell'attività
	 */
	private void searchPOI(final Context context) {
		AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
			// Creo una nuova progressDialog
			ProgressDialog dialog;

			@Override
			protected void onPreExecute() {
				// richiedo la location dell'utente
				locationManager.requestLocationUpdates(
						LocationManager.GPS_PROVIDER, 5, 1, myLocationListener);

				dialog = ProgressDialog.show(context, "Attendere...",
						"Recupero Posizione in corso...");

			}

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub

				// Attendo 15 sec che venga trovata la posizione o fin quando lo
				// stato della locazione trovata è false,altrimenti esco
				Long t = Calendar.getInstance().getTimeInMillis();
				while (!hasLocation
						&& Calendar.getInstance().getTimeInMillis() - t < 15000) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				;

				// Se è stata trovata una locazione procedo alla ricerca dei
				// poi, altrimenti termino la ricerca
				if (hasLocation) {
					// Salvo la location trovata nel gestore delle
					// visualizzazioni
					viewPoiManager.setMylocation(mylocation);

					//Controllo che le ci sia almeno un categoria per effettuare una ricerca
					if (CategoriesManager.getCategoriesManager().getCategories().size()>0){
						
					// Richiesta al gestore dei poi di ircercare i poi
					PoiListResponse response = PoiManager.searchPoi(mylocation,
							context, CategoriesManager.getCategoriesManager().getCategoriesSelected());

					// Se la risposta del gestore è nulla o si è verificato un
					// errore,termino la ricerca,altrimenti
					// procedo al salvataggio dei poi
					if (response == null || response.getStatus() != 200) {
						dialog.dismiss();

					} else {
						// salvo i poi
						pois = (ArrayList<Poi>) response.getPois();
						// salvo i poi nel gestore del visulizzatore
						viewPoiManager.setPois(pois);

					}
					}
				}

				return null;

			}

			protected void onPostExecute(String result) {
				dialog.dismiss();

				// se la locazion non è stata trovata, resituisco un messaggio
				// d'errore e termino l'attività,
				// altrimenti, setto lo stato della location e inzializzo le tab
				if (mylocation == null) {
					final AlertDialog.Builder builder = new AlertDialog.Builder(
							context);
					builder.setMessage("impossibile stabilire posizione");
					builder.setNeutralButton("Continua",
							new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog,
								int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});
					builder.setCancelable(true);

					finish();
				} else {
					hasLocation = false;
					if (setTab == false) {
						setTab();
						setTab = true;
					}
				}
			}

		};
		task.execute((Void[]) null);

	}

	/**
	 * Setta il background delle tab
	 */
	private void setTabsBackground() {
		// per ogni tab setta il background di Tab seleziona e tab non
		// selezionata
		for (int i = 0; i < getTabHost().getTabWidget().getChildCount(); i++) {
			getTabHost().getTabWidget().getChildAt(i)
			.setBackgroundResource(R.drawable.tabs_backgorund);
		}
		getTabHost().getTabWidget().getChildAt(getTabHost().getCurrentTab())
		.setBackgroundResource(R.drawable.tabs_backgorund); // Selezionato
	}

}