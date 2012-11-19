package com.geolog.util;

import java.util.ArrayList;

import com.geolog.R;
import com.geolog.dominio.POIBase;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class POIAdapter extends BaseAdapter { 
private Context context;

private ArrayList<POIBase> poi; 
public POIAdapter(Context context, ArrayList<POIBase> poi) {
   this.context = context; 
   this.poi = poi; 
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
public View getView(int position, View convertView, ViewGroup parent) { 
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
	 }

   /*public View getView(int position, View convertView, ViewGroup parent) {
        convertView = MenuInflater.inflate(IGNORE_ITEM_VIEW_TYPE, null);
 
       // ImageView iv = (ImageView)convertView.findViewById(R.id.immagine_vista);
       // iv.setImageDrawable(immagini.getDrawable(position));
 
        TextView tv = (TextView)convertView.findViewById(R.id.testo_vista);
        tv.setText(poi.get(position).getNome());
 
        return convertView;
    }*/
}
