package geolog.activities;


import geolog.managers.CategoriesManager;
import geolog.managers.PoiManager;
import geolog.poi.visualization.ItypeOfViewPoi;
import geolog.poi.visualization.PoiViewManager;
import geolog.util.MenuCategory;
import geolog.util.POIAdapter;
import geolog.util.ParametersBridge;
import geolog.util.UtilDialog;

import java.util.ArrayList;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.geolog.activity.R;
import com.geolog.dominio.Poi;
import com.geolog.web.domain.PoiListResponse;


/**
 * Gestore della visualizzazione dei Poi una lista. Ogni elmento della lista è
 * cliccabile ed è possibile visionare le informazioni del poi cliccato.
 * 
 * @author Lorenzo
 * 
 */
public class PoiListManager extends ListActivity implements ItypeOfViewPoi
		 {

	// Array di poi contenente i poi resituiti da una ricerca di punti di
	// interesse
	private ArrayList<Poi> pois;

	// Contesto dell'attività
	private Context context;

	// Location dell'utente
	private Location myLocation;

	// adattore dei Poi
	private POIAdapter poisAdapter;

	// Menu delle categorie
	private MenuCategory menuCategory;

	// ListaView di poi
	private ListView poiList;

	// Stato del click del menu dei poi
	private boolean itemListPOIClicked;

	// Gestore delle Location
	private LocationManager locationManager;

	// Listenr delle location
	private LocationListener myLocationListener;

	@SuppressWarnings("unchecked")
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.poi_list_layout);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);

		// La visibilità delle informazioni relative ad uno specifico poi è
		// nascosta
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.layoutInformationPOI);
		rl.setVisibility(View.GONE);

		// Inzializzo lo stato dei click dei poi a false.
		itemListPOIClicked = false;

		// Salvo il contesto dell'attività
		context = this;

		// Inizializzo l'arrayList dei poi
		pois = new ArrayList<Poi>();
		// Recupero i poi ricercati da una precedente ricerca
		ParametersBridge bridge = ParametersBridge.getInstance();
		// aggiorno la lista dei poi
		pois = ((ArrayList<Poi>) bridge.getParameter("listaPOI"));

		// Aggiorno la posizione dell'utente
		myLocation = (Location) ParametersBridge.getInstance().getParameter(
				"location");

		// aggiorno la lista deiPOI
		setPoiList();

		// Inzializzo il menu delle categorie
		menuCategory = new MenuCategory(false,
				(ListView) findViewById(R.id.list_category),
				CategoriesManager.getCategoriesManager(), context);
		// Il menù non è inzialmente visibile
		menuCategory.setVisibilityListCategory(false);

		// Inzializzo il locationManager
		locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		// inizializzo il locationListner
		myLocationListener = new LocationListener() {

			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				// Aggionrno la locaazione
				updateLocationData(location);
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

		// Richiedo la ricerca della poszione dell'utente
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5,
				1, myLocationListener);

		// Aggiungo la possibilità di visualizzare le informazioni dei poi
		setPoiListInformation();
		
		//Controllo se devono essere visualizzati gli aggironamenti
		checkHint();
		
	}
	
	/**
	 * Controllo se devono essere visualizzati gli hint nell'applicazione
	 */
	public void checkHint() {
		// recupero il parametro del boolean hint
		String choose = (String) ParametersBridge.getInstance().getParameter(
				"hint");
		// Se il valore non è stato ancora inzializzato, verranno mostrati i
		// suggerimenti
		if (choose == null) {
			UtilDialog
					.createBaseToast(
							"Trascina le frecce per avviare le funzionalità dell'applicazione",
							context).show();
		} else if (choose.equals("true")) {
			// se il valore è uguale a true, vengono visualizzati i suggerimenti
			UtilDialog
					.createBaseToast(
							"Trascina le frecce per avviare le funzionalità dell'applicazione",
							context).show();
		}
	}

	/**
	 * Metodo che aggiung ogni elemento della lista dei poi ad un
	 * listener.Cliccando su un elemnto della lista dei poi, si ottengono
	 * informazioni più dettagliate sul poi
	 */
	private void setPoiListInformation() {
		// Se la lista dei poi e la mia locazione sono stati acquisiti corretamente si aggiungono i vari listner
		// agli elementi della lista
		if (pois != null && myLocation!= null) {
			poiList.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// lo stato dell'elemento cliccato viene aggiornato
					itemListPOIClicked = true;

					// Recupero i layout dell'interfaccia grafica
					final LinearLayout ln = (LinearLayout) findViewById(R.id.linearLayout5);
					final RelativeLayout rl = (RelativeLayout) findViewById(R.id.layoutInformationPOI);

					// Setto la visibilità della lista delle categorie e dei
					// layout
					// Inizializzo la lista delle categorie
					ListView listCategory = (ListView) findViewById(R.id.list_category);
					listCategory.setVisibility(View.GONE);
					ln.setVisibility(View.GONE);
					rl.setVisibility(View.VISIBLE);

					// Recupero il poi che è stato selezionato
					Poi poi = (Poi) poisAdapter.getItem(arg2);
					// Visualizzo l'immagine rappresentativa del poi
					ImageView imageDescriptionPOi = (ImageView) findViewById(R.id.imagePOI);
					imageDescriptionPOi.setImageDrawable(poi
							.setImageFromResource(context));
					Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/bebe.otf");
				    
				   
					// Visualizzo il nome del poi
					TextView titoloPOI = (TextView) findViewById(R.id.nomePOI);
					titoloPOI.setText(poi.getNome());
					titoloPOI.setTypeface(myTypeface);
					// Visualizzo la descrizione del poi
					TextView descrizionePOI = (TextView) findViewById(R.id.descriptionPOI);
					descrizionePOI.setText(poi.getDescrizione());
					descrizionePOI.setTypeface(myTypeface);

				}
			});

		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		// Se viene premuto il pulsante del menu, si procede a visualizzare la
		// lista delle categorie
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			if (menuCategory.checkMenuCategory()) {
				searchPoi(context);
			}
			
			return true;
		}

		// se è stato premuto il pulsante indietro e ci troviamo
		// nell'informazione del poi, viene chiusa l'informazione dei poi
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (itemListPOIClicked) {
				itemListPOIClicked = false;
				final RelativeLayout rl = (RelativeLayout) findViewById(R.id.layoutInformationPOI);
				final LinearLayout ln = (LinearLayout) findViewById(R.id.linearLayout5);
				rl.setVisibility(View.GONE);
				ln.setVisibility(View.VISIBLE);
				return true;
			}
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		// Rimuovo il locationManager dal listener
		LocationManager locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		locationManager.removeUpdates(myLocationListener);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onResume() {
		super.onResume();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
		// aggiorno il menù delle categorie
		menuCategory.updateMenuCategory();
		
		
		
		pois = ((ArrayList<Poi>) ParametersBridge.getInstance().getParameter("listaPOI"));

		// Aggiorno la posizione dell'utente
		myLocation = (Location) ParametersBridge.getInstance().getParameter(
				"location");

		// aggiorno la lista deiPOI
		setPoiList();
		
		
		// Richedo la poszione dell'utente
		locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		Location location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location != null) {
			myLocation = location;
			// updateLocationData(location);
		}
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5,
				1, myLocationListener);
	}

	public void updateLocationData(Location location) {
		// TODO Auto-generated method stub

		// Aggiorno la location
		myLocation = location;

		// Aggiorno la lista dei poi
		setPoiList();

	}



	/**
	 * 
	 * Metodo che ricerca i punti di interesse in base alle categorie scelte
	 * dall'utente
	 * 
	 * @param context
	 *            contesto dell'attività
	 */
	@Override
	public void searchPoi(final Context context) {

		// Definisco un nuovo asynctask per la ricerca dei poi
		AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {

			// definisco una nuova barra di progresso
			ProgressDialog dialog;
			private PoiListResponse response = null;

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

				// Controllo che ci sia almeno una categoria per ricercare i poi
				if (CategoriesManager.getCategoriesManager().getCategories()
						.size() > 0) {
					// Rhiedo la ricerca dei poi al servizio web
				 response = PoiManager.searchPoi(myLocation,
							context, CategoriesManager.getCategoriesManager()
									.getCategoriesSelected());

				}
				return null;

			}

			protected void onPostExecute(String result) {
				dialog.dismiss();

				// se la risposta è negativa o si è verifcato un errore
				// viene
				// mostrato un messaggio d'errore e la
				// barra di progresso viene chiusa, altrimenti viene
				// aggiornata
				// la lista dei poi
				if (response == null || response .getStatus() != 200) {
					
					UtilDialog.alertDialog(context,
							"impossibile recuperare i poi").show();
				} else {
					// Aggiorno la lista dei poi
					pois = (ArrayList<Poi>) response.getPois();

					// Recupero il gestore della visualizzazione dei poi
					PoiViewManager view = new PoiViewManager(context);
					// aggiorno i poi del visualizzatore
					view.setPois(pois);
					// aggiorno la location del visualizzatore
					view.setMylocation(myLocation);

				
					ParametersBridge.getInstance().addParameter("listaPOI", pois);
					ParametersBridge.getInstance().addParameter("location", myLocation);
					// Se è stato trovato almeno un poi, aggiorno la
					// locazione,
					// altrimenti resituisco un messaggio d'errore
					if (pois.size() < 0) {
						dialog.dismiss();
						UtilDialog.createBaseToast("nessun poi trovato",
								context);
					} else {
						updateLocationData(myLocation);
					}

				}
			
					
			}

		};
		task.execute((Void[]) null);

	}

	/**
	 * La lista dei poi ricerca dall'utente viene aggiunta all'adattatore dei
	 * poi, che andarà a popolare la ListView dei poi.
	 * 
	 */
	private void setPoiList() {
		// Se la lista dei poi contiene è diversa da null, si procede a creare
		// un nuovo adattatore e a popolarlo,altrimenti viene
		// visualizzato un messaggio d'errore.
		if (pois != null && myLocation != null) {
		
			poisAdapter = new POIAdapter(this, pois, myLocation);
			setListAdapter(poisAdapter);
			poiList = getListView();
			poiList.setTextFilterEnabled(true);
			poiList.setVisibility(View.GONE);
			poiList.setVisibility(View.VISIBLE);
		} else {

		UtilDialog.createBaseToast("Impossibile recuperare i poi", context).show();
		}
	}

	

}
