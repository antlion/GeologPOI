package com.geolog.util;
import java.util.ArrayList;
import java.util.HashMap;

import com.geolog.R;
import com.geolog.dominio.Category;
import com.geolog.dominio.Poi;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.TextView;
public class CategoriesAdapter extends BaseAdapter{
	

	
	private Context context;

	private ArrayList<Category> categorie; 
	
	private HashMap<Category,String> prova;
	
	private CheckBox chk;
	
	
	

	public CategoriesAdapter(Context context, ArrayList<Category> categorie) {
	   this.context = context; 
	   this.categorie = categorie; 
	   
	   prova = new HashMap<Category,String>();
	   for(Category cat: categorie){
		   prova.put(cat, "false");
	   }
	 
       
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
	
	public HashMap<Category,String> aaaa()
	{
		return prova;
	}
	
	
	public void modifyHash(Category category, String valore)
	{
		prova.remove(category);
		prova.put(category, valore);
	}

	 public View getView(final int position, View convertView, ViewGroup parent) {
		
         
                 LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                 convertView = inflater.inflate(R.layout.riga_lista_gestore_mappa, null);
                
                
                 CheckBox chk = (CheckBox) convertView.findViewById(R.id.category_checkbox);
                chk.setClickable(false);
         
                TextView tx = (TextView) convertView.findViewById(R.id.textView1);
                tx.setText(categorie.get(position).getNomeCategoria());
        	
         return convertView;
          
  }
	
	}

