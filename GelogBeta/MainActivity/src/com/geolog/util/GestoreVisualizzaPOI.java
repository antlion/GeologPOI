package com.geolog.util;

import haseman.project.where4.HoldMeUp;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.geolog.ChoseCategoryActivity;
import com.geolog.GestoreAR;
import com.geolog.ListHandler;
import com.geolog.GestoreMappa;
import com.geolog.dominio.*;

public class GestoreVisualizzaPOI {
	
	private ArrayList<POIBase> pois;
	private Context ctx;
	
	public GestoreVisualizzaPOI(Context context){
		ctx = context;
		pois = new ArrayList<POIBase>();
	}
	
	
	public ArrayList<POIBase> getPois() {
		return pois;
	}


	public void setPois(ArrayList<POIBase> pois) {
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
		Intent intent = new Intent(ctx,GestoreMappa.class);		
		ParametersBridge.getInstance().addParameter("listaPOI", pois);
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
		Intent intent = new Intent(ctx,haseman.project.where4.HoldMeUp.class);
		ParametersBridge.getInstance().addParameter("listaPOI", pois);
        return intent;
	}
}
