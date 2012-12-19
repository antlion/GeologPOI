package activity.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import activity.AddPOIActivity;
import activity.PoiManager;
import activity.dominio.Poi;
import activity.web.domain.ConfrimResponse;
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

import com.geolog.R;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;



public class PositionOverlay extends ItemizedOverlay {


	private Poi poi;

	private Context context;

	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	
	private List<Overlay> mapOverlays;
	
	private MapView mapView;
	
	private Location myLocation;
	
	private MapController mapController;
	
	public PositionOverlay(Drawable defaultMarker,Context context,Poi poi,List<Overlay> mapOverlays,MapView mapView,Location myLocation,MapController mapController) {
		super(boundCenterBottom(defaultMarker));
		// TODO Auto-generated constructor stub
		this.context = context;
		this.poi = poi;
		this.mapOverlays = mapOverlays;
		this.mapView = mapView;
		this.myLocation = myLocation;
		this.mapController= mapController;
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
	/*	public boolean onTouchEvent(MotionEvent event, MapView mapView) 
	    {   
	        //---when user lifts his finger---
	        if (event.getAction() == 1) {                
	            GeoPoint p = mapView.getProjection().fromPixels(
	                (int) event.getX(),
	                (int) event.getY());
	                Toast.makeText(getBaseContext(), 
	                    p.getLatitudeE6() / 1E6 + "," + 
	                    p.getLongitudeE6() /1E6 , 
	                    Toast.LENGTH_SHORT).show();
	        }                            
	        return false;
	    } */  
	@Override
	public boolean onTap(final GeoPoint p, MapView mapView) {
		// If it was the parent that was tapped, do nothing
		if(super.onTap(p, mapView)) {

			return true;
		}
		AlertDialog.Builder alert = new AlertDialog.Builder(context);

		alert.setIcon(R.drawable.ex_mark2);
		alert.setTitle("AggiungiPOI");
		alert.setMessage("Vuoi aggiungere un punto di intresse in questa posizione?");
		alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				Location location = new Location("nuova");
				location.setLatitude(p.getLatitudeE6() / 1E6);
				location.setLongitude(p.getLongitudeE6() /1E6);

				ParametersBridge.getInstance().addParameter("Location", location);
				Intent intent = new Intent(context, AddPOIActivity.class);
				context.startActivity(intent);
			}
		});

		alert.setNegativeButton("Chiudi", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled.
			}
		});

		alert.show();



		return true;

	}           
	
	public GeoPoint createNewGeoPoint (Location location)
	{
		double latitude = location.getLatitude(); 

		double longitude = location.getLongitude(); 
		int latitudeE6 = (int) Math.floor(latitude * 1.0E6); 
		int longitudeE6 = (int) Math.floor(longitude * 1.0E6); 
		GeoPoint geoPoint = new GeoPoint(latitudeE6, longitudeE6); 
		return geoPoint;
	}



	@Override	
	protected boolean onTap(final int index) {

		OverlayItem item = mOverlays.get(index);

		if(item.getTitle().equals("MiaPosizione")){
			mapOverlays.remove(index);
			LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
			final View popUp = inflater.inflate(R.layout.ballon, mapView, false);

			MapView.LayoutParams mapParams = new MapView.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT, mOverlays.get(index)
					.getPoint(), MapView.LayoutParams.BOTTOM_CENTER);
			mapView.addView(popUp, mapParams);
			final TextView text = (TextView)popUp.findViewById(R.id.balloon_item_title);
			text.setText("MiaPosizione");
			final ImageView image = (ImageView)popUp.findViewById(R.id.balloon_close);
			image.setOnClickListener(new OnClickListener() 
			{
				public void onClick(View v) 
				{
					mapView.removeView(popUp);


					Drawable drawable = v.getResources().getDrawable(R.drawable.user_position_on_map);
					PositionOverlay itemizedoverlay = new PositionOverlay(drawable, v.getContext(),null, mapOverlays, mapView, myLocation, mapController); 
					OverlayItem overlayitem = new OverlayItem(createNewGeoPoint(myLocation), "MiaPosizione", "I'm there");
					itemizedoverlay.addOverlay(overlayitem);
					mapOverlays.add(itemizedoverlay);
					mapController.setCenter(createNewGeoPoint(myLocation));
				}
			});

		}
		else{

			final LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
			final View popUp = inflater.inflate(R.layout.ballon_poi, mapView, false);

			MapView.LayoutParams mapParams = new MapView.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT, mOverlays.get(index)
					.getPoint(), MapView.LayoutParams.BOTTOM_CENTER);
			mapView.addView(popUp, mapParams);
			final TextView text = (TextView)popUp.findViewById(R.id.balloon_item_title);
			text.setText(poi.getNome());
			final TextView descrizione = (TextView)popUp.findViewById(R.id.descrizione);
			descrizione.setText(poi.getDescrizione());
			final ImageView image = (ImageView)popUp.findViewById(R.id.balloon_close);
			image.setOnClickListener(new OnClickListener() 
			{
				public void onClick(View v) 
				{
					mapView.removeView(popUp);

					//mapController.setCenter(createNewGeoPoint(mylocation));
				}
			});
			final ImageView segnalazione = (ImageView)popUp.findViewById(R.id.segnalzione);
			segnalazione.setOnClickListener(new OnClickListener() 
			{
				public void onClick(final View v) 
				{
					mapView.removeView(popUp);
					final View popUp2 = inflater.inflate(R.layout.inserisci_segnalazione_dialog, mapView, false);

					MapView.LayoutParams mapParams = new MapView.LayoutParams(
							ViewGroup.LayoutParams.WRAP_CONTENT,
							ViewGroup.LayoutParams.WRAP_CONTENT, mOverlays.get(index)
							.getPoint(), MapView.LayoutParams.BOTTOM_CENTER);
					// map.addView(popUp2, mapParams);



					AlertDialog.Builder alert = new AlertDialog.Builder(context);

					alert.setIcon(R.drawable.ex_mark2);
					alert.setTitle("Segnalazione POI");

					// Set an EditText view to get user input 
					final EditText input = new EditText(context);
					input.setHint("inserisci la descrizione della segnalazione");
					alert.setView(input);

					alert.setPositiveButton("Invia", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {

							// Do something with value!
							String descrption = input.getText().toString();
							Suggestion(poi, descrption, new Date(), v.getContext());
						}
					});

					alert.setNegativeButton("Chiudi", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							// Canceled.
						}
					});

					alert.show();



				}
			});


		}
		return true;
	}

	public void Suggestion(final Poi poiBase,final String description,final Date date,final Context context)
	{

		final AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
			ProgressDialog dialog;
			private ConfrimResponse response;

			protected void onPreExecute(){

				dialog = ProgressDialog.show(context, "Attendere...", "Invio Segnalazione");

			}
			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub

				response = PoiManager.suggestionPoi(poiBase, description, date, "io", context);
				return null;

			}
			protected void onPostExecute(String result) {
				dialog.dismiss();
				if(response== null || response.getStatus() != 200)
				{
					UtilDialog.createBaseToast("Segnalazione non inviata", context).show();
				}
				else
					UtilDialog.createBaseToast("Segnalazione iniviata", context).show();


			}
		};
		task.execute(null);

	}

}