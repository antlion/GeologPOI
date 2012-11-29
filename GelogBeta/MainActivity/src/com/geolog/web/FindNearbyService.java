package com.geolog.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;



import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import prova.WS;
import prova.WS_findNearby;
import prova.WS_findNearbyResponse;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.util.Log;

import com.geolog.dominio.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;


public class FindNearbyService  {
	
	private static final String METHOD_NAME = "findNearby";
	private static final String NAMESPACE = "http://math";
	private static final String SOAP_ACTION = "http://math/findNearby";
	private static final String URL = "http://160.80.135.31:8080/GeologWeb/services/WS";
	private static  Poi poi;
	private static boolean getResponse = false;
	 static PoiListResponse response2 = null;
	public static  Poi findNearby (final Location location,final ArrayList<Category> categorie) 
	{
		 final ArrayList<Poi> prova = new ArrayList<Poi>();
		 poi = null;
		new Thread(new Runnable() {
	        public void run() {
	        	SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
	        	  	   
	        	        	
	        	    	
	        	        	
	        	
	        	request.addProperty("latitude",location.getLatitude());
	        	request.addProperty("langitude", location.getLongitude());
	        
	        	SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	        	
	        	envelope.addMapping(NAMESPACE, "latitude", Double.class);
	        	envelope.addMapping(NAMESPACE, "langitude", Double.class);
	        	//MarshalDouble fl = new MarshalDouble();
	        	//fl.register(envelope);
	        	envelope.setOutputSoapObject(request);
	        	HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	        	
	        		//this is the actual part that will call the webservice
	        		try {
						androidHttpTransport.call(SOAP_ACTION, envelope);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						
					} catch (XmlPullParserException e) {
						// TODO Auto-generated catch block
						
					}
	        	 
	        	

	        	// Get the SoapResult from the envelope body.
	        	SoapObject result = (SoapObject) envelope.bodyIn;

	        	if(result != null){
	        		

	        		//Get the first property and change the label text
	        		System.out.println("SOAP response:\n\n" );
	        		String json = result.getProperty(0).toString();
					GsonBuilder builder = new GsonBuilder();
					Gson gson = builder.create();
					Poi pois = gson.fromJson(json, Poi.class);
					System.out.println(pois.getNome());
					// prova.add(pois);
					poi = pois;
	        	}
	        }
	        }).start();
	        
		
	       
	return poi;
	}
	public static PoiListResponse getPoiListResponse()
	{
		return response2;
	}
	
	public static PoiListResponse prova()
	{
		 
		
	        	
	             WS nuovo = new WS();
	             nuovo.setBaseUrl(URL); 
	          try {
				String response = nuovo.findNearby(41.45435, 22.232131, 8);
				Log.d("richiesta",response);
				GsonBuilder builder = new GsonBuilder();
				Gson gson = builder.create();
				PoiListResponse pois = gson.fromJson(response, PoiListResponse.class);
			   Log.d("numeroPois",String.valueOf(pois.getCount()));
			response2 = pois;
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	       
		return response2;
	}
	/*public static PoiListResponse prova()
	{
		 
		new Thread(new Runnable() {
	        public void run() {
	        	
	             WS nuovo = new WS();
	             nuovo.setBaseUrl(URL); 
	          try {
				String response = nuovo.findNearby(41.45435, 22.232131, 8);
				Log.d("richiesta",response);
				GsonBuilder builder = new GsonBuilder();
				Gson gson = builder.create();
				PoiListResponse pois = gson.fromJson(response, PoiListResponse.class);
			   Log.d("numeroPois",String.valueOf(pois.getCount()));
			response2 = pois;
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        }
	        }).start();
		return response2;
	}*/
	
	
	public static void SearchPOI(final Context context){
		AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
			
			ProgressDialog dialog;
			@Override
			protected void onPreExecute(){
				 WS nuovo = new WS();
	             nuovo.setBaseUrl(URL); 
	             dialog = ProgressDialog.show(context, "Attendere...", "Recupero Posizione in corso...");
	             try {
	            	 
					String response = nuovo.findNearby(41.45435, 22.232131, 8);
					getResponse = true;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				//locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 1, myLocationListener);
				Long t = Calendar.getInstance().getTimeInMillis();
	            while ( getResponse == false && Calendar.getInstance().getTimeInMillis() - t < 15000) {
	                try {
	                    Thread.sleep(1000);
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
	            };
	            return null;
			
			}
			 protected void onPostExecute(String result) {
		            dialog.dismiss();
		            if( getResponse == false){
		            	Log.d("impossibile prendere la risposta","");
		            }
		            }
		        
			
		};
		task.execute(null);
	
		}
	
	
	
	
	
}
