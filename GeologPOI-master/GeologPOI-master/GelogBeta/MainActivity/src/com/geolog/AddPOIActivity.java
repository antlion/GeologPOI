package com.geolog;





import java.util.Calendar;
import java.util.Date;

import com.geolog.R;
import com.geolog.dominio.Poi;
import com.geolog.util.ParametersBridge;
import com.geolog.util.ResourcesHandler;
import com.geolog.util.UtilDialog;
import com.geolog.web.domain.ConfrimResponse;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;


/**
 * @author Lorenzo
 * 
 * Classe che gestisce l'aggiunta di un poi al sistema. L'utente tramite l'inserimento di opportune informazioni attraverso l'interfaccia grafica,
 * può aggiungere il poi nel sistema. Inoltre, l'utente può scattare una foto rappresentativa del poi da ggiungere come corredo alle informazioni sul 
 * poi stesso.
 *
 */
@SuppressLint("HandlerLeak")
public class AddPOIActivity extends Activity implements OnClickListener, android.view.View.OnClickListener{

	//Variabile statica che contiene il codice per accedere alla fotocamera del dispositivo mobile
	private static final int CAMERA_PIC_REQUEST = 1337; 

	//Gestore delle categoire
	private CategoriesManager categoriesHandler;

	//La locazione in cui si trova l'utente
	private Location mylocation;

	//Manager delle locazioni
	private LocationManager locationManager;

	//Listner delle locazioni
	private LocationListener myLocationListener;

	//Stato della locazione dell'utente
	private boolean hasLocation; 

	//Contesto dell'attività
	private Context context;

	//Foto scattata dall'utente
	private Bitmap myPhoto;

	//Risposta del servizio Web
	private ConfrimResponse webResponse;

	//Stato delle risorse aggiunte
	private boolean resourceAdded = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.add_poi_activity_layout);

		//Salvataggio del contesto
		context = this;

		//Acquisizione della locazione da altre attività
		ParametersBridge bridge = ParametersBridge.getInstance();

		//Aggiornamento della posizione dell'utente
		mylocation= (Location)bridge.getParameter("Location");

		//Inzializzazione del gestore delle categorie
		categoriesHandler = CategoriesManager.getCategoriesManager();

		//stato della locazione
		hasLocation = false;

		//Inizializzazione dello spinner della GUI
		Spinner spinner = (Spinner) findViewById(R.id.spinner1);

		//Inzializzazione del bottone per l'aggiunta del poi
		Button button = (Button) findViewById(R.id.ok);

		//Aggiungo il bottone ad un listner
		button.setOnClickListener(this);

		//creazione di un nuovo adattatore, contenente il nome di tutte le categorie
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,categoriesHandler.getCategoriesName());

		//Selezione il tipo di visualizzazione dell'adapter
		adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

		//set dell'adapter
		spinner.setAdapter(adapter);

		//ActionBar dell'applicazione
		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);

		//Aggiungo all'actionBar l'azione di scatta fotografia
		actionBar.addAction(new Action() {
			public void performAction(View view) {

				//Start di una nuova attività per lo scatto della fotografia
				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);  
				startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);

			}

			//Definisco un'icona per l'azione di scatta fotografia
			public int getDrawable() {
				return R.drawable.camera_icon;
			}
		});


		//Inizializzo il locationManager
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		//Inizializzo il locationListner
		myLocationListener = new LocationListener() { 


			public void onStatusChanged(String provider, int status, Bundle extras) { 

			} 
			public void onProviderEnabled(String provider) { 

			} 

			public void onProviderDisabled(String provider) { 

			} 


			public void onLocationChanged(Location location) {

				//Aggiorno la posizione dell'utente
				mylocation = location;

				//aggiorno lo stato della locazione
				hasLocation = true;

				//rimuovo il locationManager dal listener
				locationManager.removeUpdates(myLocationListener);

			} 
		};





	}



	public void onClick(View v) {
		// TODO Auto-generated method stubù

		//Se l'utente ha premuto il pulsante ok, si procede ad aggiungere il poi al sistema
		if (v.getId() == R.id.ok){
			//Vengono controllate se le informazioni riguardo al poi da aggiungere sono corrette. Se l'operazione va a buon fine,
			//viene cercata la posizione dell'utente
			if (checkUserInput()){

				//Se il gps non è supportato,viene mostrato un messaggi odi errore e l'attività viene interrotta,altrimenti viene ricercata la posizione
				if ( locationManager.getProvider(LocationManager.GPS_PROVIDER) == null){

					UtilDialog.createAlertNoGps(this).show();
					finish();
				}

				//Se il gps è abilitato, si procede all'acquiszione delle cordinate gps dell'utente, 
				//altrimenti viene richesto all'utente di abilitarlo
				if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) == true)
				{
					//Se la locazione dell'utente non è ancora stata trovata, viene ricercata, altrimenti si procede ad aggiungere il poi
					if ( mylocation == null)

						//Richiesta delle cordinate gps
						locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 1, myLocationListener);

					else
						//La locazione è stata trovata
						hasLocation = true;

					//Creo una nuova barra di progresso	
					final ProgressDialog progress = new ProgressDialog(this);
					progress.setIndeterminate(true);
					progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					progress.setTitle("Attendere");
					progress.setMessage("Inserisco POI");
					progress.show();

					//Creo un gestore della progressBar
					final Handler progressBarHandler = new Handler() {
						@Override
						public void handleMessage(Message msg) {
							//Termino la progressBar
							progress.dismiss();

							//Se la location dell'utente non è stata trovata, restituisco un messaggio d'errore
							if (hasLocation == false)

								UtilDialog.alertDialog(context, "Impossibile stabilire posizione").show();
							//Se l'aggiunta del poi non è andata a buon fine, resituisco un messaggio di errore, altimenti un messaggio positivo
							else  if (webResponse == null){

								UtilDialog.alertDialog(context, "Impossibile insrire il poi").show();
							}
							else{

								UtilDialog.alertDialog(context, "POI aggiunto con successo").show();
							}
							//Se è stata scattata una foto e non è stata aggiunta, viene mostrato un messaggio d'errore
							if (myPhoto != null && resourceAdded == false)
							{
								UtilDialog.alertDialog(context, "Impossibile aggiungere foto").show();
							}
						}
					};
					//Creo un nuovo thread per la gestione dell'aggiunta del POI
					Thread threadAddPoi = new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub

							//Attendo per 15 secondi la ricerca di una posizione. Se durante questo tempo non viene trovata, termino l'esecuzione del thread
							Long t = Calendar.getInstance().getTimeInMillis();
							while (hasLocation == false && Calendar.getInstance().getTimeInMillis() - t < 15000) {
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							};

							//Controllo se è stata trovata una locazione, in caso positvo, procedo ad aggiungere il poi
							if (hasLocation){

								///Ottengo le informazioni che ha aggiunto l'utente sul poi
								final EditText et1 = (EditText) findViewById(R.id.editText1);
								final EditText et2 = (EditText) findViewById(R.id.editText2);
								Spinner spinner = (Spinner) findViewById(R.id.spinner1);
								//Creo una nuova data per l'inserimento del poi
								Date date = new Date();

								//Ottengo la categoria scelta dall'utente per il poi da aggiungere
								int id = categoriesHandler.getCategoryIdFromSource(spinner.getSelectedItem().toString());

								//Creo un nuovo poi con le informazioni ottenute
								final Poi poi = PoiManager.createNewPoi(mylocation, id, et1.getText().toString(), et2.getText().toString(),  date);

								//Uso il servizio web per aggiungere il poi al sistema
								webResponse = PoiManager.addPOI(poi, context, "io");

								//Se la risposta del servizio web è nulla o c'è stato un errore, l'esecuzione del thread termina,
								//altrimenti procedo a caricare l'immagine scattata dalla fotocamera
								if ( webResponse == null || webResponse.getStatus() != 200){

									progressBarHandler.sendEmptyMessage(0);
								}
								else {

									//recupero l'id del poi appena creato
									int idPOI = Integer.parseInt(webResponse.getResult());

									//se la foto non è stata scattata, termino l'esecuzione, altrimento procedo all'upload
									if (myPhoto != null){

										//contatto il servizio web per caricare la foto
										webResponse = PoiManager.uploadPoiResource(idPOI, context, ResourcesHandler.covertBitmapToByte(myPhoto), "image/jpeg");

										//Se il caricamento è avvenuto con successo, aggiorno l ostato delle risorse e termino il thread
										if (webResponse == null || webResponse.getStatus() != 200)

											progressBarHandler.sendEmptyMessage(0);
										else {
											resourceAdded = true;
											progressBarHandler.sendEmptyMessage(0);
										}
									}
									else{
										progressBarHandler.sendEmptyMessage(0);
									}
								}

							}
							else {
								progressBarHandler.sendEmptyMessage(0);}
						}
					});
					threadAddPoi.start();

				}
				else {
					UtilDialog.createAlertNoGps(this).show();

				}

			}
			else
				UtilDialog.createBaseToast("I campi non devono essere vuoti", getApplicationContext()).show();
		}
	}





	/**
	 * Metodo che controlla il corretto inserimento delle informazioni aggiunte dall'utente per la creazione
	 * di un nuovo poi.
	 * @return boolean true, se le informazioni inserite sono corrette,false altrimenti.
	 */
	public boolean checkUserInput()
	{
		//Ottengo i campi in cui sono contenute le informazioni
		EditText et1 = (EditText) findViewById(R.id.editText1);
		EditText et2 = (EditText) findViewById(R.id.editText2);
		Spinner spinner = (Spinner) findViewById(R.id.spinner1);

		//Se i campi non sono vuoti, termino con esito positivo, altrimenti termino con esito negativo
		if (et1.getText().length() >0 && et2.getText().length() >0 && spinner.getSelectedItem()!=null)
		{
			return true;
		}

		return false;
	}


	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
		
		//Se l'utente ha rihiesto di scattare una foto e l'operazione è andata a buon fine, si procede alla visualizzazione della foto
		//e al suo salvataggio.
		
		if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) {  

			//Viene salvata una copia dell'immagine catturata
			myPhoto = (Bitmap) data.getExtras().get("data"); 
			
			//L'immagine catturata dalla fotocamera viene visualizzata in anteprima sullo schermo.
			ImageView cameraPic = (ImageView) findViewById(R.id.imagePic);
			
			cameraPic.setImageBitmap(myPhoto);
		}  
	}



	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		
	} 


}


