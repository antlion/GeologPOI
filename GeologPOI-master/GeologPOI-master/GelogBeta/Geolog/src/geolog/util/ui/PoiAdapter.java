package geolog.util.ui;


import geolog.managers.PoiManager;
import geolog.util.AuthGoogle;

import java.util.ArrayList;
import java.util.Date;

import com.geolog.activity.R;
import com.geolog.dominio.Poi;
import com.geolog.dominio.web.ConfrimResponse;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Adattatore dei poi. Utilizzato per popolare una listView di poi. Questo
 * adattatore contiente una lista di poi e per ogni poi, un immagine che lo
 * rappresenta. Viene visualizzata la distanza tra il poi e la posizione
 * dell'utente
 * 
 * @author Lorenzo
 */
public class PoiAdapter extends BaseAdapter {

	// Contesto dell'attività che ha chiamato il servizio
	/**
	 * @uml.property  name="context"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private Context context;
	// locazione dell'utente
	/**
	 * @uml.property  name="myLocation"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private Location myLocation;
	// lista dei poi
	/**
	 * @uml.property  name="pois"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="com.geolog.dominio.Poi"
	 */
	private ArrayList<Poi> pois;
	// Immagine per la segnalazione
	/**
	 * @uml.property  name="suggestionView"
	 * @uml.associationEnd  
	 */
	private ImageView suggestionView;

	/**
	 * Costruttore della classe
	 * 
	 * @param context
	 *            contesto dell'attività che ha chiamato il servizio
	 * @param pois
	 *            lista dei po
	 * @param myLocation
	 *            locazione dell'utente
	 */
	public PoiAdapter(Context context, ArrayList<Poi> pois, Location myLocation) {
		this.context = context;
		this.pois = pois;
		this.myLocation = myLocation;
	}

	public int getCount() {
		return pois.size();
	}

	public Object getItem(int position) {
		return pois.get(position);
	}

	public long getItemId(int position) {
		return pois.get(position).hashCode();
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.riga_lista_gestore_lista,
					null);
		}
		
		Typeface myTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/bebe.otf");
	   
	 
		
		// Visualizzo il nome del poi
		TextView poiName = (TextView) convertView.findViewById(R.id.nomePOI);
		poiName.setText(pois.get(position).getNome());
		poiName.setTypeface(myTypeface);
		// visualizzo la categoria del poi
		TextView categoryName = (TextView) convertView
				.findViewById(R.id.categoriaPOI);
		categoryName.setText(pois.get(position).getCategory()
				.getNomeCategoria());
		categoryName.setTypeface(myTypeface);
		// Visualizzo l'immagine del poi
		ImageView poiImage = (ImageView) convertView
				.findViewById(R.id.poi_image);
		// Scarico la risorsea del poi
		pois.get(position).setImageFromResource(context);
		// Prendo l'immagine scaricata
		Drawable drawablePoiImage = pois.get(position).setImageFromResource(
				context);
		// controllo che l'immagine sia corretta
		if (drawablePoiImage == null)
			// altrimenti uso un immagine di base
			poiImage.setImageResource(R.drawable.no_image_aviable);
		else
			// setto l'immagine scaricata
			poiImage.setImageDrawable(drawablePoiImage);

		// Creo un listenr sull'immagine della segnalazione
		suggestionView = (ImageView) convertView
				.findViewById(R.id.imageSuggestion);
		suggestionView.setClickable(true);
		suggestionView.setOnClickListener(new OnClickListener() {

			public void onClick(final View v) {
				// TODO Auto-generated method stub

				// creo un nuovo dialogo per la segnalazione
				AlertDialog.Builder suggestionDialog = new AlertDialog.Builder(
						context);
				suggestionDialog.setIcon(R.drawable.ex_mark2);
				suggestionDialog.setTitle("Segnalazione POI");
				// Prendo il riferimento all'edit text per controllare le
				// informazioni immesse dall'utente
				final EditText input = new EditText(context);
				input.setHint("inserisci la descrizione della segnalazione");
				suggestionDialog.setView(input);
				suggestionDialog.setPositiveButton("Invia",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

								// Quando l'immagine delle segnalazione viene
								// premuta, prendo il testo inserito dall'utente
								String descrption = input.getText().toString();
								Log.d("edit", descrption);
								// Creo una nuova segnalazione
								addSuggestion(pois.get(position), descrption,
										new Date(), v.getContext());
							}
						});

				suggestionDialog.setNegativeButton("Chiudi",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// Canceled.
							}
						});

				suggestionDialog.show();

			}
		});
		// Calcolo la distanza tra il poi e l'utente
		int distanceBetweenUserAndPoi = (int) myLocation.distanceTo(pois.get(
				position).getPOILocation());
		// visualizzo la distanza tra l'utente e il poi
		TextView viewDistance = (TextView) convertView
				.findViewById(R.id.distance);
		viewDistance.setText(String.valueOf("Distanza: "
				+ distanceBetweenUserAndPoi + " metri"));
		viewDistance.setTypeface(myTypeface);
		return convertView;

	}

	/**
	 * Aggiunge una segnlazione al sistema
	 * 
	 * @param poiBase
	 *            poi su cui viene fatta la segnalazione
	 * @param description
	 *            descrizione della segnalazione
	 * @param date
	 *            data di creazione della segnalazione
	 * @param context
	 *            contesto dell'attività che richiama il metodo
	 */
	public void addSuggestion(final Poi poiBase, final String description,
			final Date date, final Context context) {

		final AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
			// barra di progresso
			ProgressDialog dialog;
			// risposta di conferma del servizio web
			private ConfrimResponse response;

			protected void onPreExecute() {

				dialog = ProgressDialog.show(context, "Attendere...",
						"Invio Segnalazione");

			}

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				// Contatto il servizio web per aggiungere la segnalazione
				response = PoiManager.suggestionPoi(poiBase, description, date,
						AuthGoogle.getAccountName((Activity) context), context);
				return null;

			}

			protected void onPostExecute(String result) {
				// Chiudo la barra di progresso
				dialog.dismiss();
				// Se la risposta del server è nulla o errata, visualizzo un
				// messaggio d'errore,altrimenti un messaggio positivo
				if (response == null || response.getStatus() != 200) {
					UtilDialog.createBaseToast("Segnalazione non inviata",
							context).show();
				} else
					UtilDialog
							.createBaseToast("Segnalazione iniviata", context)
							.show();

			}
		};
		task.execute((Void[]) null);

	}
}
