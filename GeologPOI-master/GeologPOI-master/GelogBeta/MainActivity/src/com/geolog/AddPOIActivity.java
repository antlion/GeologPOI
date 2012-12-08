package com.geolog;



import java.io.ByteArrayOutputStream;
import java.net.CacheResponse;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import prova2.WSs;

import com.geolog.dominio.Category;
import com.geolog.dominio.Poi;
import com.geolog.util.AuthGoogle;
import com.geolog.util.ResourcesHandler;
import com.geolog.util.UtilDialog;
import com.geolog.web.Services;
import com.geolog.web.domain.ConfrimResponse;
import com.geolog.web.domain.PoiListResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;



import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
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
	private Bitmap photo;
	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// aaaaa();
		setContentView(R.layout.add_poi_activity_layout);
		
		context = this;
		mylocation = null;
		gestoreCategorie = new CategoryHandler();
		hasLocation = false;
		EditText et1 = (EditText) findViewById(R.id.editText1);
		EditText et2 = (EditText) findViewById(R.id.editText2);
		Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		Button button = (Button) findViewById(R.id.ok);
		button.setOnClickListener(this);
		

		
	
		
		
		
		
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				this,
				android.R.layout.simple_spinner_item,
				ottieniCategorie()
				);
		//android.R.layout.simple_dropdown_item_1line
		adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		spinner.setAdapter(adapter);

		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);

		actionBar.addAction(new Action() {
			public void performAction(View view) {
				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);  
				startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
				
			}

			public int getDrawable() {
				return R.drawable.camera_icon;
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
				locationManager.removeUpdates(myLocationListener);
				Date date = new Date();
				EditText et1 = (EditText) findViewById(R.id.editText1);
				EditText et2 = (EditText) findViewById(R.id.editText2);
				Spinner spinner = (Spinner) findViewById(R.id.spinner1);
				CategoryHandler categoryHandler = new CategoryHandler();
				int id = categoryHandler.getCategoryIdFromSource(spinner.getSelectedItem().toString());
				Poi poi = HandlerPOI.creaPOI(mylocation, new Category(spinner.getSelectedItem().toString(), id,0), et1.getText().toString(), et2.getText().toString(), 0, date);
				Services servces = new Services();
				ConfrimResponse aaa = (ConfrimResponse) servces.addPOI(poi, context);
				
				if (HandlerPOI.addPOI(poi, context) == false)
				{
					UtilDialog.alertDialog(context, "Impossibile aggiungere il Poi");
				}
				else
					UtilDialog.createBaseToast("Poi aggiunto con successo", context);
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
		// TODO Auto-generated method stub�
		
		if (v.getId() == R.id.ok){
			if (checkUserInput()){
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
	}

	public boolean checkUserInput()
	{
		EditText et1 = (EditText) findViewById(R.id.editText1);
		EditText et2 = (EditText) findViewById(R.id.editText2);
		Spinner spinner = (Spinner) findViewById(R.id.spinner1);

		if (et1.getText().length() >0 && et2.getText().length() >0 && spinner.getSelectedItem()!=null)
		{
			System.out.println(spinner.getSelectedItem().toString());
			return true;
		}
		return false;
	}


	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
		if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) {  
			
			//L'immagine catturata dalla fotocamera viene visualizzata in anteprima sullo schermo.
			ImageView cameraPic = (ImageView) findViewById(R.id.imagePic);
			photo = (Bitmap) data.getExtras().get("data"); 
			cameraPic.setImageBitmap(photo);
			
		
			
			//json.put(WebConstant.JSON_PUT_PROPERTY_BUZZ_IMAGE, base64Image);
			/*WSs service = new WSs();
			service.setTimeOut(10000000);
			service.setUrl("http://160.80.135.31:8080/GeologWeb/services/WS");
			String res = service.upload(12, "image/jpeg", ResourcesHandler.covertBitmapToByte(photo));
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			cameraPic.setImageResource(R.drawable.arrow_down);
			ConfrimResponse responseWeb= gson.fromJson(res, ConfrimResponse.class);
			System.out.println(responseWeb.getResult());
			Services services2= new Services();
			Drawable d = services2.downloadResource("http://160.80.135.31:8080"+responseWeb.getResult(), context);
			cameraPic.setImageDrawable(d);*/
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



	public void aaaaa()
	{
		AccountManager manager = AccountManager.get(this);
		Account[] accounts = manager.getAccountsByType("com.google");
		manager.getAuthToken(AuthGoogle.googleServiceAviable(this), "�Manage your tasks�", null, this, new AccountManagerCallback<Bundle>() {
			public void run(AccountManagerFuture<Bundle> future) {
				try {
					// If the user has authorized your application to use the tasks API
					// a token is available.
					String token = future.getResult().getString(AccountManager.KEY_AUTHTOKEN);
					// Now you can use the Tasks API...
					// Setting up the Tasks API Service

				} catch (OperationCanceledException e) {
					// TODO: The user has denied you access to the API, you should handle that
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, null);
	}

}
