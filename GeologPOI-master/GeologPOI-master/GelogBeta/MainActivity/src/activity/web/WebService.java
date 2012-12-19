package activity.web;

import java.util.Calendar;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.neurospeech.wsclient.SoapFaultException;

import prova.WS;
import activity.dominio.Category;
import activity.dominio.Suggestion;
import activity.util.UtilDialog;
import activity.web.domain.BaseResponse;
import activity.web.domain.PoiListResponse;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

public class WebService {
	
	private static final String WEB_SERVICE_URL = "http://160.80.135.31:8080/GeologWeb/services/WS";
	private Context context;
	private PoiListResponse response;
	public WebService (Context context) {
		this.context = context;
		response = null;
	}
	
	public PoiListResponse findNearby(Location location, String category)
	{
		final String aaa;
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
			 	nuovo.setBaseUrl(WEB_SERVICE_URL); 
	          try {
	        	 
				//String responseFromServices = nuovo.  (41.45435, 22.232131, 8);
				GsonBuilder builder = new GsonBuilder();
				Gson gson = builder.create();
				response = gson.fromJson("dddfs", PoiListResponse.class);
			 //  Log.d("numeroPois",String.valueOf(pois.getCount()));
				
				
				
			} catch(NullPointerException e)
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
            		UtilDialog.alertDialog(context, "Impossibile stabilire la conessione con il server").show();
            		
            	}
            	
            	
            	
	            }
		
		
		
		
		};
		task.execute(null);
		return response;
	}
	
	
	
	
	
	

}
