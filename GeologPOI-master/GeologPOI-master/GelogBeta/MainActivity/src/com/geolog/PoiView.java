package com.geolog;

import java.util.ArrayList;

import com.geolog.dominio.Poi;

import android.app.Activity;
import android.content.Context;
import android.location.Location;

public class PoiView extends Activity{

	//Array di poi contenente i poi resituiti da una ricerca di punti di interesse
		private ArrayList<Poi> pois;

		//Contesto dell'attività
		private Context context;

		//Location dell'utente
		private Location myLocation;
	
}
