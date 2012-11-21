package com.geolog;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.geolog.util.GestoreVisualizzaPOI;
import com.geolog.util.UtilGps;
import com.geolog.dominio.*;
public class RicercaPOI extends TabActivity implements Visualizzazione{

	private UtilGps gps;
	private Location location;
	private ArrayList<POIBase> pois;
	private GestoreVisualizzaPOI gestoreVisualizzazione;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
       
        
      
        
        //setContentView(R.layout.menu_activity);
        	gps = new UtilGps(this,this);
        	gestoreVisualizzazione = new GestoreVisualizzaPOI(this);
        	pois = new ArrayList<POIBase>();
        	Location location = new Location(LocationManager.GPS_PROVIDER);
        	location.setLatitude(41.858247);
        	location.setLongitude(22.345678);
	        PoiEmergenza newPOI = new PoiEmergenza(new Categoria("Emergenza",1),"Ospedale PTV", "ospedale qui viicno",1,location);
	        newPOI.setImage(R.drawable.croce_rossa);
	        pois.add(newPOI);
	        
	        gestoreVisualizzazione.setPois(pois);
	       setContentView(R.layout.main);
	        
	        TabHost tabHost = getTabHost();
	        
	        // Tab for Photos
	        TabSpec photospec = tabHost.newTabSpec("Lista");
	        // setting Title and Icon for the Tab
	        
	        photospec.setIndicator("Lista", null);
	        Intent photosIntent = gestoreVisualizzazione.visualizzaLista();
	        photospec.setContent(photosIntent);
	 
	        // Tab for Songs
	        TabSpec songspec = tabHost.newTabSpec("Mappa");
	        songspec.setIndicator("", getResources().getDrawable(R.drawable.maps_icon));
	        Intent songsIntent =gestoreVisualizzazione.visualizzaMappa();
	        songspec.setContent(songsIntent);
	 
	        // Tab for Videos
	        TabSpec videospec = tabHost.newTabSpec("AR");
	       videospec.setIndicator("", getResources().getDrawable(R.drawable.ar_icon));
	        Intent videosIntent = gestoreVisualizzazione.visualizzaAR();
	        videospec.setContent(videosIntent);
	 
	       
	        
	        
	        // Adding all TabSpec to TabHost
	        tabHost.addTab(photospec); // Adding photos tab
	        tabHost.addTab(songspec); // Adding songs tab
	        tabHost.addTab(videospec); // Adding videos tab
	       	        setTabsBackground();
	       	     
	        
	}
	
	
	
	private void setTabsBackground() {
		 for(int i=0;i<getTabHost().getTabWidget().getChildCount();i++) {
		  getTabHost().getTabWidget().getChildAt(i)
		  .setBackgroundResource(R.drawable.priva); //Non selezionato
		    }
		 getTabHost().getTabWidget().getChildAt(getTabHost().getCurrentTab())
		 .setBackgroundResource(R.drawable.priva); // Selezionato
		}
			
		 public void onResume()
		 {
			 super.onResume(); 
			 if (gps != null){
				 //effettua ricerca
				 gps.onResumeGps();
				 
			 }
		 }

		 public void onPause()
		 {
			 super.onPause();
			 if (gps != null){
				 //effettua ricerca
				 gps.onPauseGps();
				
			 }
		 }

	public void updateLocationData(Location location) {
		// TODO Auto-generated method stub
		this.location = location;
		GestorePOI.cercaPOI(location);
		//scegliModalitàVisualizzazione();
	}

	/*public void scegliModalitaVisualizzazione()
	{
		ImageView image = new ImageView(getBaseContext());	     
	    image.setImageResource(R.drawable.visualizza_poi);
		Resources res = getResources();
		String[] planets = res.getStringArray(R.array.visualizza_poi);
		new AlertDialog.Builder(this)
		.setCustomTitle(image)
		.setItems(planets, new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int which) {
            	   if (which == 0){
            		  
            		  startActivity(gestoreVisualizzazione.visualizzaMappa());
            		  
            	   }
            	   if(which == 1){
            		   startActivity(gestoreVisualizzazione.visualizzaLista());
            	   }
            	   if(which == 2){
            		   startActivity(gestoreVisualizzazione.visualizzaAR());
            	   }
           }
    }).show();
	}*/
	
	
	


	

   

}