package geolog.ar;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.util.Log;
import android.view.View;

public class ARSphericalView extends View
{
	/**
	 * @uml.property  name="azimuth"
	 */
	public volatile float azimuth; //Angle from north
	/**
	 * @uml.property  name="distance"
	 */
	public volatile float distance; //Distance to object
	/**
	 * @uml.property  name="inclination"
	 */
	public volatile float inclination = -1; //angle off horizon.
	/**
	 * @uml.property  name="location"
	 * @uml.associationEnd  
	 */
	public volatile Location location;
	
	/**
	 * @uml.property  name="x"
	 */
	public volatile int x;
	/**
	 * @uml.property  name="y"
	 */
	public volatile int y;
	/**
	 * @uml.property  name="visible"
	 */
	public volatile boolean visible = false;
	
	
	
	public static Location deviceLocation;
	//used to compute inclination
	public static float currentAltitude = 0;
	/**
	 * @uml.property  name="p"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	protected Paint p = new Paint();
	
	public ARSphericalView(Context ctx)
	{
		super(ctx);
	
	}
	
	


	public ARSphericalView(Context ctx, Location deviceLocation, Location objectLocation)
	{
		super(ctx);
		if(deviceLocation != null)
		{
			azimuth = deviceLocation.bearingTo(objectLocation);
			distance = deviceLocation.distanceTo(objectLocation);
			if(deviceLocation.hasAccuracy() && objectLocation.hasAltitude())
			{
				double opposite;
				boolean neg = false;
				if(objectLocation.getAltitude() > deviceLocation.getAltitude())
				{
					opposite = objectLocation.getAltitude() - deviceLocation.getAltitude();
				}
				else
				{
					opposite = deviceLocation.getAltitude() - objectLocation.getAltitude();
					neg = true;
				}
				inclination = (float) Math.atan(((double)opposite/distance));
				if(neg)
					inclination = inclination * -1;
			}
		}
	}
	public void draw(Canvas c)
	{

	}

}
