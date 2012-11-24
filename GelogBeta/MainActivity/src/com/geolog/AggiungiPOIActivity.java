package com.geolog;



import java.util.ArrayList;

import com.geolog.util.CategoryHandler;
import com.geolog.util.UtilGps;
import com.geolog.web.WebService;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AggiungiPOIActivity extends Activity implements OnClickListener, android.view.View.OnClickListener,Visualizzazione{
	
	private CategoryHandler gestoreCategorie;
	private UtilGps gps;
	private Location location;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aggiunta_poi_activity_layout);
        
        gestoreCategorie = new CategoryHandler();
        gps = new UtilGps(this, this);
        gps.getGpsStatus();
        EditText et1 = (EditText) findViewById(R.id.editText1);
        EditText et2 = (EditText) findViewById(R.id.editText2);
        Spinner spinner = (Spinner) findViewById(R.id.spinner1);
        Button button = (Button) findViewById(R.id.ok);
        button.setOnClickListener(this);
       
   ArrayAdapter<String> adapter = new ArrayAdapter<String>(
        		this,
        		android.R.layout.simple_spinner_item,
        		ottieniCategorie()
        		);
   spinner.setAdapter(adapter);
	}
	
	public String[] ottieniCategorie()
	{
		ArrayList<String> categorie = new ArrayList<String>();
		categorie = gestoreCategorie.ottieniNomiCategorie();
		String[] nomi = new String[categorie.size()];
		int count=0;
		for ( String categoria: categorie){
			nomi[count] = categoria;
			count++;
		}
		return nomi;
	}

	public void onClick(View v) {
		// TODO Auto-generated method stubù
		if ( location != null){
			//crea nuovo POI
			//invia nuovo POI
			//interfaccia webServer
			WebService.aggiungiPOI(GestorePOI.creaPOI());
		}
		else{
			Context context = getApplicationContext();
			CharSequence text = "Hello toast!";
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, "no", duration);
			toast.show();
			
		}
	}

	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		
	}

	public void updateLocationData(Location location) {
		// TODO Auto-generated method stub
		this.location = location;
	}
}
