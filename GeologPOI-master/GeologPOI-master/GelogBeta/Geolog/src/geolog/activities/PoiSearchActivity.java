package geolog.activities;


import geolog.managers.CategoriesManager;
import geolog.managers.PoiManager;
import geolog.poi.visualization.PoiViewManager;
import geolog.util.ui.UtilDialog;


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


import com.geolog.activity.R;
import com.geolog.dominio.web.PoiListResponse;

/**
 * Attivit� per la ricerca dei punti di interesse. Vengono create le tab che rappresentano le varie visualizzazioni dei poi
 * se il gps � attivo vengono ricercati i poi e passate alle varie visualizzazioni
 * 
 * @author Lorenzo
 * 
 */
public class PoiSearchActivity extends TabActivity {

	// ArrayList dei poi ricercati
	/**
	 * @uml.property  name="pois"
	 */
	//private ArrayList<Poi> pois;

	// Gestore visualizzazione poi
	/**
	 * @uml.property  name="viewPoiManager"
	 * @uml.associationEnd  
	 */
	private PoiViewManager viewPoiManager;

	// Gestore delle locazioni
	/**
	 * @uml.property  name="locationManager"
	 * @uml.associationEnd  
	 */
	private LocationManager locationManager;

	// Listenr delle locazione
	/**
	 * @uml.property  name="myLocationListener"
	 * @uml.associationEnd  
	 */
	private LocationListener myLocationListener;

	// Locazione dell'utente
	/**
	 * @uml.property  name="mylocation"
	 * @uml.associationEnd  
	 */
	private Location mylocation;

	// Stato della locazione dell'utente
	/**
	 * @uml.property  name="hasLocation"
	 */
	private boolean hasLocation;

	// Contesto dell'applicazione
	/**
	 * @uml.property  name="context"
	 * @uml.associationEnd  
	 */
	private Context context;

	// Stato delle tabelle
	/**
	 * @uml.property  name="setTab"
	 */
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
	//	pois = new ArrayList<Poi>();

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

		// se il provider gps non � supportato dal device, viene resituito un
		// messaggio d'errore e viene terminata l'attivit�
		if (locationManager.getProvider(LocationManager.GPS_PROVIDER) == null) {
			UtilDialog.createAlertNoProviderGps(context, this).show();
			
		}

		// se il gps � abilitato si procede alla ricerca dei punti di
		// interesse,altrimenti viene richiesto di attivare il gps
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) == true) {
			searchPOI(this);

		} else {
			UtilDialog.createAlertNoGps(this, this).show();
	
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
	 * lista,su mappa,su realt� aumentata
	 * 
	 */
	private void setTab() {
		// Recupero il riferimento all'TabHost
		TabHost tabHost = getTabHost();

		// Creo la tabella per la mappa
		TabSpec poiMap = tabHost.newTabSpec("Mappa");
		poiMap.setIndicator("", getResources()
				.getDrawable(R.drawable.maps));
		poiMap.setContent(viewPoiManager.createNewViewIntent("Map"));

		// Tab per la lista dei poi
		TabSpec poiList = tabHost.newTabSpec("Lista");
		poiList.setIndicator("", getResources()
				.getDrawable(R.drawable.appdraw1));
		poiList.setContent(viewPoiManager.createNewViewIntent("List"));

		// Tab per la realt� aumentata
		TabSpec poiAr = tabHost.newTabSpec("AR");
		poiAr.setIndicator("", getResources().getDrawable(R.drawable.camera1));
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
	 *            contesto dell'attivit�
	 */
	public void searchPOI(final Context context) {
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
				// stato della locazione trovata � false,altrimenti esco
				Long t = Calendar.getInstance().getTimeInMillis();
				while (!hasLocation
						&& Calendar.getInstance().getTimeInMillis() - t < 15000) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				//aggiungo la mia locazione e i poi al gestore delle viste
			//	viewPoiManager.setMylocation(mylocation);
			//	viewPoiManager.setPois(pois);
				
				// Se � stata trovata una locazione procedo alla ricerca dei
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

					// Se la risposta del gestore � nulla o si � verificato un
					// errore,termino la ricerca,altrimenti
					// procedo al salvataggio dei poi
					if (response == null || response.getStatus() != 200) {
						dialog.dismiss();

					} else {
						// salvo i poi
						//pois = (ArrayList<Poi>) response.getPois();
						// salvo i poi nel gestore del visulizzatore
						viewPoiManager.setPois( response.getPois());
  
					}
					}
				}

				return null;

			}

			protected void onPostExecute(String result) {
				dialog.dismiss();

				// se la locazion non � stata trovata, resituisco un messaggio
				// d'errore 
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
							if (setTab == false) {
								setTab();
								setTab = true;
							}
						}
					});
					builder.setCancelable(false);
					builder.show();
					//finish();
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

	public Location getMylocation() {
		return mylocation;
	}

	public void setMylocation(Location mylocation) {
		this.mylocation = mylocation;
	}

	public boolean isHasLocation() {
		return hasLocation;
	}

	public void setHasLocation(boolean hasLocation) {
		this.hasLocation = hasLocation;
	}

	public PoiViewManager getViewPoiManager() {
		return viewPoiManager;
	}

	
}