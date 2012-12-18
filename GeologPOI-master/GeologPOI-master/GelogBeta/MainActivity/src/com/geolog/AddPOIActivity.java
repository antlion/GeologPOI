package com.geolog;



import java.io.ByteArrayOutputStream;
import java.net.CacheResponse;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.simple.JSONObject;

import prova2.WSs;

import com.geolog.dominio.Category;
import com.geolog.dominio.Poi;
import com.geolog.util.AuthGoogle;
import com.geolog.util.ParametersBridge;
import com.geolog.util.ResourcesHandler;
import com.geolog.util.UtilDialog;
import com.geolog.web.Services;
import com.geolog.web.domain.CategoryListResponse;
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
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
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

	private CategoriesHandler categoryHandler;
	private static final int CAMERA_PIC_REQUEST = 1337;  
	private Location mylocation;
	private LocationManager locationManager;
	private LocationListener myLocationListener;
	private boolean hasLocation; 
	private Bitmap photo;
	private Context context;
	private Services services;
	private ProgressDialog progressBar;
	private int progressBarStatus;
	private Thread prova;
	private ConfrimResponse responseWeb;
	private boolean resourceAdded = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// aaaaa();
		setContentView(R.layout.add_poi_activity_layout);

		context = this;
		  ParametersBridge bridge = ParametersBridge.getInstance();
	        mylocation= (Location)bridge.getParameter("Location");
	      
	
		categoryHandler = CategoriesHandler.getGestoreCategorie();
		hasLocation = false;
		services = new Services();

		final EditText et1 = (EditText) findViewById(R.id.editText1);
		final EditText et2 = (EditText) findViewById(R.id.editText2);

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

			} 
		};


	
		

	}

	public String[] ottieniCategorie()
	{
		ArrayList<String> categorie = new ArrayList<String>();
		categorie = categoryHandler.ottieniNomiCategorie();
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

		if (v.getId() == R.id.ok){
			if (checkUserInput()){

				if ( locationManager.getProvider(LocationManager.GPS_PROVIDER) == null){
					UtilDialog.createAlertNoGps(this).show();
					finish();
				}

				if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) == true)
				{
					//Aspetto che venga presa la location
					if ( mylocation == null)
					locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 1, myLocationListener);
					else{
					hasLocation = true;}
					final ProgressDialog progress = new ProgressDialog(this);
					progress.setIndeterminate(true);
					progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					progress.setTitle("Attendere");
					progress.setMessage("Inserisco POI");
					progress.show();
					final Handler handler = new Handler() {
						@Override
						public void handleMessage(Message msg) {
							progress.dismiss();
							if (hasLocation == false)
								UtilDialog.alertDialog(context, "Impossibile stabilire posizione").show();

							else  if (responseWeb == null){
								UtilDialog.alertDialog(context, "Impossibile insrire il poi").show();
							}
							else{
							UtilDialog.alertDialog(context, "POI aggiunto con successo").show();
							}
						}
					};
					 Thread thread = new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							Long t = Calendar.getInstance().getTimeInMillis();
							while (hasLocation == false && Calendar.getInstance().getTimeInMillis() - t < 15000) {
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							};
							if (hasLocation){
								if (checkUserInput()){
									final EditText et1 = (EditText) findViewById(R.id.editText1);
									final EditText et2 = (EditText) findViewById(R.id.editText2);
									Date date = new Date();
									Spinner spinner = (Spinner) findViewById(R.id.spinner1);
									int id = categoryHandler.getCategoryIdFromSource(spinner.getSelectedItem().toString());
									final Poi poi = HandlerPOI.creaPOI(mylocation, id, et1.getText().toString(), et2.getText().toString(),  date);
									responseWeb = HandlerPOI.addPOI(poi, context, "io");
									if ( responseWeb == null || responseWeb.getStatus() != 200){
										handler.sendEmptyMessage(0);
										}
									else {
									int idPOI = Integer.parseInt(responseWeb.getResult());
								
									if (photo != null){
									responseWeb = (ConfrimResponse) HandlerPOI.uploadPoiResource(idPOI, context, ResourcesHandler.covertBitmapToByte(photo), "image/jpeg");
									if (responseWeb == null || responseWeb.getStatus() != 200)
										handler.sendEmptyMessage(0);
									else {
										handler.sendEmptyMessage(0);
									}
									}
									else{
										handler.sendEmptyMessage(0);
									}
								}
								}
								else
									handler.sendEmptyMessage(0);	
							}else
							{
								handler.sendEmptyMessage(0);}
						}
					});
					thread.start();

				}
				else {
					UtilDialog.createAlertNoGps(this).show();

				}

			}
			else
				UtilDialog.createBaseToast("I campi non devono essere vuoti", getApplicationContext()).show();
		}
	}





	public boolean checkUserInput()
	{
		EditText et1 = (EditText) findViewById(R.id.editText1);
		EditText et2 = (EditText) findViewById(R.id.editText2);
		Spinner spinner = (Spinner) findViewById(R.id.spinner1);

		if (et1.getText().length() >0 && et2.getText().length() >0 && spinner.getSelectedItem()!=null)
		{

			return true;
		}
		UtilDialog.createBaseToast("I campi non devono essere vuoti", context).show();
		return false;
	}


	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
		if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) {  

			//L'immagine catturata dalla fotocamera viene visualizzata in anteprima sullo schermo.
			ImageView cameraPic = (ImageView) findViewById(R.id.imagePic);
			photo = (Bitmap) data.getExtras().get("data"); 
			cameraPic.setImageBitmap(photo);




		}  
	}  
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub

	}



}


