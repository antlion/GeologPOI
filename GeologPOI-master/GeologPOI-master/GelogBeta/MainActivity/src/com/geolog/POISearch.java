package com.geolog;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;


import com.geolog.dominio.Poi;
import com.geolog.util.AuthGoogle;
import com.geolog.util.UtilDialog;
import com.geolog.web.Services;
import com.geolog.web.domain.PoiListResponse;

public class POISearch extends TabActivity {

	
	
	private ArrayList<Poi> pois =  new ArrayList<Poi>();
	private ViewPOIHandler gestoreVisualizzazione;
	
	private LocationManager locationManager;
	private LocationListener myLocationListener;
	private Location mylocation;
	private boolean hasLocation; 
	private Context context;
	private boolean setTab;
	private CategoriesHandler categoryHandler;
	public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  //AuthGoogle.aaaaa(this);
  //  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
    setTab = false;
       context = this; 
       mylocation = null;
       hasLocation = false;
     categoryHandler = CategoriesHandler.getGestoreCategorie();
      
       locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
       myLocationListener = new LocationListener() { 
				public void onStatusChanged(String provider, int status, Bundle extras) { 
			    if (status == LocationProvider.AVAILABLE) { 
			    	//esegui scansione
			   } else { 
				  //restituisci messaggio di errore
			   } 
			  } 
			 public void onProviderEnabled(String provider) { 
			  
			  } 
			
			   public void onProviderDisabled(String provider) { 
				  
			  } 
			   
			 
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				//vis.updateLocationData(location);
				//effutua ricerca poi
				Log.d("Locazione cabiata", location.toString());
				
				hasLocation = true;
				//chiama web Service
				//pois = GestorePOI.cercaPOI(location);
				mylocation = location;
				//location.setLatitude(41.703422);
	        	//location.setLongitude(12.690868);
	        	/*Poi newPOI = new Poi(new Category("Emergenza",1,R.drawable.croce_rossa),"Ospedale PTV", "ospedale qui viicno",1,null, location, R.drawable.ptv);
	        	Location location2 = new Location("prova");
	        	location2.setLatitude(42.703422);
	        	location2.setLongitude(20.690868);
	        	Poi newPOI2 = new Poi(new Category("Ristoro",2,R.drawable.food_icon),"SushiBar", "Ristorante cinese pienodi schifezze",2,null, location2, R.drawable.kfc);
	        	pois.add(newPOI);
	        	pois.add(newPOI2);*/
	        	
	        	/*LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE); 
	  		  	locationManager.removeUpdates(myLocationListener);
	  		  	if (setTab == false)
	  		  	{ 
	  		  		setTab();
	  		  		setTab = true;
	  		  	}*/
				//Log.d("size",String.valueOf(pois.size()));
			} 
			 };
      
       
			//inizializzo i poi a null
        	gestoreVisualizzazione = new ViewPOIHandler(this);
        	gestoreVisualizzazione.setPois(pois);
        	gestoreVisualizzazione.setMylocation(mylocation);
        	//locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 1, myLocationListener);
        	//Location location = new Location(LocationManager.GPS_PROVIDER);
        	//location.setLatitude(41.703422);
        	//location.setLongitude(12.690868);
	      //  Poi newPOI = new Poi(new Category("Emergenza",1),"Ospedale PTV", "ospedale qui viicno",1,null, location, R.drawable.croce_rossa);
	       
	       // pois.add(newPOI);
	        
	       // gestoreVisualizzazione.setPois(pois);
	        setContentView(R.layout.poi_search_layout);
	        
	       
	        
	        if ( locationManager.getProvider(LocationManager.GPS_PROVIDER) == null){
	        	UtilDialog.createAlertNoGps(context).show();
	        	finish();
				}
		
				if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) == true)
				{
					searchPOI(this);
					
			       	     
				}
				else {
					UtilDialog.createAlertNoGps(this).show();
					
				}
	}
	
	
				
	        
	        
	public void setTab(){
		 TabHost tabHost = getTabHost();
		 // Tab for Songs
        TabSpec songspec = tabHost.newTabSpec("Mappa");
        songspec.setIndicator("", getResources().getDrawable(R.drawable.maps_icon));
        Intent songsIntent =gestoreVisualizzazione.visualizzaMappa();
        songspec.setContent(songsIntent);
        // Tab for Photos
        TabSpec photospec = tabHost.newTabSpec("Lista");
        // setting Title and Icon for the Tab
        
        photospec.setIndicator("Lista", null);
        Intent photosIntent = gestoreVisualizzazione.visualizzaLista();
        photospec.setContent(photosIntent);
        // Tab for Videos
        TabSpec videospec = tabHost.newTabSpec("AR");
       videospec.setIndicator("", getResources().getDrawable(R.drawable.ar_icon));
        Intent videosIntent = gestoreVisualizzazione.visualizzaAR();
        videospec.setContent(videosIntent);
 
       
        
        
      
       tabHost.addTab(songspec);
       tabHost.addTab(photospec); // Adding photos tab
         // Adding songs tab
       tabHost.addTab(videospec); // Adding videos tab
       setTabsBackground();
		
	}
							
		public void searchPOI(final Context context){
		AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
			
			ProgressDialog dialog;
			@Override
			protected void onPreExecute(){
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 1, myLocationListener);
				dialog = ProgressDialog.show(context, "Attendere...", "Recupero Posizione in corso...");
			
			}
			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				//locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 1, myLocationListener);
				Long t = Calendar.getInstance().getTimeInMillis();
	            while ( !hasLocation && Calendar.getInstance().getTimeInMillis() - t < 15000) {
	                try {
	                    Thread.sleep(1000);
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
	            };
	            
	            
	            if (hasLocation)
	            {
	            	
	            	//dialog.setMessage("Ricerco i poi");
	            
	            	PoiListResponse response = HandlerPOI.cercaPOI(mylocation, context);
	            	if (response == null || response.getStatus() != 200)
	            	{
	            		dialog.dismiss();
	            		UtilDialog.alertDialog(context, "impossibile recuperare i poi").show();
	            	}
	            	else {
	            			pois = (ArrayList<Poi>) response.getPois();
	            			gestoreVisualizzazione.setPois(pois);
	        	        	gestoreVisualizzazione.setMylocation(mylocation);
	            			if (pois.size()>0){
	            				dialog.dismiss();
	            				UtilDialog.createBaseToast("nessun poi trovato", context);}
	            			
	            	}
	            }
	            
	            
	            return null;
			
			}
			 protected void onPostExecute(String result) {
		            dialog.dismiss();
		            if(mylocation == null){
		            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		    		builder.setMessage("impossibile stabilire posizione");
		    		builder.setNeutralButton("Continua", new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});
		    		builder.setCancelable(true);
		    		
		    		finish();
		    		}
		            else {
		            	hasLocation = false;
		            	if (setTab == false)
			  		  	{ 
			  		  		setTab();
			  		  		setTab = true;
			  		  	}
		            }
		        }
			
		};
		task.execute(null);
	
		}
	public void onPause()
	{
		super.onPause();
		  LocationManager locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE); 
		  locationManager.removeUpdates(myLocationListener); 
	}
	
	public void onResume()
	{
		super.onResume();
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) &&  locationManager.getProvider(LocationManager.GPS_PROVIDER) != null)
		{	//getLocationForSearchPOI(this);
		Location location = locationManager.getLastKnownLocation(
				LocationManager.GPS_PROVIDER
				);
				if (location != null) {
					mylocation = location;
				//updateLocationData(location);
				}
		}
			
		  // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 1, myLocationListener); 
		  
	}
	
	private void setTabsBackground() {
		 for(int i=0;i<getTabHost().getTabWidget().getChildCount();i++) {
		  getTabHost().getTabWidget().getChildAt(i)
		  .setBackgroundResource(R.drawable.priva); //Non selezionato
		    }
		 getTabHost().getTabWidget().getChildAt(getTabHost().getCurrentTab())
		 .setBackgroundResource(R.drawable.priva); // Selezionato
		}

}