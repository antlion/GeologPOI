package com.geolog;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.geolog.dominio.*;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
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

public class GestoreMappa extends MapActivity implements Visualizzazione{
	private GeoPoint currentPosition; 
	private UtilGps gps;
	private MapController mapController;
	private String providerId = LocationManager.GPS_PROVIDER; 
	private ArrayList<POIBase> poi;
	private MapView map;
	private CategoryHandler gestoreCategorie;
	private LinearLayout MenuList;
	private Button btnToggleMenuList;
	private int screenWidth;
	private boolean isExpanded;
	
	
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view);
        poi = new ArrayList<POIBase>();
        ParametersBridge bridge = ParametersBridge.getInstance();
        poi = (ArrayList<POIBase>) bridge.getParameter("listaPOI");
       gestoreCategorie = new CategoryHandler();
        
        //verrà passata l'istanza dei POI ricercati
        
       
        //POI di prova
      
        //fine poi di prova
        MapView mapView = (MapView) findViewById(R.id.mapView);
        map = mapView;
        mapView.setClickable(true);
        mapView.setBuiltInZoomControls(true);
         gps = new UtilGps(this,this);
        gps.getGpsSupport();
        gps.getGpsStatus();
        mapController = mapView.getController(); 
        mapController.setZoom(10); 
        
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
	      
	        
	        
	     // then animate the view translating from (0, 0)
	    
	      
		
        
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_MENU) {
	    	ListView v = (ListView) findViewById(R.id.listView1);
	    	
	    	if(isExpanded){
	    		v.setVisibility(View.GONE);
	    	isExpanded = false;
	    	MenuList.startAnimation(new com.geolog.slide.CollapseAnimation(MenuList, 0,(int)(screenWidth*0.7), 20));
	    	
	    	checkMenuCategory((CategoryAdapter) v.getAdapter());
	    	;}
	    	else{
	    		
	    		isExpanded = true;
	    		
	    		MenuList.startAnimation(new com.geolog.slide.ExpandAnimation(MenuList, 0,(int)(screenWidth*0.7), 20));
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
		ArrayList<Categoria> categoriaSelezionate = new ArrayList<Categoria>();
		int number = categoryChoose.getCount();
		for(int count = 0; count<number;count++)
		{
			if(categoryChoose.getCheckBoxstatus())
			categoriaSelezionate.add((Categoria)categoryChoose.getItem(count));			
		}
		gestoreCategorie.salvaSelezione(categoriaSelezionate);
		//effettua una nuova ricerca
		//chiama il servizio del cupis
		//ottienicateogorie
		//aggiorna categorie
	}
	   
	protected void onResume() { 
		   super.onResume(); 
		  if(gps != null)
		 gps.onResumeGps();
		 } 
	@Override
	protected void onPause() { 
	   super.onPause(); 
	   if(gps != null)
	gps.onPauseGps();
	 } 
	
	
	
	public void updateLocationData(Location location) { 
		 
		   double latitude = location.getLatitude(); 
	
		   double longitude = location.getLongitude(); 
		   int latitudeE6 = (int) Math.floor(latitude * 1.0E6); 
		   int longitudeE6 = (int) Math.floor(longitude * 1.0E6); 
		   GeoPoint geoPoint = new GeoPoint(latitudeE6, longitudeE6); 
		  
		   	MapView mapView = (MapView) findViewById(R.id.mapView);
	        mapView.setClickable(true);
	        currentPosition = geoPoint;
	        mapController.setCenter(currentPosition);
	        //mapView.getOverlays().add(new CurrentPositionOverlay());
	        Resources res = GestoreMappa.this.getResources(); 
	       
	        
	        List<Overlay> mapOverlays = mapView.getOverlays();
	        mapView.getOverlays().clear();
	        aggiungiMiaPosizione(mapOverlays,currentPosition);
	        
	      //  mapView.getOverlays().add(new PositionOverlay(GestoreMappa.this.getResources(),
	       // BitmapFactory.decodeResource(res, R.drawable.arrow_down),geoPoint,this));
	       addPOIOverlay(mapOverlays);
	        
		  // mapController.setCenter(geoPoint); 
	  
	       
		 }
    
	
	private void aggiungiMiaPosizione(List<Overlay> mapOverlays, GeoPoint geoPoint)
	{
		Drawable drawable = this.getResources().getDrawable(R.drawable.arrow_down);
		
        PositionOverlay itemizedoverlay = new PositionOverlay(drawable, this,null); 
        OverlayItem overlayitem = new OverlayItem(geoPoint, "MiaPosizione", "I'm in Mexico City!");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
		
	}
	private void addPOIOverlay(List<Overlay> mapOverlays)
	{
		for(POIBase pois: poi){
			GeoPoint point = createNewGeoPoint(pois.getLocation());
			Drawable drawable = this.getResources().getDrawable(pois.getImage());
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
			
		 
		 	private POIBase poi;
		 
		 
			public PositionOverlay(Drawable defaultMarker,Context context,POIBase poi) {
				super(boundCenterBottom(defaultMarker));
				// TODO Auto-generated constructor stub
				 mContext = context;
				 this.poi = poi;
			/*	 map.setOnTouchListener(new View.OnTouchListener() {

			           public boolean onTouch(View v, MotionEvent event) {
			               // TODO Auto-generated method stub
			               GeoPoint p = null;
			               System.out.println("toccato");
			               if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP) {
			                   p = map.getProjection().fromPixels((int) event.getX(),
			                           (int) event.getY());
			                   String mapBackButton = (p.getLatitudeE6() / 1E6 + ","
			                           + p.getLongitudeE6() / 1E6 + "Action is : "
			                           + event.getAction());
			                   System.out.println(mapBackButton);
			                   //return true;
			                   Toast.makeText(
			                           getBaseContext(),
			                           p.getLatitudeE6() / 1E6 + "," + p.getLongitudeE6()
			                                   / 1E6 + "Action is : " + event.getAction(),
			                           Toast.LENGTH_SHORT).show();
			                   return true;
			               }
			               return false;
			           }
			       });*/
				
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
						     map.addView(popUp2, mapParams);
						     final Button close = (Button) popUp2.findViewById(R.id.chiudi);
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
						    	
						    });
				         }
				     });
				  
				  
				/*  AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
				 dialog.setTitle(poi.getNome());
				 Resources res = getResources();
				 String[] planets = res.getStringArray(R.array.poi_selezionato);
				 dialog.setItems(planets,  new DialogInterface.OnClickListener() {
		               public void onClick(DialogInterface dialog, int which) {
		                   // The 'which' argument contains the index position
		                   // of the selected item
		            	   if (which == 0){
		            		   System.out.println(poi.getDescrizione());
		            		   LayoutInflater inflater = getLayoutInflater();
		            		   View layout = inflater.inflate(R.layout.toast_layout,(ViewGroup) findViewById(R.id.toast_layout_root));

		            		   TextView text = (TextView) layout.findViewById(R.id.text);
		            		   text.setText(poi.getDescrizione());

		            		   Toast toast = new Toast(getApplicationContext());
		            		   toast.setGravity(Gravity.BOTTOM, 0,0);
		            		   toast.setDuration(Toast.LENGTH_LONG);
		            		   toast.setView(layout);
		            		   toast.show();
		            	   }
		            	   if(which == 1){
		            		   //segnala POI
		            		   	LayoutInflater li = LayoutInflater.from(mContext);
		       					View promptsView = li.inflate(R.layout.inserisci_segnalazione_dialog, null);
		            		   AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		            		    // Get the layout inflater
		            		    LayoutInflater inflater = getLayoutInflater();

		            		    // Inflate and set the layout for the dialog
		            		    // Pass null as the parent view because its going in the dialog layout
		            		    builder.setView(promptsView);
		            		    final EditText test = (EditText) promptsView.findViewById(R.id.descrizione_segnalazione_poi);
		            		    // Add action buttons
		            		          builder.setPositiveButton("Invia segnalazione", new DialogInterface.OnClickListener() {
		            		               public void onClick(DialogInterface dialog, int id) {
		            		                   // sign in the user ...
		            		            	   
		            		            	    System.out.println(test.getText().toString());
		            		            	   GestorePOI.segnalaPOI(poi,test.getText().toString());
		            		               }
		            		           })
		            		           .setNegativeButton("cancella", new DialogInterface.OnClickListener() {
		            		               public void onClick(DialogInterface dialog, int id) {
		            		                  
		            		               }
		            		           });      
		            		    builder.show();
		            	   }
		               }
		        });
				  dialog.show();*/}
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
