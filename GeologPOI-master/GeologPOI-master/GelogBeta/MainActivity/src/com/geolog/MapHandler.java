package com.geolog;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.geolog.dominio.*;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;




import com.geolog.util.*;
import com.geolog.web.Services;
import com.geolog.web.domain.BaseResponse;
import com.geolog.web.domain.ConfrimResponse;
import com.geolog.web.domain.PoiListResponse;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.markupartist.android.widget.ActionBar;

public class MapHandler extends MapActivity implements VisHandler{
	private GeoPoint currentPosition; 
	private MapController mapController;
	private String providerId = LocationManager.GPS_PROVIDER; 
	private ArrayList<Poi> poi;
	private MapView map;
	private CategoriesHandler gestoreCategorie;
	private LinearLayout MenuList;
	private Button btnToggleMenuList;
	private int screenWidth;
	private boolean isExpanded;
	private Location mylocation;
	private List<Overlay> mapOverlays;
	private LocationManager locationManager;
	private LocationListener myLocationListener;
	private CategoriesAdapter categoryAdapter;
	private Context context ;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   
       context = getApplicationContext();
        setContentView(R.layout.map_poi);
       setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
        poi = new ArrayList<Poi>();
        ParametersBridge bridge = ParametersBridge.getInstance();
        poi = (ArrayList<Poi>) bridge.getParameter("listaPOI");
        mylocation =(Location)ParametersBridge.getInstance().getParameter("location");
        gestoreCategorie = CategoriesHandler.getGestoreCategorie();
        
        //inizializzo la mappa
        MapView mapView = (MapView) findViewById(R.id.mapView);
        map = mapView;
        mapView.setClickable(true);
        mapView.setBuiltInZoomControls(true);
    
        mapController = mapView.getController(); 
        mapView.setBuiltInZoomControls(true);
        mapController.setZoom(10); 
        
        mapOverlays = mapView.getOverlays();
        
        
        updateLocationData(mylocation);
     
        mapController.setCenter(createNewGeoPoint(mylocation));
     
        
        ListView v = (ListView) findViewById(R.id.listView1);
   	 	Context ctx = getApplicationContext();
	    Resources res = ctx.getResources();
	      
	        ////TypedArray immagini = res.obtainTypedArray(R.array.immagini);
	    
	    categoryAdapter = new CategoriesAdapter(getApplicationContext(), gestoreCategorie.getCategorie());
        gestoreCategorie.setSelectionCategory(v, getApplicationContext(),categoryAdapter);
      
	   // v.setAdapter(pois);
	    v.setVisibility(View.GONE);
	    MenuList = (LinearLayout) findViewById(R.id.linearLayout2);
	    DisplayMetrics metrics = new DisplayMetrics();
	    getWindowManager().getDefaultDisplay().getMetrics(metrics);
	    screenWidth = metrics.widthPixels;
	      
	    
	    
	        
	    locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
	       myLocationListener = new LocationListener() { 
					
				   
				 
				public void onLocationChanged(Location location) {
					// TODO Auto-generated method stub
					//vis.updateLocationData(location);
					//effutua ricerca poi
					Log.d("Locazione cabiata2", location.toString());
					//updateLocationData(mylocation);
					mylocation = location;
					   updateLocationData(mylocation);
				/*	aggiungiMiaPosizione(mapOverlays,createNewGeoPoint(location));
					if (poi != null){
					 addPOIOverlay(mapOverlays);}*/
					}

				public void onProviderDisabled(String arg0) {
					// TODO Auto-generated method stub
					
				}

				public void onProviderEnabled(String provider) {
					// TODO Auto-generated method stub
					
				}

				public void onStatusChanged(String provider, int status,
						Bundle extras) {
					// TODO Auto-generated method stub
					
				} 
				 };   
	     // then animate the view translating from (0, 0)
				 locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 1, myLocationListener);
	      Log.d("cerco posiziome","gps");
		
        
	}
	
	public void onPause()
	{
		super.onPause();
		  //LocationManager locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE); 
		  locationManager.removeUpdates(myLocationListener); 
	}
	
	public void onResume()
	{
		super.onResume();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
		locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE); 
		Location location = locationManager.getLastKnownLocation(
				LocationManager.GPS_PROVIDER
				);
				if (location != null) {
				mylocation = location;
				//updateLocationData(location);
				}
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 1, myLocationListener);
	}
	
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_MENU) {
	    	ListView v = (ListView) findViewById(R.id.listView1);
	    	
	    	if(isExpanded){
	    		v.setVisibility(View.GONE);
	    	isExpanded = false;
	    	//MenuList.startAnimation(new com.geolog.slide.CollapseAnimation(MenuList, 0,(int)(screenWidth*0.7), 20));
	    	gestoreCategorie.checkMenuCategory(categoryAdapter);
	    	searchPOI(context);
			if (poi != null)
			{
				updateLocationData(mylocation);
			}
	    //	checkMenuCategory((CategoryAdapter) v.getAdapter());
	    	;}
	    	else{
	    		
	    		isExpanded = true;
	    		
	    		//MenuList.startAnimation(new com.geolog.slide.ExpandAnimation(MenuList, 0,(int)(screenWidth*0.7), 20));
	    		View view =(View)findViewById( R.id.linearLayout2);
	    		//view.setVisibility(View.GONE);
	    	v.setVisibility(View.VISIBLE);
	    	//	view.setVisibility(View.VISIBLE);
	    		}
		     return true;
	    } else {
	        return super.onKeyDown(keyCode, event);
	    }
	}
	

	

	public void updateLocationData(Location location) { 
		 
		   if( mylocation != null){
		        aggiungiMiaPosizione(mapOverlays,createNewGeoPoint(mylocation));
		        mapController.setCenter(createNewGeoPoint(mylocation));}
		        if (poi != null){
		        addPOIOverlay(mapOverlays);}
		        else {
		        	UtilDialog.createBaseToast("impossibile recuperare poi", this).show();
		        } 
	}
    
	
	private void aggiungiMiaPosizione(List<Overlay> mapOverlays, GeoPoint geoPoint)
	{
		map.getOverlays().clear();
		Drawable drawable = this.getResources().getDrawable(R.drawable.pegman);
		PositionOverlay itemizedoverlay = new PositionOverlay(drawable, this,null); 
        OverlayItem overlayitem = new OverlayItem(geoPoint, "MiaPosizione", "I'm there");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
        mapController.setCenter(geoPoint);
	}
	private void addPOIOverlay(List<Overlay> mapOverlays)
	{
		
		for(Poi pois: poi){
			//aggiunta dopo dominio
			
			GeoPoint point = createNewGeoPoint(pois.getPOILocation());
			
			Drawable drawable = pois.getCategoria().getIconFromResource(context);
			PositionOverlay itemizedoverlay = new PositionOverlay(drawable, this,pois); 
			OverlayItem overlayitem = new OverlayItem(point, pois.getNome(), pois.getDescrizione());
			 itemizedoverlay.addOverlay(overlayitem);
		     mapOverlays.add(itemizedoverlay);
			
		}
		
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
	
	
	
	 
	 

	 public class PositionOverlay extends ItemizedOverlay {
			
		 
		 	private Poi poi;
		 
		 
			public PositionOverlay(Drawable defaultMarker,Context context,Poi poi) {
				super(boundCenterBottom(defaultMarker));
				// TODO Auto-generated constructor stub
				 mContext = context;
				 this.poi = poi;
			
				
			}

			private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
			private Context mContext;
			
			
			
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
			        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);

					alert.setIcon(R.drawable.ex_mark2);
					alert.setTitle("AggiungiPOI");
					alert.setMessage("Vuoi aggiungere un punto di intresse in questa posizione?");
					alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					
						 Location location = new Location("nuova");
					        location.setLatitude(p.getLatitudeE6() / 1E6);
					        location.setLongitude(p.getLongitudeE6() /1E6);
					        
					        ParametersBridge.getInstance().addParameter("Location", location);
					        Intent intent = new Intent(getBaseContext(), AddPOIActivity.class);
					        startActivity(intent);
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
			
			
			
			
		@Override	
		protected boolean onTap(final int index) {
				
			 OverlayItem item = mOverlays.get(index);
			
		 if(item.getTitle().equals("MiaPosizione")){
			 mapOverlays.remove(index);
			 final View popUp = getLayoutInflater().inflate(R.layout.ballon, map, false);
			   
			 MapView.LayoutParams mapParams = new MapView.LayoutParams(
                     ViewGroup.LayoutParams.WRAP_CONTENT,
                     ViewGroup.LayoutParams.WRAP_CONTENT, mOverlays.get(index)
                                     .getPoint(), MapView.LayoutParams.BOTTOM_CENTER);
		     map.addView(popUp, mapParams);
		     final TextView text = (TextView)popUp.findViewById(R.id.balloon_item_title);
		     text.setText("MiaPosizione");
		     final ImageView image = (ImageView)popUp.findViewById(R.id.balloon_close);
		     image.setOnClickListener(new OnClickListener() 
		     {
		         public void onClick(View v) 
		         {
		        	 	map.removeView(popUp);
		        	 	
		        	 	
		        	 	Drawable drawable = v.getResources().getDrawable(R.drawable.pegman);
		        		PositionOverlay itemizedoverlay = new PositionOverlay(drawable, v.getContext(),null); 
		                OverlayItem overlayitem = new OverlayItem(createNewGeoPoint(mylocation), "MiaPosizione", "I'm there");
		                itemizedoverlay.addOverlay(overlayitem);
		                mapOverlays.add(itemizedoverlay);
		                mapController.setCenter(createNewGeoPoint(mylocation));
		         }
		     });
			
			 }
			  else{
				  
				  
				  final View popUp = getLayoutInflater().inflate(R.layout.ballon_poi, map, false);
				   
					 MapView.LayoutParams mapParams = new MapView.LayoutParams(
		                     ViewGroup.LayoutParams.WRAP_CONTENT,
		                     ViewGroup.LayoutParams.WRAP_CONTENT, mOverlays.get(index)
		                                     .getPoint(), MapView.LayoutParams.BOTTOM_CENTER);
				     map.addView(popUp, mapParams);
				     final TextView text = (TextView)popUp.findViewById(R.id.balloon_item_title);
				     text.setText(poi.getNome());
				     final TextView descrizione = (TextView)popUp.findViewById(R.id.descrizione);
				    descrizione.setText(poi.getDescrizione());
				     final ImageView image = (ImageView)popUp.findViewById(R.id.balloon_close);
				     image.setOnClickListener(new OnClickListener() 
				     {
				         public void onClick(View v) 
				         {
				        	 	map.removeView(popUp);
				        		
				                //mapController.setCenter(createNewGeoPoint(mylocation));
				         }
				     });
				     final ImageView segnalazione = (ImageView)popUp.findViewById(R.id.segnalzione);
				     segnalazione.setOnClickListener(new OnClickListener() 
				     {
				         public void onClick(final View v) 
				         {
				        	 map.removeView(popUp);
				        	 final View popUp2 = getLayoutInflater().inflate(R.layout.inserisci_segnalazione_dialog, map, false);
							   
							 MapView.LayoutParams mapParams = new MapView.LayoutParams(
				                     ViewGroup.LayoutParams.WRAP_CONTENT,
				                     ViewGroup.LayoutParams.WRAP_CONTENT, mOverlays.get(index)
				                                     .getPoint(), MapView.LayoutParams.BOTTOM_CENTER);
						    // map.addView(popUp2, mapParams);
						     
						     
						     
							 	AlertDialog.Builder alert = new AlertDialog.Builder(mContext);

								alert.setIcon(R.drawable.ex_mark2);
								alert.setTitle("Segnalazione POI");

								// Set an EditText view to get user input 
								final EditText input = new EditText(mContext);
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

			
			
		}
	
	
	 @Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	 //Vedere se si può mettere in un altra classe)
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

					 response = HandlerPOI.segnalaPOI(poiBase, description, date, context);
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
	 
	 public void searchPOI(final Context context){
			AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
				
				ProgressDialog dialog;
				@Override
				protected void onPreExecute(){
					locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 1, myLocationListener);
					dialog = ProgressDialog.show(context, "Attendere...", "Ricerca poi in corso...");
				
				}
				@Override
				protected String doInBackground(Void... params) {
					// TODO Auto-generated method stub
					
		            
		          
		            	
		            
		            	PoiListResponse response = HandlerPOI.cercaPOI(mylocation, context);
		            	if (response == null || response.getStatus() != 200)
		            	{
		            		dialog.dismiss();
		            		UtilDialog.alertDialog(context, "impossibile recuperare i poi").show();
		            	}
		            	else {
		            			poi = (ArrayList<Poi>) response.getPois();
		            			ViewPOIHandler view = new ViewPOIHandler(context);
		            			view.setPois(poi);
		        	        	view.setMylocation(mylocation);
		            			if (poi.size()<0){
		            				dialog.dismiss();
		            				UtilDialog.createBaseToast("nessun poi trovato", context);}
		            			else {
		            				updateLocationData(mylocation);
		            			}
		            			
		            	}
		            
		            
		            
		            return null;
				
				}
				 protected void onPostExecute(String result) {
			            dialog.dismiss();
			       
			        }
				
			};
			task.execute(null);
		
			}

	
}
