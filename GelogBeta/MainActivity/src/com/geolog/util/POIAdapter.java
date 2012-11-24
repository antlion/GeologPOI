package com.geolog.util;

import java.util.ArrayList;

import com.geolog.R;
import com.geolog.dominio.POIBase;

import android.content.Context;
import android.content.res.TypedArray;
import android.location.Location;
import android.util.FloatMath;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class POIAdapter extends BaseAdapter { 
private Context context;
private Location myLocation;
private ArrayList<POIBase> poi; 
public POIAdapter(Context context, ArrayList<POIBase> poi,Location myLocation) {
   this.context = context; 
   this.poi = poi; 
   this.myLocation = myLocation;
 } 
public int getCount() { 
   return poi.size(); 
 } 
public Object getItem(int position) { 
   return poi.get(position); 
 } 
public long getItemId(int position) { 
   return poi.get(position).hashCode(); 
 } 
/*public View getView(int position, View convertView, ViewGroup parent) { 
		TextView textView; 
	   if (convertView == null) { 
		 textView = new TextView(context);  
	   //textView = (TextView)convertView.findViewById(R.id.testo_vista); 
	   //imageView.setScaleType(ScaleType.CENTER_CROP); 
	  } else { 
	   textView = (TextView) convertView; 
	  }
	   textView.setText(poi.get(position).getNome());
	   //imageView.setImageDrawable(images[position]); 
	   return textView; 
	 }*/

public View getView(int position, View convertView, ViewGroup parent) { 
	   if (convertView == null) {
           LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           convertView = inflater.inflate(R.layout.riga_lista_gestore_lista, null);
   }
	   TextView textView = (TextView) convertView.findViewById(R.id.nomePOI);
	   textView.setText(poi.get(position).getNome());
	   TextView textView2 = (TextView) convertView.findViewById(R.id.categoriaPOI);
	   textView2.setText(poi.get(position).getCategoria().getNomeCategoria());
	   
	   Location poiLocation = poi.get(position).getLocation();
	  
	   
	   int numero =(int) myLocation.distanceTo(poi.get(0).getLocation()) ; 
		
	   
	   TextView textView3 = (TextView) convertView.findViewById(R.id.distance);
	   textView3.setText(String.valueOf("Distanza: "+numero+" metri"));
	   return convertView;
	   
 }



// see http://androidsnippets.com/distance-between-two-gps-coordinates-in-meter



   /*public View getView(int position, View convertView, ViewGroup parent) {
        convertView = MenuInflater.inflate(IGNORE_ITEM_VIEW_TYPE, null);
 
       // ImageView iv = (ImageView)convertView.findViewById(R.id.immagine_vista);
       // iv.setImageDrawable(immagini.getDrawable(position));
 
        TextView tv = (TextView)convertView.findViewById(R.id.testo_vista);
        tv.setText(poi.get(position).getNome());
 
        return convertView;
    }*/
}
