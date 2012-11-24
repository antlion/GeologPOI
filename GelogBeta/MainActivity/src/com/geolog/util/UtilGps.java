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
import android.util.FloatMath;

public class UtilGps  {

	private LocationManager locationManager;
	private Context context;
	//private Location Location;
	private Activity activity;
	private final Visualizzazione vis;
	private LocationListener myLocationListener;
	private String providerId = LocationManager.GPS_PROVIDER;
	
	private static double  gps2m(float lat_a, float lng_a, float lat_b, float lng_b) {
	    float pk = (float) (180/3.14169);

	    float a1 = lat_a / pk;
	    float a2 = lng_a / pk;
	    float b1 = lat_b / pk;
	    float b2 = lng_b / pk;

	    float t1 = FloatMath.cos(a1)*FloatMath.cos(a2)*FloatMath.cos(b1)*FloatMath.cos(b2);
	    float t2 = FloatMath.cos(a1)*FloatMath.sin(a2)*FloatMath.cos(b1)*FloatMath.sin(b2);
	    float t3 = FloatMath.sin(a1)*FloatMath.sin(b1);
	    double tt = Math.acos(t1 + t2 + t3);
	   
	    return 6366000*tt;
	}
	
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
