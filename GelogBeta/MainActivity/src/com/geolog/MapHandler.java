package com.geolog;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.geolog.dominio.*;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;

public class MapHandler extends MapActivity implements VisHandler{
	private GeoPoint currentPosition; 
	private MapController mapController;
	private String providerId = LocationManager.GPS_PROVIDER; 
	private ArrayList<Poi> poi;
	private MapView map;
	private CategoryHandler gestoreCategorie;
	private LinearLayout MenuList;
	private Button btnToggleMenuList;
	private int screenWidth;
	private boolean isExpanded;
	private Location mylocation;
	private List<Overlay> mapOverlays;
	private LocationManager locationManager;
	private LocationListener myLocationListener;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_poi);
        poi = new ArrayList<Poi>();
        ParametersBridge bridge = ParametersBridge.getInstance();
        poi = (ArrayList<Poi>) bridge.getParameter("listaPOI");
        mylocation =(Location)ParametersBridge.getInstance().getParameter("location");
        gestoreCategorie = new CategoryHandler();
        
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
	    final CategoryAdapter pois = new CategoryAdapter(ctx, gestoreCategorie.richiediCategorie(),gestoreCategorie.getCategorySelected2());
	    v.setAdapter(pois);
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
		locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE); 
		Location location = locationManager.getLastKnownLocation(
				LocationManager.GPS_PROVIDER
				);
				if (location != null) {
					mylocation = location;
				updateLocationData(location);
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
	    	
	    	checkMenuCategory((CategoryAdapter) v.getAdapter());
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
	
	
	
	

   
	public void checkMenuCategory(CategoryAdapter categoryChoose)
	{
		ArrayList<Category> categoriaSelezionate = new ArrayList<Category>();
		int number = categoryChoose.getCount();
		for(int count = 0; count<number;count++)
		{
			if(categoryChoose.getCheckBoxstatus())
			categoriaSelezionate.add((Category)categoryChoose.getItem(count));			
		}
		gestoreCategorie.salvaSelezione(categoriaSelezionate);
		//effettua una nuova ricerca
		//chiama il servizio del cupis
		//ottienicateogorie
		//aggiorna categorie +
		poi = GestorePOI.cercaPOI(mylocation, this);
		
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
		Drawable drawable = this.getResources().getDrawable(R.drawable.arrow_down);
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
			Drawable drawable = this.getResources().getDrawable(pois.getCategoria().getIcon());
			PositionOverlay itemizedoverlay = new PositionOverlay(drawable, this,pois); 
			OverlayItem overlayitem = new OverlayItem(point, pois.getNome(), pois.getDescrizione());
			 itemizedoverlay.addOverlay(overlayitem);
		     mapOverlays.add(itemizedoverlay);
			//mapView.getOverlays().add(new PositionOverlay(GestoreMappa.this.getResources(),
			   //     BitmapFactory.decodeResource(res, pois.getImage()),createNewGeoPoint(pois.getLocation()), this));
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
			/*public boolean onTouchEvent(MotionEvent event, MapView mapView) 
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
		    }   */
			
			
			
			
		@Override	
		protected boolean onTap(final int index) {
				 
			 OverlayItem item = mOverlays.get(index);
			
		 if(item.getTitle().equals("MiaPosizione")){
			 
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
				         }
				     });
				     final ImageView segnalazione = (ImageView)popUp.findViewById(R.id.segnalzione);
				     segnalazione.setOnClickListener(new OnClickListener() 
				     {
				         public void onClick(View v) 
				         {
				        	 map.removeView(popUp);
				        	 final View popUp2 = getLayoutInflater().inflate(R.layout.inserisci_segnalazione_dialog, map, false);
							   
							 MapView.LayoutParams mapParams = new MapView.LayoutParams(
				                     ViewGroup.LayoutParams.WRAP_CONTENT,
				                     ViewGroup.LayoutParams.WRAP_CONTENT, mOverlays.get(index)
				                                     .getPoint(), MapView.LayoutParams.BOTTOM_CENTER);
						    // map.addView(popUp2, mapParams);
						     
						     
						     
								final View view = getLayoutInflater().inflate(R.layout.inserisci_segnalazione_dialog, map, false);
								AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
								dialog.setIcon(R.drawable.ex_mark2);
								dialog.setTitle("Segnalazione POI");
								dialog.setView(view);
								dialog.setPositiveButton("Invia", new DialogInterface.OnClickListener() {
									
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										//invia Segnalazione
										EditText descriptionSuggest = (EditText) view.findViewById(R.id.descrizione_segnalazione_poi);
										String descrption = descriptionSuggest.getText().toString();
									}
								});
								dialog.setNegativeButton("Chiudi", new DialogInterface.OnClickListener() {
									
									

									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										dialog.dismiss();
									}
								});
								dialog.show();
						     
						     
						   /*  final Button close = (Button) popUp2.findViewById(R.id.chiudi);
						    close.setOnClickListener(new OnClickListener()
						    {

								public void onClick(View arg0) {
									// TODO Auto-generated method stub
									map.removeView(popUp2);
								}
						    	
						    });
						    final Button invia = (Button) popUp2.findViewById(R.id.invia);
						    close.setOnClickListener(new OnClickListener()
						    {

								public void onClick(View arg0) {
									// TODO Auto-generated method stub
									//invia Segnalazione al cupis
								}
						    	
						    });*/
				         }
				     });
				  
				  
			}
			  return true;
			}

			
			
		}
	 public boolean onTap(GeoPoint p, MapView mapView) {
           System.out.println("ontap2");
           //  return super.onTap(p, mapView);
             return true;
     }
	
	 @Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	
	
}
