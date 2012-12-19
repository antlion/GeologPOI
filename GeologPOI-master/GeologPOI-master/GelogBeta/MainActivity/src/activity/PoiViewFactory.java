package activity;

import java.util.ArrayList;

import activity.ar.HandlerAR;
import activity.dominio.Poi;
import activity.util.ParametersBridge;
import android.content.Context;
import android.content.Intent;
import android.location.Location;


public class PoiViewFactory {

	public static Intent getView(String tipodivis,Context context,Location Location,ArrayList<Poi> pois)
	{
		if (tipodivis.equals("Map"))
		{
			Intent intent = new Intent(context, PoiMapManager.class);
			ParametersBridge.getInstance().addParameter("listaPOI", pois);
			ParametersBridge.getInstance().addParameter("location", Location);
			return intent;
		}
		if (tipodivis.equals("List"))
		{
			Intent intent = new Intent(context, PoiListManager.class);
			ParametersBridge.getInstance().addParameter("listaPOI", pois);
			ParametersBridge.getInstance().addParameter("location", Location);
			return intent;
		}
		if(tipodivis.equals("Ar"))
		{
			Intent intent = new Intent(context, HandlerAR.class);
			ParametersBridge.getInstance().addParameter("listaPOI", pois);
			ParametersBridge.getInstance().addParameter("location", Location);
			return intent;
		}
		return null;
	}
}
