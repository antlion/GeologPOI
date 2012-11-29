package com.geolog.util;
import java.util.ArrayList;
import java.util.HashMap;

import com.geolog.R;
import com.geolog.dominio.Category;
import com.geolog.dominio.Poi;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
public class CategoryAdapter extends BaseAdapter{
	

	
	private Context context;

	private ArrayList<Category> categorie; 
	
	private CheckBox chk;
	
	private HashMap<String,String> categorySelected;
	

	public CategoryAdapter(Context context, ArrayList<Category> categorie,HashMap<String,String>categorySelected) {
	   this.context = context; 
	   this.categorie = categorie; 
	   this.categorySelected = categorySelected;
	   
	 } 
	public int getCount() { 
	   return categorie.size(); 
	 } 
	public Object getItem(int position) { 
	   return categorie.get(position); 
	 } 
	public long getItemId(int position) { 
	   return categorie.get(position).hashCode(); 
	 } 
	public boolean getCheckBoxstatus()
	{
		return chk.isChecked();
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
		   textView.setText(categorie.get(position).getNomeCategoria());
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
	
	 public View getView(int position, View convertView, ViewGroup parent) {
         if (convertView == null) {
                 LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                 convertView = inflater.inflate(R.layout.riga_lista_gestore_mappa, null);
         }
         chk = (CheckBox) convertView.findViewById(R.id.category_checkbox);
       
        
         if(categorySelected.get(categorie.get(position).getNomeCategoria()) == "true")
        	 chk.setChecked(true);
                
      
        
         TextView tv = (TextView)convertView.findViewById(R.id.list_text);
	     tv.setText(categorie.get(position).getNomeCategoria());

         return convertView;
          
  }
	}

