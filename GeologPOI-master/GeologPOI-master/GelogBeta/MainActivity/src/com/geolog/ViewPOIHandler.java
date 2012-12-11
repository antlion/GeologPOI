package com.geolog;



import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.geolog.ar.HandlerAR;
import com.geolog.dominio.*;
import com.geolog.util.ParametersBridge;

public class ViewPOIHandler {
	
	private ArrayList<Poi> pois;
	private Context ctx;
	private Location mylocation; 
	
	 
	public Location getMylocation() {
		return mylocation;
	}


	public void setMylocation(Location mylocation) {
		this.mylocation = mylocation;
	}


	public ViewPOIHandler(Context context){
		ctx = context;
		pois = new ArrayList<Poi>();
	}
	
	
	public ArrayList<Poi> getPois() {
		return pois;
	}


	public void setPois(ArrayList<Poi> pois) {
		this.pois = pois;
	}


	public Context getContext() {
		return ctx;
	}


	public void setContext(Context context) {
		this.ctx = context;
	}


	public Intent visualizzaMappa()
	{	
		Intent intent = new Intent(ctx,MapHandler.class);		
		ParametersBridge.getInstance().addParameter("listaPOI", pois);
		ParametersBridge.getInstance().addParameter("location", mylocation);
        return intent;
	}
	
	public Intent visualizzaLista()
	{
		Intent intent = new Intent(ctx, ListHandler.class);
		ParametersBridge.getInstance().addParameter("listaPOI", pois);
        return intent;
	}
	public Intent visualizzaAR()
	{
		Intent intent = new Intent(ctx,HandlerAR.class);
		ParametersBridge.getInstance().addParameter("listaPOI", pois);
        return intent;
	}
}
