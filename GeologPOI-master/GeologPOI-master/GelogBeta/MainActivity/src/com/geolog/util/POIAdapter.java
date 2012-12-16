package com.geolog.util;

import java.util.ArrayList;
import java.util.Date;

import com.geolog.HandlerPOI;
import com.geolog.R;
import com.geolog.dominio.Poi;
import com.geolog.web.domain.ConfrimResponse;
import com.google.android.maps.MapView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
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
	   Drawable d = poi.get(position).setImageFromResource(context);
	   
	   
	   
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
				Suggestion(poi.get(position), descrption, new Date(), v.getContext());
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
}
