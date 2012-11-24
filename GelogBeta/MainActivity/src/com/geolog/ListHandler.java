package com.geolog;

import java.util.ArrayList;



import com.geolog.dominio.Categoria;
import com.geolog.dominio.POIBase;
import com.geolog.dominio.PoiEmergenza;
import com.geolog.util.CategoryAdapter;
import com.geolog.util.CategoryHandler;
import com.geolog.util.POIAdapter;
import com.geolog.util.ParametersBridge;
import com.geolog.util.UtilGps;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class ListHandler extends ListActivity implements Visualizzazione {

	private ArrayList<POIBase> poi;
	private Point p;
	private Context ctx;
	private UtilGps utilGps;
	private Location myLocation;
	private POIAdapter pois;
	private LinearLayout MenuList;
	private Button btnToggleMenuList;
	private int screenWidth;
	private boolean isExpanded;
	TextView view;
	private CategoryHandler gestoreCategorie;
	 public void onCreate(final Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.lista_poi);
	        
	        poi = new ArrayList<POIBase>();
	        ParametersBridge bridge = ParametersBridge.getInstance();
	        poi = (ArrayList<POIBase>) bridge.getParameter("listaPOI");
	       
	        utilGps = new UtilGps(this,this);
	        gestoreCategorie = new CategoryHandler();
	        isExpanded = false;
	        ListView v = (ListView) findViewById(R.id.listView1);
	        final CategoryAdapter pois = new CategoryAdapter(this, gestoreCategorie.richiediCategorie(),gestoreCategorie.getCategorySelected2());
	        v.setAdapter(pois);
	        v.setVisibility(View.GONE);
	        String providerId = LocationManager.GPS_PROVIDER; 
	       
	        ctx = getApplicationContext();
	        Resources res = ctx.getResources();
	      
	        MenuList = (LinearLayout) findViewById(R.id.linearLayout2);
	        
	        DisplayMetrics metrics = new DisplayMetrics();
	        getWindowManager().getDefaultDisplay().getMetrics(metrics);
	        screenWidth = metrics.widthPixels;
	       
	        
	        
	        ////TypedArray immagini = res.obtainTypedArray(R.array.immagini);
	     //   final POIAdapter pois = new POIAdapter(ctx, poi,utilGps.getLastLocation());
	       // setListAdapter(pois);
	 
	        //istView listaPOI = getListView();
	        //listaPOI.setTextFilterEnabled(true);
	       
	        
	        
	    /*   final Context context = this;
	        
	        listaPOI.setOnItemClickListener(new OnItemClickListener() {
	            

				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					
					final POIBase prova= (POIBase) pois.getItem(arg2);
					Resources res = getResources();
					String[] planets = res.getStringArray(R.array.poi_selezionato);
					new AlertDialog.Builder(context)
			        .setTitle(prova.getNome())
			        
			        .setItems(planets, new DialogInterface.OnClickListener() {
			               public void onClick(DialogInterface dialog, int which) {
			            	   if (which == 0){
			            		   ImageView image = new ImageView(getBaseContext());
			        		       
			       		        image.setImageResource(R.drawable.poi_info);
			       		     new AlertDialog.Builder(context)
						        .setCustomTitle(image)
						        .setMessage(prova.getDescrizione())
						        .show();
			            		  
			            	   }
			            	   if(which == 1){
			            		   //segnala POI
			            	   }
			           }
			    }).show();
					
	        }
					
	        }	);*/
					
					
					
					 //final Dialog builder= onCreateDialog(savedInstanceState,"askljadsk");
				       
					 //AlertDialog.Builder builder = new AlertDialog.Builder(GestoreLista.this);
					
			
	      
						
 }
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
		    if (keyCode == KeyEvent.KEYCODE_MENU) {
		    	ListView v = (ListView) findViewById(R.id.listView1);
		    	if (isExpanded) {
	    			
	    			
	    			isExpanded = false;
	    			//MenuList.startAnimation(new com.geolog.slide.CollapseAnimation(MenuList, 0,(int)(screenWidth*0.7), 20));
	    			 v.setVisibility(View.GONE);
	    			 checkMenuCategory((CategoryAdapter)v.getAdapter());
	    		}else {
	        		isExpanded = true;
	        		//MenuList.startAnimation(new com.geolog.slide.ExpandAnimation(MenuList, 0,(int)(screenWidth*0.7), 20));
	        		v.setVisibility(View.VISIBLE);
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
		  if(utilGps != null)
			  utilGps.onResumeGps();
		 } 
	@Override
	protected void onPause() { 
	   super.onPause(); 
	   if(utilGps != null)
		   utilGps.onPauseGps();
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
		
		 final POIAdapter pois = new POIAdapter(ctx, poi,location);
	        setListAdapter(pois);
	}
	
	
		
	
	

}
