package com.geolog.util;

import java.util.ArrayList;
import java.util.Date;

import com.geolog.HandlerPOI;
import com.geolog.R;
import com.geolog.dominio.Poi;
import com.google.android.maps.MapView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.util.FloatMath;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class POIAdapter extends BaseAdapter { 
private Context context;
private Location myLocation;
private ArrayList<Poi> poi; 
private ImageView suggestionView;
public POIAdapter(Context context, ArrayList<Poi> poi,Location myLocation) {
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

public View getView(final int position, View convertView, ViewGroup parent) throws NullPointerException { 
	   if (convertView == null) {
           LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           convertView = inflater.inflate(R.layout.riga_lista_gestore_lista, null);
   }
	   TextView textView = (TextView) convertView.findViewById(R.id.nomePOI);
	   textView.setText(poi.get(position).getNome());
	   TextView textView2 = (TextView) convertView.findViewById(R.id.categoriaPOI);
	   textView2.setText(poi.get(position).getCategoria().getNomeCategoria());
	   
	   ImageView information = (ImageView) convertView.findViewById(R.id.poi_image);
	   poi.get(position).setImageFromResource(context);
	   Drawable d = poi.get(position).getDrawableImage();
	   
	   
	   
	   if (d == null)
	   information.setImageResource(R.drawable.no_image_aviable);
	   else
		   information.setImageDrawable(d);
	   
	   suggestionView = (ImageView) convertView.findViewById(R.id.imageSuggestion);
	   suggestionView.setClickable(true);
	   suggestionView.setOnClickListener(new OnClickListener() {
		
		public void onClick(final View v) {
			// TODO Auto-generated method stub
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
				Log.d("edit",descrption);
			if(	(HandlerPOI.segnalaPOI(poi.get(position), descrption, new Date(), v.getContext())) )
			{
				UtilDialog.createBaseToast("Segnlazione inviata", context);
			}
			else
				UtilDialog.createBaseToast("Segnalazione non iviata", context);
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
	   
	  
	 
	   
	   int numero =(int) myLocation.distanceTo(poi.get(position).getPOILocation()) ; 
		
	   
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
