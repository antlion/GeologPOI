package com.geolog;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.geolog.dominio.Category;
import com.geolog.dominio.Poi;
import com.geolog.util.CategoryAdapter;
import com.geolog.util.POIAdapter;
import com.geolog.util.ParametersBridge;
import com.geolog.util.UtilDialog;
import com.geolog.web.FindNearbyService;

public class ListHandler extends ListActivity implements VisHandler,OnTouchListener {

	private ArrayList<Poi> poi;

	private Context ctx;


	
	private Location myLocation;
	private POIAdapter pois;
	private LinearLayout MenuList;
	private Button btnToggleMenuList;
	private int screenWidth;
	private boolean isExpanded;
	private ListView listCategory;
	private Activity activity;
	TextView view;
	private CategoryAdapter categoryAdapter ;
	private CategoryHandler gestoreCategorie;
	private ListView listaPOI;

	private boolean itemListPOIClicked = false;
	private LocationManager locationManager;
	private LocationListener myLocationListener;
	
	 public void onCreate(final Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.lista_poi);
	        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
	        RelativeLayout rl = (RelativeLayout) findViewById(R.id.layoutInformationPOI);
	        rl.setVisibility(View.GONE);
	        
	        activity = this;
	        poi = new ArrayList<Poi>();
	        ParametersBridge bridge = ParametersBridge.getInstance();
	        poi = (ArrayList<Poi>) bridge.getParameter("listaPOI");
	        myLocation =(Location)ParametersBridge.getInstance().getParameter("location");
	        
	        
	        gestoreCategorie = CategoryHandler.getGestoreCategorie();
	        isExpanded = false;
	   
	       setListPOI();
	        
	        listCategory = (ListView) findViewById(R.id.list_category);
	        categoryAdapter = new CategoryAdapter(this, gestoreCategorie.getCategorie());
	        gestoreCategorie.setSelectionCategory(listCategory, getApplicationContext(), categoryAdapter);
	       // listCategory.setAdapter(categoryAdapter);
	        listCategory.setVisibility(View.GONE);
	        
	       
	       
	        ctx = getApplicationContext();
	       
	      
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
						Log.d("Locazione cabiata3", location.toString());
						updateLocationData(location);
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
					 locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 1, myLocationListener);
	  
	        
	        
	     final Context context = this;
	        if ( poi != null){
	        listaPOI.setOnItemClickListener(new OnItemClickListener() {
	            	public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					itemListPOIClicked = true;
					final LinearLayout ln = (LinearLayout) findViewById(R.id.linearLayout5);
					//ListView v = (ListView) findViewById(R.id.listView1);
					
					final RelativeLayout rl = (RelativeLayout) findViewById(R.id.layoutInformationPOI);
					
					
					
					listCategory.setVisibility(View.GONE);
					ln.setVisibility(View.GONE);
					rl.setVisibility(View.VISIBLE);
					
					Poi poi = (Poi)pois.getItem(arg2);
					
					ImageView imageDescriptionPOi = (ImageView) findViewById(R.id.imagePOI);
					
					
				
					  
					   
					   
					
					  
						imageDescriptionPOi.setImageDrawable(poi.setImageFromResource(context));
					
					TextView titoloPOI = (TextView) findViewById(R.id.nomePOI);
					titoloPOI.setText(poi.getNome());
					
					TextView descrizionePOI = (TextView) findViewById(R.id.descriptionPOI);
					descrizionePOI.setText(poi.getDescrizione());
					
					
					
				}

	        }	
					
	        	);
					
					
	        }
					
		
           
 }
	 
	 
	 public void setListPOI()
	 {
		 if( poi !=null){
		        pois = new POIAdapter(this, poi,myLocation);
		        setListAdapter(pois);
		        listaPOI = getListView();
			    listaPOI.setTextFilterEnabled(true);
			    listaPOI.setVisibility(View.GONE);
			    listaPOI.setVisibility(View.VISIBLE);}
		        else {
		        	UtilDialog.createBaseToast("impossibile recuperare poi", this).show();

		        }
	 }
	 
	 
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
		    if (keyCode == KeyEvent.KEYCODE_MENU) {
		    	
		    	LinearLayout ln = (LinearLayout) findViewById(R.id.linearLayout5);
		    	if (isExpanded) {
	    			isExpanded = false;
	    			//MenuList.startAnimation(new com.geolog.slide.CollapseAnimation(MenuList, 0,(int)(screenWidth*0.7), 20));
	    			 listCategory.setVisibility(View.GONE);
	    			 gestoreCategorie.checkMenuCategory(categoryAdapter);
	    			 poi = HandlerPOI.cercaPOI(myLocation, this);
	    				updateLocationData(myLocation);
	    			// checkMenuCategory((CategoryAdapter)listCategory.getAdapter());
	    		}else {
	        		isExpanded = true;
	            	//MenuList.startAnimation(new com.geolog.slide.ExpandAnimation(MenuList, 0,(int)(screenWidth*0.7), 20));
	        		//ln.setVisibility(View.GONE);
	        		listCategory.setVisibility(View.VISIBLE);
	    		}
			     return true;
		    }
		    if( keyCode == KeyEvent.KEYCODE_BACK)
		    {
		    	if ( itemListPOIClicked ){
		    		itemListPOIClicked = false;
		    		final RelativeLayout rl = (RelativeLayout) findViewById(R.id.layoutInformationPOI);
		    		final LinearLayout ln = (LinearLayout) findViewById(R.id.linearLayout5);
		    		rl.setVisibility(View.GONE);
		    		ln.setVisibility(View.VISIBLE);
		    		return true;
		    	}
		    	return false;
		    }
		    else {
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
			
			poi = HandlerPOI.cercaPOI(myLocation, this);
			updateLocationData(myLocation);
		}
	 
	@Override
	protected void onPause() { 
	   super.onPause(); 
	   LocationManager locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE); 
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
					myLocation = location;
				//updateLocationData(location);
				}
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 1, myLocationListener);
	}
	 	
	 public Dialog onCreateDialog(Bundle savedInstanceState,String string) {
	     AlertDialog.Builder builder = new AlertDialog.Builder(this);
	     // Get the layout inflater
	     LayoutInflater inflater = this.getLayoutInflater();

	     // Inflate and set the layout for the dialog
	     // Pass null as the parent view because its going in the dialog layout
	     builder.setView(inflater.inflate(R.layout.dialog_poi_information, null));
	  
	     // Add action buttons
	    builder.setMessage(string);
       
       // builder.setMessage(string);
	     return builder.create();
	 }
	
	 
	public void updateLocationData(Location location) {
		// TODO Auto-generated method stub
		myLocation = location;
		setListPOI();
			
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}        
	       
	}
	
	
		
	
	


