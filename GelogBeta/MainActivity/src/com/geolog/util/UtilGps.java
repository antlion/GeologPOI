package com.geolog.util;

import com.geolog.R;
import com.geolog.Visualizzazione;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;

public class UtilGps  {

	private LocationManager locationManager;
	private Context context;
	//private Location Location;
	private Activity activity;
	private final Visualizzazione vis;
	private LocationListener myLocationListener;
	private String providerId = LocationManager.GPS_PROVIDER;
	
	
	public UtilGps(Context context,Activity activity)
	{
		this.activity=activity;
		 vis = (Visualizzazione)activity;
		
		this.context = context;
		locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
		
		myLocationListener = new LocationListener() { 
			
			
			   public void onStatusChanged(String provider, int status, Bundle extras) { 
			    if (status == LocationProvider.AVAILABLE) { 
			  
			   } else { 
				  
			   } 
			  } 
			 
			   public void onProviderEnabled(String provider) { 
			  
			  } 
			
			   public void onProviderDisabled(String provider) { 
				  
			  } 
			   
			 
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				vis.updateLocationData(location);
				
			} 
			 };
		
	}
	public Location getLastLocation(){
		Location location = locationManager.getLastKnownLocation(LocationManager.
				GPS_PROVIDER
				);
		return location;
	}
	
	public void onPauseGps()
	{
		  LocationManager locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE); 
		  locationManager.removeUpdates(myLocationListener); 
	}
	public void onResumeGps()
	{
		 LocationManager locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE); 
		  LocationProvider provider = locationManager.getProvider(providerId); 
		   if (provider == null) { 
			   buildAlertMessageNoGpsSupport();
		  } else { 
		    
		    boolean gpsEnabled = locationManager.isProviderEnabled(providerId); 
		    if (gpsEnabled) { 
		 
		   } else { 
			   buildAlertMessageNoGps();
		   } 
		 Location location = locationManager.getLastKnownLocation( LocationManager.GPS_PROVIDER); 
		    if (location != null) { 
		    vis.updateLocationData(location); 
		   } 
		   locationManager.requestLocationUpdates(providerId, 5, 1, myLocationListener); 
		  } 
	}
	
	public  void buildAlertMessageNoGpsSupport()
	{
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(context.getString(R.string.noGpsProvider));
		builder.setCancelable(true);
		final AlertDialog alert = builder.create();
		alert.show();
	}
	public void buildAlertMessageNoGps() {
		// TODO Auto-generated method stub
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
	    builder.setMessage("Yout GPS seems to be disabled, do you want to enable it?")
	           .setCancelable(false)
	           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	               public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
	                   context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
	                   
	               }
	           })
	           .setNegativeButton("No", new DialogInterface.OnClickListener() {
	               public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
	                    dialog.cancel();
	               }
	           });
	    final AlertDialog alert = builder.create();
	    alert.show();
	}

	public boolean getGpsSupport()
	{
		if ( locationManager.getProvider(LocationManager.GPS_PROVIDER) == null){
			buildAlertMessageNoGpsSupport();
			return false;
	}
		else
			return true;
	}
	
	public boolean getGpsStatus()
	{
		if ( locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
			return true;
		else{
			buildAlertMessageNoGps();
		}
		return false;
	}
	
	
	
	}
