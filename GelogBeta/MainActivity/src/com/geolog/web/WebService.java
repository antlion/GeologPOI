package com.geolog.web;

import java.util.Calendar;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.geolog.dominio.Category;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.neurospeech.wsclient.SoapFaultException;

import prova.WS;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

public class WebService {
	private Context context;
	private PoiListResponse response;
	public WebService (Context context) {
		this.context = context;
		response = null;
	}
	
	public PoiListResponse findNearby(Location location, String category)
	{
		
		final AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
		ProgressDialog dialog;
		boolean connectionTimeout = false;
		
		protected void onPreExecute(){
			 dialog = ProgressDialog.show(context, "Attendere...", "Recupero punti di interesse");
		}
		
		
		
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			//locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 1, myLocationListener);
			 WS nuovo = new WS();
	           nuovo.setBaseUrl("http://160.80.135.31:8080/GeologWeb/services/WS"); 
	          try {
	        	 
				String responseFromServices = nuovo.findNearby(41.45435, 22.232131, 8);
				GsonBuilder builder = new GsonBuilder();
				Gson gson = builder.create();
				response = gson.fromJson(responseFromServices, PoiListResponse.class);
			 //  Log.d("numeroPois",String.valueOf(pois.getCount()));
				
				
				
			} catch(SoapFaultException e)
	          {
	        	 Log.d("timeout","aaa");
	        	 connectionTimeout = true;
	        	  return null;
	          }
	          catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
	          
			
           
            return null;
		}
            protected void onPostExecute(String result) {
            	dialog.dismiss();
            	if ( connectionTimeout == true)
            	{
            		AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            		alertDialog.setTitle("Informazione");
            		alertDialog.setMessage("Impossibile stabilire la conessione con il server");
            		alertDialog.setNeutralButton("Continua", new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});
            		alertDialog.show();
            		
            	}
            	
            	
            	
	            }
		
		
		
		
		};
		task.execute(null);
		return response;
	}
	
	
	

}
