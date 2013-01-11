package geolog.ar;


import geolog.activities.PoiAugmentedRealityManager;
import geolog.managers.ResourcesManager;


import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;



import com.geolog.dominio.*;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.util.Log;

public class FourSquareClient
{
	
	int highestCheckin = 0;
	@SuppressWarnings({ "unused", "rawtypes" })
	private void adjustAz(Vector v)
	{
		Enumeration e = v.elements();
		while(e.hasMoreElements())
		{
			PoiOnCamera ven = (PoiOnCamera)e.nextElement();
			ven.inclination = (ven.checkins / highestCheckin) * 100;
			Log.e("inc","Inclination is "+ven.inclination);
		}
	}
	
	public Vector<PoiOnCamera> prova(Location Loc, ArrayList<Poi> poi,Context context)
	{
		
		
		Vector<PoiOnCamera> v = new Vector<PoiOnCamera>();
		//gli passo la mia posizione
		/*Location curLocation = null;
		curLocation = Loc;
		 
		Location location1 = new Location("locazione1");
		location1.setLatitude(41.850319);
		location1.setLongitude(12.632983);
		Location location2 = new Location("locazione2");
		location2.setLatitude(43.856808);
		location2.setLongitude(19.632855);
		Location location3 = new Location("locazione3");
		
	     FourSqareVenue curVen = new FourSqareVenue(HandlerAR.ctx);
	     curVen.location = location1;
	     curVen.name="poi di proca";
	     v.add(curVen);
			//adjustAz(v);
			
			FourSqareVenue curVen2 = new FourSqareVenue(HandlerAR.ctx);
		     curVen2.location = location2;
		     curVen2.name="ospedale ptv";
		     v.add(curVen2);*/
			for(Poi newPoi : poi){
				Location location = newPoi.getPOILocation();
				location.setLatitude(newPoi.getLatitude());
				location.setLongitude(newPoi.getLongitude());
				
				PoiOnCamera curVen1 = new PoiOnCamera(PoiAugmentedRealityManager.ctx);

			     curVen1.location = location;
			    
			     //curVen1.icon = newPoi.getCategoria().getIcon();
			     
			     curVen1.name=newPoi.getNome();
			     curVen1.mylocation = Loc;
			     
			     if (ResourcesManager.controlImageResource(newPoi.getCategory().getIcon(), context)){
			    	 
			    	 Bitmap icon2 = BitmapFactory.decodeFile((context.getFilesDir().toString()
								+ "//" + ResourcesManager.getNameFileFromUrl((newPoi.getCategory().getIcon()))));
			    	 curVen1.icon= icon2;
			    	 }
			     else
			     curVen1.icon= null;
			     v.add(curVen1);
				
			}
				
				
				
		     
			return v;
	}
	
	
/*	public Vector<FourSqareVenue> getVenuList(Location loc)
	{

		Vector<FourSqareVenue> v = new Vector<FourSqareVenue>();
		try{
		URL url = new URL("http://api.foursquare.com/v1/venues?l=5&geolat="+loc.getLatitude()+"&geolong="+loc.getLongitude());
		Log.e("where4","Url: "+url.toString());
		HttpURLConnection httpconn;
		httpconn = (HttpURLConnection) url.openConnection();
		httpconn.setDoInput(true);
		httpconn.setDoOutput(false);
		httpconn.connect();
		int httpRC = httpconn.getResponseCode();
		if (httpRC == HttpURLConnection.HTTP_OK)
		{
			XmlPullParserFactory x = XmlPullParserFactory.newInstance();
			x.setNamespaceAware(false);
			XmlPullParser p = x.newPullParser();
			p.setInput(new InputStreamReader(httpconn.getInputStream()));
			boolean parsing = true;
			String curText="", name;
			float lat=-1,lon=-1;
			FourSqareVenue curVen = new FourSqareVenue(HandlerAR.ctx);
			while(parsing)
			{
				int next = p.next();
				switch(next)
				{
				case XmlPullParser.START_TAG:
					name = p.getName();
					if(name.equals("venue"))
						curVen = new FourSqareVenue(HandlerAR.ctx);
					break;
				case XmlPullParser.END_TAG:
					name = p.getName();
					if(name.equals("venue"))
					{
						curVen.location = new Location("FourSqareApi");
						curVen.location.setLatitude(lat);
						curVen.location.setLongitude(lon);
						v.add(curVen);
					}
					else if(name.equals("name"))
						curVen.name = curText;

					else if(name.equals("geolat"))
						lat = Float.parseFloat(curText);
					else if(name.equals("geolong"))
						lon = Float.parseFloat(curText);
					else if(name.equals("checkins"))
					{
						curVen.checkins = Integer.parseInt(curText);
						if(curVen.checkins > highestCheckin)
							highestCheckin = curVen.checkins;
					}
					break;
				case XmlPullParser.CDSECT:
				case XmlPullParser.TEXT:
					curText = p.getText();
					break;
				case XmlPullParser.END_DOCUMENT:
					parsing = false;
					
				}
			}
			adjustAz(v);
		}
		}catch(Exception e)
		{
			Log.e("4sqrClient","Failed to get");
		}
		
		return v;
	}*/
}
