package com.geolog.web;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.http.util.ByteArrayBuffer;
import org.json.simple.JSONObject;

import prova.WS;
import prova2.WSs;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.geolog.dominio.*;
import com.geolog.util.UtilDialog;
import com.geolog.web.domain.AddPOIResponse;
import com.geolog.web.domain.BaseResponse;
import com.geolog.web.domain.CategoryListResponse;
import com.geolog.web.domain.PoiListResponse;
import com.geolog.web.domain.SuggestionResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Services {

	private WSs webService;
	private static final String WEB_SERVICE_URL = "http://192.168.0.102:8080/Prova/services/WS";
	private JSONObject result;

	//tutti i metodi mi ritoranano un oggetto di tipo BaseResponse
	private BaseResponse baseResponse;
	public Services()
	{
		webService = new WSs();
		webService.setUrl(WEB_SERVICE_URL);

	}

	public BaseResponse findNearby(final Location location,String id,final Context context)
	{
		final AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
			ProgressDialog dialog;

			protected void onPreExecute(){

				dialog = ProgressDialog.show(context, "Attendere...", "Ricerca punti di interesse");

			}
			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub


				try {
					String response = webService.findNearby((float)location.getLatitude(), true, (float)location.getLongitude(), true, null);
					GsonBuilder builder = new GsonBuilder();
					Gson gson = builder.create();
					PoiListResponse responseWeb= gson.fromJson(response, PoiListResponse.class);
					if ( responseWeb.getStatus() == 200){
						baseResponse = responseWeb;
					}

					else
						baseResponse = null;

				} 
				catch (Exception e) {
					// TODO Auto-generated catch block
					baseResponse = null;
					return null;
				}
				return null;
			}
			protected void onPostExecute(String result) {
				dialog.dismiss();
				if ( baseResponse == null)
				{
					UtilDialog.alertDialog(context, "Impossibile stabilire la conessione con il server").show();
				}

			}
		};
		task.execute(null);
		return baseResponse;
	}

	public BaseResponse asd()
	{
		try {
			String response = webService.listCategories();
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			CategoryListResponse responseWeb= gson.fromJson(response, CategoryListResponse.class);
			if ( responseWeb.getStatus() == 200){
				baseResponse = responseWeb;
			}

			else
				baseResponse = null;

		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			baseResponse = null;
			return null;
		}
		return null;
	}
	public BaseResponse getListCategory(final Context context)
	{
		final AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
			ProgressDialog dialog;

			protected void onPreExecute(){

				dialog = ProgressDialog.show(context, "Attendere...", "Invio segnalazione al Server");

			}
			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub


				try {
					String response = webService.listCategories();
					GsonBuilder builder = new GsonBuilder();
					Gson gson = builder.create();
					CategoryListResponse responseWeb= gson.fromJson(response, CategoryListResponse.class);
					if ( responseWeb.getStatus() == 200){
						baseResponse = responseWeb;
					}

					else
						baseResponse = null;

				} 
				catch (Exception e) {
					// TODO Auto-generated catch block
					baseResponse = null;
					return null;
				}
				return null;
			}
			protected void onPostExecute(String result) {
				dialog.dismiss();
				if ( baseResponse == null)
				{
					UtilDialog.alertDialog(context, "Impossibile stabilire la conessione con il server").show();
				}

			}
		};
		task.execute(null);
		return baseResponse;

	}

	public BaseResponse addSuggestion(final Suggestion suggestion,final Context context)
	{
		final AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
			ProgressDialog dialog;

			protected void onPreExecute(){
				result = new JSONObject();
				JSONObject categoria = new JSONObject();
				categoria.put("id",suggestion.getPoi().getCategoria().getId());

				JSONObject poi = new JSONObject();
				poi.put("nome",suggestion.getPoi().getNome());
				poi.put("descrizione", suggestion.getPoi().getDescrizione());
				poi.put("id", suggestion.getPoi().getIdTipo());
				poi.put("latitude", suggestion.getPoi().getLatitude());
				poi.put("longitude", suggestion.getPoi().getLongitude());
				poi.put("category",categoria);
				result.put("id", suggestion.getId());
				result.put("user",suggestion.getUser());

				result.put("creationDate", suggestion.getCreationDate());
				result.put("description", suggestion.getDescription());
				result.put("poi", poi);
				dialog = ProgressDialog.show(context, "Attendere...", "Invio segnalazione al Server");

			}
			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				//locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 1, myLocationListener);

				try {
					String response = webService.getSuggestion(result.toJSONString());
					GsonBuilder builder = new GsonBuilder();
					Gson gson = builder.create();
					SuggestionResponse responseWeb= gson.fromJson(response, SuggestionResponse.class);
					if ( responseWeb.getStatus() == 200){
						baseResponse = responseWeb;
					}

					else
						baseResponse = null;

				} 
				catch (Exception e) {
					// TODO Auto-generated catch block
					baseResponse = null;
					return null;
				}
				return null;
			}
			protected void onPostExecute(String result) {
				dialog.dismiss();
				if ( baseResponse == null)
				{
					UtilDialog.alertDialog(context, "Impossibile stabilire la conessione con il server").show();
				}

			}
		};
		task.execute(null);
		return baseResponse;



	}

	public BaseResponse addPOI(final Poi poi,final Context context)
	{
		final AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
			ProgressDialog dialog;

			protected void onPreExecute(){
				result = new JSONObject();
				
				result.put("name",poi.getNome());
				result.put("longitude",poi.getLongitude());
				result.put("latitude",poi.getLongitude());
				result.put("description", poi.getDescrizione());
				
				result.put("category_id",8);
				dialog = ProgressDialog.show(context, "Attendere...", "Invio segnalazione al Server");

			}
			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub


				try {
					WSs services = new WSs();
					services.setUrl("http://160.80.135.31:8080/GeologWeb/services/WS");
					String response = services.addPoi(result.toJSONString(),"io");
					GsonBuilder builder = new GsonBuilder();
					Gson gson = builder.create();
					CategoryListResponse responseWeb= gson.fromJson(response, CategoryListResponse.class);
					if ( responseWeb.getStatus() == 200){
						baseResponse = responseWeb;
					}

					else
						baseResponse = null;

				} 
				catch (Exception e) {
					// TODO Auto-generated catch block
					baseResponse = null;
					return null;
				}
				return null;
			}
			protected void onPostExecute(String result) {
				dialog.dismiss();
				if ( baseResponse == null)
				{
					UtilDialog.alertDialog(context, "Impossibile stabilire la conessione con il server").show();
				}

			}
		};
		task.execute(null);
		return baseResponse;

	}

	public Drawable downloadResource(String url,Context context)
	{
		
		
	

		String path = (context.getFilesDir().toString());
		try{
			URL web = new URL(url); 
			File file = new File(path+"salem.jpg");

			long startTime = System.currentTimeMillis();
		
			URLConnection ucon = web.openConnection();

			
			InputStream is = ucon.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);

			
			ByteArrayBuffer baf = new ByteArrayBuffer(50);
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}

		
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(baf.toByteArray());
			fos.close();
			Log.d("ImageManager", "download ready in"
					+ ((System.currentTimeMillis() - startTime) / 1000)
					+ " sec");
			Drawable d = new BitmapDrawable(context.getResources(),path+"salem.jpg");
			return d;
		}

		catch (IOException e) {
			Log.d("ImageManager", "Error: " + e);
			return null;
		}
		catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}


}
