package com.geolog;



import java.util.ArrayList;
import java.util.Calendar;

import com.geolog.util.CategoryHandler;
import com.geolog.util.UtilDialog;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class AddPOIActivity extends Activity implements OnClickListener, android.view.View.OnClickListener{
	
	private CategoryHandler gestoreCategorie;
    private static final int CAMERA_PIC_REQUEST = 1337;  
	private Location mylocation;
	private LocationManager locationManager;
	private LocationListener myLocationListener;
	private boolean hasLocation; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aggiunta_poi_activity_layout);
        mylocation = null;
        gestoreCategorie = new CategoryHandler();
        hasLocation = false;
        EditText et1 = (EditText) findViewById(R.id.editText1);
        EditText et2 = (EditText) findViewById(R.id.editText2);
        Spinner spinner = (Spinner) findViewById(R.id.spinner1);
        Button button = (Button) findViewById(R.id.ok);
        button.setOnClickListener(this);
        Button cameraShot = (Button) findViewById(R.id.cameraShot);
        cameraShot.setOnClickListener(this);
       
   ArrayAdapter<String> adapter = new ArrayAdapter<String>(
        		this,
        		android.R.layout.simple_spinner_item,
        		ottieniCategorie()
        		);
   spinner.setAdapter(adapter);
   
   ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
  actionBar.
   actionBar.addAction(new Action() {
       public void performAction(View view) {
          // Toast.makeText(HomeActivity.this, "Added action.", Toast.LENGTH_SHORT).show();
       }
       public String getName()
       {
    	   return "Aggiungi";
       }
       public int getDrawable() {
           return R.drawable.icon;
       }
   });
   
   
   
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
			//invia i dati a cpuis
			//controlla dati
			Log.d("Locazione",location.toString());
			mylocation = location;
			hasLocation = true;
		} 
		 };
   
   
   
   
	}
	
	public String[] ottieniCategorie()
	{
		ArrayList<String> categorie = new ArrayList<String>();
		categorie = gestoreCategorie.ottieniNomiCategorie();
		String[] nomi = new String[categorie.size()];
		int count=0;
		for ( String categoria: categorie){
			nomi[count] = categoria;
			count++;
		}
		return nomi;
	}

	public void onClick(View v) {
		// TODO Auto-generated method stubù
		if (v.getId() == R.id.cameraShot)
		{
		    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);  
		    startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
		}
		if (v.getId() == R.id.ok){
			//controlla campi
			if ( locationManager.getProvider(LocationManager.GPS_PROVIDER) == null){
	        	UtilDialog.createAlertNoGps(this).show();
	        	finish();
				}
		
				if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) == true)
				{
					getLocation(this);
					
			       	     
				}
				else {
					UtilDialog.createAlertNoGps(this).show();
					
				}
			
			
		}
	}

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) {  
            // do something  
        	ImageView cameraPic = (ImageView) findViewById(R.id.imagePic);
        	Bitmap photo = (Bitmap) data.getExtras().get("data"); 
            cameraPic.setImageBitmap(photo);
        }  
    }  
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		
	}

	public void getLocation(final Context context){
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
		    		
		    		
		    		}
		            else {
		            	hasLocation = false;
		            	//invia i dati a cupis
		            }
		        }
			
		};
		task.execute(null);
	
		}
	
	
	
	
	
}
