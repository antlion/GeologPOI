package geolog.util;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.geolog.AddPoiActivity;
import com.geolog.PoiManager;
import com.geolog.R;

import com.geolog.dominio.Poi;
import com.geolog.web.domain.ConfrimResponse;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

/**
 * 
 * Oggetti visibili sulla mappa. Gli oggetti sono posizionati su overlay della
 * mappa.
 * 
 * @author Lorenzo
 * 
 */
@SuppressWarnings("rawtypes")
public class PositionOverlay extends ItemizedOverlay {

	// Poi item
	private Poi poi;
	// contesto dell'attività che usa l'overlay
	private Context context;
	// Lista degli overlayItem
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	// Overlay presenti sulla mappa
	private List<Overlay> mapOverlays;
	// Visualizzatore della mappa
	private MapView mapView;
	// Locazione dell'utente
	private Location myLocation;
	// controller della mappa
	private MapController mapController;

	/**
	 * Costruttore della classe
	 * 
	 * @param defaultMarker
	 *            icona del poi
	 * @param context
	 *            contesto dell'attività
	 * @param poi
	 *            poi
	 * @param mapOverlays
	 *            overlay della mappa
	 * @param mapView
	 *            visualizzatore della mappa
	 * @param myLocation
	 *            locazione della mappa
	 * @param mapController
	 *            controller della mappa
	 */
	public PositionOverlay(Drawable defaultMarker, Context context, Poi poi,
			List<Overlay> mapOverlays, MapView mapView, Location myLocation,
			MapController mapController) {
		super(boundCenterBottom(defaultMarker));
		// TODO Auto-generated constructor stub
		this.context = context;
		this.poi = poi;
		this.mapOverlays = mapOverlays;
		this.mapView = mapView;
		this.myLocation = myLocation;
		this.mapController = mapController;
	}

	@Override
	protected OverlayItem createItem(int i) {
		// TODO Auto-generated method stub
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return mOverlays.size();
	}

	public void addOverlay(OverlayItem overlay) {
		mOverlays.add(overlay);
		populate();
	}

	@Override
	public boolean onTap(final GeoPoint p, MapView mapView) {
		// If it was the parent that was tapped, do nothing
		if (super.onTap(p, mapView)) {

			return true;
		}

		// Quando l'utennte clicca sulla mappa, viene chiesto di aggiungere un
		// poi tramite un dialofo
		AlertDialog.Builder alert = new AlertDialog.Builder(context);
		alert.setIcon(R.drawable.ex_mark2);
		alert.setTitle("AggiungiPOI");
		alert.setMessage("Vuoi aggiungere un punto di intresse in questa posizione?");
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				// Se l'utente clicca ok, viene creata la locazione in cui
				// aggiungere il poi
				Location location = new Location("nuova");
				location.setLatitude(p.getLatitudeE6() / 1E6);
				location.setLongitude(p.getLongitudeE6() / 1E6);

				// Viene aggiunta la locazione come paramentro tra attività
				ParametersBridge.getInstance().addParameter("Location",
						location);
				// viene aperta l'attività dell'aggiunta poi
				Intent intent = new Intent(context, AddPoiActivity.class);
				context.startActivity(intent);
			}
		});

		alert.setNegativeButton("Chiudi",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Close dialog
					}
				});

		alert.show();
		return true;

	}

	/**
	 * Creazione di un Geopoint a partire da una locazione
	 * 
	 * @param location
	 *            una locazione
	 * @return GeoPoint un geopoint della locazione passata come paramentro
	 */
	public GeoPoint createNewGeoPoint(Location location) {
		double latitude = location.getLatitude();
		double longitude = location.getLongitude();

		int latitudeE6 = (int) Math.floor(latitude * 1.0E6);
		int longitudeE6 = (int) Math.floor(longitude * 1.0E6);

		GeoPoint geoPoint = new GeoPoint(latitudeE6, longitudeE6);

		return geoPoint;
	}

	@Override
	protected boolean onTap(final int index) {

		// Se l'utente clicca su un overlay item, viene presa la sua posizione
		// nell'array di overlay
		OverlayItem item = mOverlays.get(index);
		// Se l'item corrisponde alla posizione dell'utente viene mostrato un
		// messaggio relativo alla posizione dell'utente,altrimenti
		// un messaggio relativo al poi toccato
		if (item.getTitle().equals("MiaPosizione")) {
			// elimina l'overlay dell'utente
			mapOverlays.remove(index);
			// prende il layout della GUI
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			// Creo una nuova view
			final View popUp = inflater
					.inflate(R.layout.ballon, mapView, false);
			// Posiziono la view
			MapView.LayoutParams mapParams = new MapView.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT, mOverlays.get(index)
							.getPoint(), MapView.LayoutParams.BOTTOM_CENTER);
			// aggiungo la view alla mappa
			mapView.addView(popUp, mapParams);

			// Visualizzo la posizione dell'utente
			final TextView myPosition = (TextView) popUp
					.findViewById(R.id.balloon_item_title);
			myPosition.setText("MiaPosizione");

			// Aggiungo al listenr l'immagine che corrisponde alla chiusura
			// della view creata
			final ImageView image = (ImageView) popUp
					.findViewById(R.id.balloon_close);
			image.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					// Rimuovo la view
					mapView.removeView(popUp);
					// Creo di nuovo la view per l'utente
					Drawable drawable = v.getResources().getDrawable(
							R.drawable.user_position_on_map);
					PositionOverlay itemizedoverlay = new PositionOverlay(
							drawable, v.getContext(), null, mapOverlays,
							mapView, myLocation, mapController);
					OverlayItem overlayitem = new OverlayItem(
							createNewGeoPoint(myLocation), "MiaPosizione",
							"I'm there");
					itemizedoverlay.addOverlay(overlayitem);
					mapOverlays.add(itemizedoverlay);
					mapController.setCenter(createNewGeoPoint(myLocation));
				}
			});

		} else {
			// prende il layout della GUI
			final LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// Creo una nuova view
			final View popUp = inflater.inflate(R.layout.ballon_poi, mapView,
					false);
			// Posiziono la view
			MapView.LayoutParams mapParams = new MapView.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT, mOverlays.get(index)
							.getPoint(), MapView.LayoutParams.BOTTOM_CENTER);
			// aggiungo la view alla mappa
			mapView.addView(popUp, mapParams);

			// visualizzo il nome del poi
			final TextView textNamePoi = (TextView) popUp
					.findViewById(R.id.balloon_item_title);
			textNamePoi.setText(poi.getNome());

			// Visualizzo la desscrizione del poi
			final TextView descrizione = (TextView) popUp
					.findViewById(R.id.descrizione);
			descrizione.setText(poi.getDescrizione());

			// Aggiungo al lsitner l'immagine di chiusura della view
			final ImageView image = (ImageView) popUp
					.findViewById(R.id.balloon_close);
			image.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					// Chiudi la view
					mapView.removeView(popUp);

				}
			});

			// Aggiungo al listenr l'immagine di aggiungi segnalazione
			final ImageView segnalazione = (ImageView) popUp
					.findViewById(R.id.segnalzione);
			segnalazione.setOnClickListener(new OnClickListener() {
				public void onClick(final View v) {
					// chiudo la precedente view
					mapView.removeView(popUp);
					
					// Creo un nuovo dialogo per aggiungere la segnalazione
					AlertDialog.Builder alert = new AlertDialog.Builder(context);
					alert.setIcon(R.drawable.ex_mark2);
					alert.setTitle("Segnalazione POI");

					// Set an EditText view to get user input
					final EditText input = new EditText(context);
					input.setHint("inserisci la descrizione della segnalazione");
					alert.setView(input);
					alert.setPositiveButton("Invia",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									// Prendo la descrizione della segnalazione
									// dell'utente
									String descrption = input.getText()
											.toString();
									// Aggiungo la segnalazione al sistema
									addSuggestion(poi, descrption, new Date(),
											v.getContext());
								}
							});

					alert.setNegativeButton("Chiudi",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									// Canceled.
								}
							});

					alert.show();

				}
			});

		}
		return true;
	}

	public void addSuggestion(final Poi poiBase, final String description,
			final Date date, final Context context) {

		final AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
			// Progress Dialog
			ProgressDialog dialog;
			// Risposta di conferma del servizio web
			private ConfrimResponse response;

			protected void onPreExecute() {

				dialog = ProgressDialog.show(context, "Attendere...",
						"Invio Segnalazione");

			}

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				// Chiedo al servizio web di aggiungere la segnalzione
				response = PoiManager.suggestionPoi(poiBase, description, date,
						"io", context);
				return null;

			}

			protected void onPostExecute(String result) {
				// Chiudo la barra di progresso
				dialog.dismiss();
				// controllo la risposta del servizio web. Se la risposta è
				// nulla o errata, mostro un messaggio d'errore, altimenti un
				// messaggio positivo
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