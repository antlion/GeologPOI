package geolog.activities;



import geolog.ar.ARLayout;
import geolog.ar.CustomCameraView;
import geolog.ar.FourSquareClient;
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
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
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


public class PoiAugmentedRealityManager extends Activity  implements ItypeOfViewPoi{
	/**
	 * Called when the activity is first created.
	 * @uml.property  name="cv"
	 * @uml.associationEnd  
	 */
	private CustomCameraView cv;
	public static volatile Context ctx;
	public Context context;
	/**
	 * @uml.property  name="ar"
	 * @uml.associationEnd  
	 */
	private ARLayout ar;
	/**
	 * @uml.property  name="curLocation"
	 * @uml.associationEnd  
	 */
	volatile Location curLocation = null;
	/**
	 * @uml.property  name="poi"
	 */
	private ArrayList<Poi> poi;
	/**
	 * @uml.property  name="gpsListener"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private LocationListener gpsListener = new LocationListener(){

		@SuppressWarnings("unchecked")
		public void onLocationChanged(Location location)
		{
			Log.e("HoldMe","Got first");
			if(curLocation != null)
				return;
			poi = new ArrayList<Poi>();
			ParametersBridge bridge = ParametersBridge.getInstance();
			poi = (ArrayList<Poi>) bridge.getParameter("listaPOI");
			curLocation = location;
			Log.d("miaLocation",location.toString());
			//  curLocation.setLatitude(41.687015);
			// curLocation.setLongitude(12.774302);
			new Thread(){
				public void run(){
					FourSquareClient fc = new FourSquareClient();
					Vector<PoiOnCamera> vc = fc.prova(curLocation,poi,getApplicationContext());
					//Vector<FourSqareVenue> vc = fc.getVenuList(curLocation);
					Log.e("Where4","CurLocation LA:"+curLocation.getLatitude()+" LO:"+curLocation.getLongitude());

					ar.clearARViews();
					if(vc != null && vc.size() > 0)
					{
						@SuppressWarnings("rawtypes")
						Enumeration e = vc.elements();
						while(e.hasMoreElements())
						{
							PoiOnCamera fq = (PoiOnCamera) e.nextElement();
							Log.e("Where4","Got Venue:"+fq.name);
							if(fq.location != null)
								Log.i("Where4", "Lat:"+fq.location.getLatitude()+":"+fq.location.getLongitude());
							Log.e("Where4", "Azimuth: "+fq.azimuth);
							ar.addARView(fq);
						}
					}
				}
			}.start();
			LocationManager locMan = (LocationManager)ctx.getSystemService(Context.LOCATION_SERVICE);
			locMan.removeUpdates(this);


		}

		public void onProviderDisabled(String provider){}

		public void onProviderEnabled(String provider){}

		public void onStatusChanged(String provider, int status, Bundle extras){}

	};
	private MenuCategory menuCategory;



	public void onStart()
	{
		super.onStart();
	}
	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState) {
		try{
			super.onCreate(savedInstanceState);

			poi  = new ArrayList<Poi>();
			ParametersBridge bridge = ParametersBridge.getInstance();
			poi = (ArrayList<Poi>) bridge.getParameter("listaPOI");
			// curLocation =(Location)ParametersBridge.getInstance().getParameter("location");
			context = this;
			ctx = this.getApplicationContext();
			// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
			requestWindowFeature(Window.FEATURE_NO_TITLE); 
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
					WindowManager.LayoutParams.FLAG_FULLSCREEN); 

			ar = new ARLayout(getApplicationContext());

			cv = new CustomCameraView(this.getApplicationContext());
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			//cl = new CompassListener(this.getApplicationContext());
			WindowManager w = getWindowManager();
			Display d = w.getDefaultDisplay();
			int width = d.getWidth();
			int height = d.getHeight(); 
			ar.screenHeight = height;
			ar.screenWidth = width;
			FrameLayout rl = new FrameLayout(getApplicationContext());

			rl.addView(cv,width,height);
			ar.debug = true;
			rl.addView(ar, width, height);
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
			//Log.e("Where","Orientation:"+i);
			//rl.bringChildToFront(cl);
			//addLoadingLayouts();
			String choose = (String) ParametersBridge.getInstance().getParameter("hint");
	 		if ( choose == null)
	 		{
	 			UtilDialog.createBaseToast("Clicca sul tasto del menu, per ricercare i poi", context).show();
	 		}
	 		else
	 		{
	 			if ( choose.equals("true"))
	 				UtilDialog.createBaseToast("Trascina le frecce per avviare le funzionalità dell'applicazione", context).show();
	 		}
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
		cv.closeCamera();
		ar.close();
		//cl.close();
		//cv.closeCamera();
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
				FourSquareClient fc = new FourSquareClient();
				Vector<PoiOnCamera> vc = fc.prova(curLocation,poi,getApplicationContext());
				//Vector<FourSqareVenue> vc = fc.getVenuList(curLocation);
				Log.e("Where4","CurLocation LA:"+curLocation.getLatitude()+" LO:"+curLocation.getLongitude());

				ar.clearARViews();
				if(vc != null && vc.size() > 0)
				{
					Enumeration e = vc.elements();
					while(e.hasMoreElements())
					{
						PoiOnCamera fq = (PoiOnCamera) e.nextElement();
						Log.e("Where4","Got Venue:"+fq.name);
						if(fq.location != null)
							Log.i("Where4", "Lat:"+fq.location.getLatitude()+":"+fq.location.getLongitude());
						Log.e("Where4", "Azimuth: "+fq.azimuth);
						ar.addARView(fq);
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

						// se la risposta è negativa o si è verifcato un errore
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
							
							// Se è stato trovato almeno un poi, aggiorno la
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

