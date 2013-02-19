package geolog.activities;


import geolog.managers.CategoriesManager;
import geolog.managers.PoiManager;
import geolog.managers.ResourcesManager;
import geolog.util.ParametersBridge;
import geolog.util.ui.UtilDialog;

import java.util.Calendar;
import java.util.Date;


import com.geolog.activity.R;
import com.geolog.dominio.web.ConfrimResponse;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

/**
 * @author Lorenzo
 * 
 *         Classe che gestisce l'aggiunta di un poi al sistema. L'utente tramite
 *         l'inserimento di opportune informazioni attraverso l'interfaccia
 *         grafica, puo' aggiungere il poi nel sistema. Inoltre, l'utente puo'
 *         scattare una foto rappresentativa del poi da ggiungere come corredo
 *         alle informazioni sul poi stesso.
 * 
 */
@SuppressLint("HandlerLeak")
public class AddPoiActivity extends Activity implements OnClickListener,
		android.view.View.OnClickListener {

	// Variabile statica che contiene il codice per accedere alla fotocamera del
	// dispositivo mobile
	private static final int CAMERA_PIC_REQUEST = 1337;

	// Gestore delle categoire
	/**
	 * @uml.property  name="categoriesHandler"
	 * @uml.associationEnd  
	 */
	private CategoriesManager categoriesHandler;

	// La locazione in cui si trova l'utente
	/**
	 * @uml.property  name="mylocation"
	 * @uml.associationEnd  
	 */
	private Location mylocation;

	// Manager delle locazioni
	/**
	 * @uml.property  name="locationManager"
	 * @uml.associationEnd  
	 */
	private LocationManager locationManager;

	// Listner delle locazioni
	/**
	 * @uml.property  name="myLocationListener"
	 * @uml.associationEnd  
	 */
	private LocationListener myLocationListener;

	// Stato della locazione dell'utente
	/**
	 * @uml.property  name="hasLocation"
	 */
	private boolean hasLocation;

	// Contesto dell'attivit�
	/**
	 * @uml.property  name="context"
	 * @uml.associationEnd  
	 */
	private Context context;

	// Foto scattata dall'utente
	/**
	 * @uml.property  name="myPhoto"
	 * @uml.associationEnd  
	 */
	private Bitmap myPhoto;

	private ConfrimResponse webResponse = null;
	
	private ConfrimResponse webResponseAddRes= null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.add_poi_activity_layout);

		// Salvataggio del contesto
		context = this;

		// Acquisizione della locazione da altre attivita'
		ParametersBridge bridge = ParametersBridge.getInstance();

		// Aggiornamento della posizione dell'utente
		mylocation = (Location) bridge.getParameter("Location");

		// Inzializzazione del gestore delle categorie
		categoriesHandler = CategoriesManager.getCategoriesManager();

		// stato della locazione
		hasLocation = false;

		// Inizializzazione dello spinner della GUI
		Spinner spinner = (Spinner) findViewById(R.id.spinner1);

		// Inzializzazione del bottone per l'aggiunta del poi
		Button button = (Button) findViewById(R.id.ok);

		// Aggiungo il bottone ad un listner
		button.setOnClickListener(this);

		// creazione di un nuovo adattatore contenente il nome di tutte le
		// categorie
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item,
				categoriesHandler.getCategoriesName());

		// Selezione il tipo di visualizzazione dell'adapter
		adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

		// set dell'adapter
		spinner.setAdapter(adapter);

		ImageView cameraPic = (ImageView) findViewById(R.id.imagePic);

		cameraPic.setImageDrawable(getResources().getDrawable(R.drawable.noimage));
		
		
		// ActionBar dell'applicazione
		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);

		// Aggiungo all'actionBar l'azione di scatta fotografia
		actionBar.addAction(new Action() {
			public void performAction(View view) {

				// Start di una nuova attivita' per lo scatto della fotografia
				Intent cameraIntent = new Intent(
						android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);

			}

			// Definisco un'icona per l'azione di scatta fotografia
			public int getDrawable() {
				return R.drawable.picture;
			}
		});

		// Inizializzo il locationManager
		locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);

		// Inizializzo il locationListner
		myLocationListener = new LocationListener() {

			public void onStatusChanged(String provider, int status,
					Bundle extras) {

			}

			public void onProviderEnabled(String provider) {

			}

			public void onProviderDisabled(String provider) {

			}

			public void onLocationChanged(Location location) {

				// Aggiorno la posizione dell'utente
				mylocation = location;

				// aggiorno lo stato della locazione
				hasLocation = true;

				// rimuovo il locationManager dal listener
				locationManager.removeUpdates(myLocationListener);

			}
		};

		
		
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub�

		// Se l'utente ha premuto il pulsante ok, si procede ad aggiungere il
		// poi al sistema
		if (v.getId() == R.id.ok) {
			// Vengono controllate se le informazioni riguardo al poi da
			// aggiungere sono corrette. Se l'operazione va a buon fine,
			// viene cercata la posizione dell'utente
			if (checkUserInput()) {
					sendPoi();

			} else
				UtilDialog.createBaseToast("I campi non devono essere vuoti",
						getApplicationContext()).show();
		}
	}

	/**
	 * Metodo che controlla il corretto inserimento delle informazioni aggiunte
	 * dall'utente per la creazione di un nuovo poi.
	 * 
	 * @return boolean true, se le informazioni inserite sono corrette,false
	 *         altrimenti.
	 */
	public boolean checkUserInput() {
		// Ottengo i campi in cui sono contenute le informazioni
		EditText et1 = (EditText) findViewById(R.id.editText1);
		EditText et2 = (EditText) findViewById(R.id.editText2);
		Spinner spinner = (Spinner) findViewById(R.id.spinner1);

		// Se i campi non sono vuoti, termino con esito positivo, altrimenti
		// termino con esito negativo
		if (et1.getText().length() > 0 && et2.getText().length() > 0
				&& spinner.getSelectedItem() != null) {
			return true;
		}

		return false;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		// Se l'utente ha rihiesto di scattare una foto e l'operazione � andata
		// a buon fine, si procede alla visualizzazione della foto
		// e al suo salvataggio.

		if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) {

			// Viene salvata una copia dell'immagine catturata
			myPhoto = (Bitmap) data.getExtras().get("data");

			// L'immagine catturata dalla fotocamera viene visualizzata in
			// anteprima sullo schermo.
			ImageView cameraPic = (ImageView) findViewById(R.id.imagePic);

			cameraPic.setImageBitmap(myPhoto);
		}
	}
	
	private void sendPoi()
	{
		AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
			// Creo una nuova progressDialog
			ProgressDialog dialog;
			

			@Override
			protected void onPreExecute() {
				locationManager.requestLocationUpdates(
						LocationManager.GPS_PROVIDER, 5, 1, myLocationListener);
			

				dialog = ProgressDialog.show(context, "Attendere...",
						"Invio poi in corso");
				
			}

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub

				Long t = Calendar.getInstance().getTimeInMillis();
				while (!hasLocation
						&& Calendar.getInstance().getTimeInMillis() - t < 15000) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if (hasLocation)
				{
					// /Ottengo le informazioni che ha aggiunto
					// l'utente sul poi
					final EditText et1 = (EditText) findViewById(R.id.editText1);
					final EditText et2 = (EditText) findViewById(R.id.editText2);
					Spinner spinner = (Spinner) findViewById(R.id.spinner1);
					// Creo una nuova data per l'inserimento del poi
					Date date = new Date();

					// Ottengo la categoria scelta dall'utente per
					// il poi da aggiungere
					int id = categoriesHandler
							.getCategoryIdFromSource(spinner
									.getSelectedItem().toString());

					// Creo un nuovo poi con le informazioni
					// ottenute
					
					

					// Uso il servizio web per aggiungere il poi al
					// sistema
					webResponse = PoiManager.addPOI(PoiManager.createNewPoi(
							mylocation, id, et1.getText()
							.toString(), et2.getText()
							.toString(), date), context,
							"io");

					// Se la risposta del servizio web � nulla o c'�
					// stato un errore, l'esecuzione del thread
					// termina,
					// altrimenti procedo a caricare l'immagine
					// scattata dalla fotocamera
					if (webResponse != null
							&& webResponse.getStatus() == 200) {

						// recupero l'id del poi appena creato
						int idPOI = Integer.parseInt(webResponse
								.getResult());

						// se la foto non � stata scattata, termino
						// l'esecuzione, altrimento procedo
						// all'upload
						if (myPhoto != null) {

							// contatto il servizio web per caricare
							// la foto
							webResponseAddRes = PoiManager
									.uploadPoiResource(
											idPOI,
											context,
											ResourcesManager
													.covertBitmapToByte(myPhoto),
											"image/jpeg");
								
					} 

						
					}

				
					}
				return null;
			}

			protected void onPostExecute(String result) {
				dialog.dismiss();
				if ( !hasLocation){
					UtilDialog.createBaseToast("impossibile stabilire posizione", context).show();
				}
				else if (webResponse != null && webResponse.getStatus() == 200){
					if (webResponseAddRes == null || webResponseAddRes.getStatus() != 200)
						UtilDialog.createBaseToast("poi aggiunto.Impossibile aggiungere la risorsa", context).show();
				}
				else{
					UtilDialog.createBaseToast("poi aggiunto con successo", context).show();
				}
				
				
			}

		};
		task.execute((Void[]) null);

	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub

	}

	public ConfrimResponse getWebResponse() {
		return webResponse;
	}

	public ConfrimResponse getWebResponseAddRes() {
		return webResponseAddRes;
	}

	public void setMyPhoto(Bitmap myPhoto) {
		this.myPhoto = myPhoto;
	}
	
	

}
