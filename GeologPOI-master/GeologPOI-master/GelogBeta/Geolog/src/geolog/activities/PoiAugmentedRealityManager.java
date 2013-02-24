package geolog.activities;



import geolog.ar.ARLayout;
import geolog.ar.CustomCameraView;
import geolog.ar.PoiOnCameraManager;
import geolog.ar.PoiOnCamera;
import geolog.managers.CategoriesManager;
import geolog.managers.PoiManager;
import geolog.poi.visualization.ItypeOfViewPoi;
import geolog.poi.visualization.PoiViewManager;
import geolog.util.ParametersBridge;
import geolog.util.ui.MenuCategory;
import geolog.util.ui.UtilDialog;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

import com.geolog.dominio.Poi;
import com.geolog.dominio.web.PoiListResponse;




import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ListView;


/**
 * @author antlion
 * Gestione della visualizzazione dei poi su realtà aumentata, attraverso l'ausilio della fotocamera del
 * dispositivo mobile.
 *
 */
public class PoiAugmentedRealityManager extends Activity  implements ItypeOfViewPoi{
	
	// Visualizzazione della videocamera
	private CustomCameraView customCameraView;
	//Contesto dell'applicazione
	public static volatile Context ctx;
	public Context context;
	//Layout della visualizzazione dei poi
	private ARLayout augmentedRealtyLayout;
	//Location corrente
	volatile Location curLocation = null;
	//lista dei poi da visualizzare
	private ArrayList<Poi> poi;
	//Listener del gps
	private LocationListener gpsListener = new LocationListener(){

	
		@SuppressWarnings("unchecked")
		public void onLocationChanged(Location location)
		{
			//Se la posizione non è nulla aggiorna la posizione
			if(curLocation != null)
				return;
			updateLocationData(location);
			/*poi = new ArrayList<Poi>();
			ParametersBridge bridge = ParametersBridge.getInstance();
			poi = (ArrayList<Poi>) bridge.getParameter("listaPOI");
			curLocation = location;
			Log.d("miaLocation",location.toString());
		
			new Thread(){
				public void run(){
					PoiOnCameraManager poiOnCameraManager = new PoiOnCameraManager();
					Vector<PoiOnCamera> poiOnCamera = poiOnCameraManager.createPoiOnCameraFromPoi(curLocation,poi,getApplicationContext());
					
					Log.e("Geolog","CurLocation LA:"+curLocation.getLatitude()+" LO:"+curLocation.getLongitude());

					augmentedRealtyLayout.clearARViews();
					if(poiOnCamera != null && poiOnCamera.size() > 0)
					{
						@SuppressWarnings("rawtypes")
						Enumeration e = poiOnCamera.elements();
						while(e.hasMoreElements())
						{
							PoiOnCamera element = (PoiOnCamera) e.nextElement();
							Log.e("Where4","Got Venue:"+element.name);
							if(element.location != null)
								Log.i("Geolog", "Lat:"+element.location.getLatitude()+":"+element.location.getLongitude());
							Log.e("Where4", "Azimuth: "+element.azimuth);
							augmentedRealtyLayout.addARView(element);
						}
					}
				}
			}.start();*/
			LocationManager locMan = (LocationManager)ctx.getSystemService(Context.LOCATION_SERVICE);
			locMan.removeUpdates(this);


		}

		public void onProviderDisabled(String provider){}

		public void onProviderEnabled(String provider){}

		public void onStatusChanged(String provider, int status, Bundle extras){}

	};
	
	//Menu delle categorie
	private MenuCategory menuCategory;



	public void onStart()
	{
		super.onStart();
	}
	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState) {
		try{
			super.onCreate(savedInstanceState);

			//Inizalizza una nuova lista dei poi
			poi  = new ArrayList<Poi>();
			//Ottieni i paremetri passati tra attività
			ParametersBridge bridge = ParametersBridge.getInstance();
			poi = (ArrayList<Poi>) bridge.getParameter("listaPOI");
			
			//Inzializza il contesto
			context = this;
			ctx = this.getApplicationContext();
			//Orientazione dello schermo
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			requestWindowFeature(Window.FEATURE_NO_TITLE); 
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
					WindowManager.LayoutParams.FLAG_FULLSCREEN); 
			
			//Inzializza il layout della visualizzazione dei poi
			augmentedRealtyLayout = new ARLayout(getApplicationContext());
			//Inizializza la visione della fotocamera
			customCameraView = new CustomCameraView(this.getApplicationContext());
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			//Ottieni la larghezza e la grandella dello schermo
			WindowManager w = getWindowManager();
			Display d = w.getDefaultDisplay();
			int width = d.getWidth();
			int height = d.getHeight(); 
			augmentedRealtyLayout.screenHeight = height;
			augmentedRealtyLayout.screenWidth = width;
			
			FrameLayout rl = new FrameLayout(getApplicationContext());
			rl.addView(customCameraView,width,height);
			augmentedRealtyLayout.debug = true;
			rl.addView(augmentedRealtyLayout, width, height);
			ListView listView = new ListView(ctx);
			listView.setLayoutParams(new LayoutParams(
					200,
					ViewGroup.LayoutParams.WRAP_CONTENT));
			
			// Inzializzazione del gestore delle categorie
			CategoriesManager categoriesHandler = CategoriesManager.getCategoriesManager();
			menuCategory = new MenuCategory(false,
					listView, categoriesHandler,
					ctx);
			rl.addView(listView);

			setContentView(rl);
			listView.setVisibility(View.GONE);
			LocationManager locMan = (LocationManager)ctx.getSystemService(Context.LOCATION_SERVICE);
			locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, gpsListener);
			
			
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			String  string =( new Boolean (prefs.getBoolean("checkBoxSuggestion", false))).toString();
			if (string.equals("true"))
				UtilDialog
				.createBaseToast(
						"premi il tasto menu'per visualizzare le categorie,chiudilo per avviare la ricerca", context).show();
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}






	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			if (menuCategory.checkMenuCategory()) {
				searchPoi(context);
			}
			return true;
		}
		else
			return super.onKeyDown(keyCode, event);
	}

	public void onDestroy()
	{
		super.onDestroy();
		customCameraView.closeCamera();
		augmentedRealtyLayout.close();
	
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}
	@Override
	public void updateLocationData(Location location) {
		// TODO Auto-generated method stub
		Log.e("HoldMe","Got first");
		
		
		curLocation = location;
		Log.d("miaLocation",location.toString());
		//  curLocation.setLatitude(41.687015);
		// curLocation.setLongitude(12.774302);
		new Thread(){
			@SuppressWarnings("rawtypes")
			public void run(){
				//Ottieni il gestore dei poi della VideoCamera
				PoiOnCameraManager poiOnCameraManager = new PoiOnCameraManager();
				//Creazione dei poi che devono essere visualizzati sulla fotocamera
				Vector<PoiOnCamera> poisOnCamera = poiOnCameraManager.createPoiOnCameraFromPoi(curLocation,poi,getApplicationContext());
				
				Log.e("Geolog","CurLocation LA:"+curLocation.getLatitude()+" LO:"+curLocation.getLongitude());
				//Pulisci lo shcermo da vecchi poi
				augmentedRealtyLayout.clearARViews();
				
				//Se il vettore dei poi non è nullo, ed esiste almeno un poi da visualizzare
				//aggiungi il poi sullo schermo
				if(poisOnCamera != null && poisOnCamera.size() > 0)
				{
					Enumeration e = poisOnCamera.elements();
					while(e.hasMoreElements())
					{
						PoiOnCamera poiOnCamera = (PoiOnCamera) e.nextElement();
						Log.e("Geolog","Got Venue:"+poiOnCamera.name);
						if(poiOnCamera.location != null)
							Log.i("Geolog", "Lat:"+poiOnCamera.location.getLatitude()+":"+poiOnCamera.location.getLongitude());
						Log.e("Geolog", "Azimuth: "+poiOnCamera.azimuth);
						augmentedRealtyLayout.addARView(poiOnCamera);
					}
				}
			}
		}.start();
	}
	@Override
	public void searchPoi(final Context context1) {

		// Definisco un nuovo asynctask per la ricerca dei poi
				AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {

					// definisco una nuova barra di progresso
					ProgressDialog dialog;
					private PoiListResponse response = null;

					@Override
					protected void onPreExecute() {
						// Richiedo la posizione dell'utente
						LocationManager locMan = (LocationManager)context1.getSystemService(Context.LOCATION_SERVICE);
						locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 1, gpsListener);

						// Mostro la barra di progresso
						dialog = ProgressDialog.show(context1, "Attendere...",
								"Ricerca poi in corso...");

					}

					@Override
					protected String doInBackground(Void... params) {
						// TODO Auto-generated method stub

						// Controllo che ci sia almeno una categoria per ricercare i poi
						if (CategoriesManager.getCategoriesManager().getCategories()
								.size() > 0) {
							// Rhiedo la ricerca dei poi al servizio web
						 response = PoiManager.searchPoi(curLocation,
									context1, CategoriesManager.getCategoriesManager()
											.getCategoriesSelected());

						}
						return null;

					}

					protected void onPostExecute(String result) {
						dialog.dismiss();

						// se la risposta � negativa o si � verifcato un errore
						// viene
						// mostrato un messaggio d'errore e la
						// barra di progresso viene chiusa, altrimenti viene
						// aggiornata
						// la lista dei poi
						if (response == null || response .getStatus() != 200) {
							
							UtilDialog.alertDialog(context1,
									"impossibile recuperare i poi").show();
						} else {
							// Aggiorno la lista dei poi
							poi = (ArrayList<Poi>) response.getPois();

							// Recupero il gestore della visualizzazione dei poi
							PoiViewManager view = new PoiViewManager(context);
							// aggiorno i poi del visualizzatore
							view.setPois(poi);
							// aggiorno la location del visualizzatore
							view.setMylocation(curLocation);

							
							ParametersBridge.getInstance().addParameter("listaPOI", poi);
							ParametersBridge.getInstance().addParameter("location", curLocation);
							
							// Se � stato trovato almeno un poi, aggiorno la
							// locazione,
							// altrimenti resituisco un messaggio d'errore
							if (poi.size() < 0) {
								dialog.dismiss();
								UtilDialog.createBaseToast("nessun poi trovato",
										context1).show();
							} else {
								updateLocationData(curLocation);
							}

						}
					
							
					}

				};
				task.execute((Void[]) null);

			
	}
}

