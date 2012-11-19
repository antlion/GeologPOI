package haseman.project.fourSquareLibs;

import haseman.project.where4.HoldMeUp;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.Vector;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class FourSquareClient
{
	int highestCheckin = 0;
	private void adjustAz(Vector v)
	{
		Enumeration e = v.elements();
		while(e.hasMoreElements())
		{
			FourSqareVenue ven = (FourSqareVenue)e.nextElement();
			ven.inclination = (ven.checkins / highestCheckin) * 100;
			Log.e("inc","Inclination is "+ven.inclination);
		}
	}
	
	public Vector<FourSqareVenue> prova(Location Loc)
	{
		
		
		Vector<FourSqareVenue> v = new Vector<FourSqareVenue>();
		Location curLocation = null;
		curLocation = Loc;
		 
	     FourSqareVenue curVen = new FourSqareVenue(HoldMeUp.ctx);
	     curVen.location = curLocation;
	     curVen.name="poi di proca";
	     curVen.location.setLatitude(42.437647);
			curVen.location.setLongitude(14.21315);
			v.add(curVen);
			//adjustAz(v);
			
			FourSqareVenue curVen2 = new FourSqareVenue(HoldMeUp.ctx);
		     curVen2.location = curLocation;
		     curVen2.name="ospedale ptv";
		     curVen2.location.setLatitude(41.856808);
				curVen2.location.setLongitude(12.632855);
			//v.add(curVen2);
				
				
				Location location2 = new Location("prova");
				FourSqareVenue curVen3 = new FourSqareVenue(HoldMeUp.ctx);
			     curVen3.location = location2;
			     curVen3.name="aeroporto fiumicino";
			     curVen3.location.setLatitude(41.79768);
					curVen3.location.setLongitude(12.257257);
					v.add(curVen3);
				
			
			return v;
	}
	
	
	public Vector<FourSqareVenue> getVenuList(Location loc)
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
			FourSqareVenue curVen = new FourSqareVenue(HoldMeUp.ctx);
			while(parsing)
			{
				int next = p.next();
				switch(next)
				{
				case XmlPullParser.START_TAG:
					name = p.getName();
					if(name.equals("venue"))
						curVen = new FourSqareVenue(HoldMeUp.ctx);
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
	}
}
