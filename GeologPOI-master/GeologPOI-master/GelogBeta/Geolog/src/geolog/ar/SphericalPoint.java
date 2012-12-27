package geolog.ar;

public class SphericalPoint
{
	/**
	 * @uml.property  name="azimuth"
	 */
	public float azimuth; //Angle from north
	/**
	 * @uml.property  name="distance"
	 */
	public float distance; //Distance to object
	/**
	 * @uml.property  name="inclination"
	 */
	public float inclination; //angle off horizon.
	
	//used to compute inclination
	public static float currentAltitude = 0;
	
	public SphericalPoint(float angleFromNorth, float distance, float altitude)
	{
		this.distance = distance;
		//arctan of opposite/adjacent
		float opposite;
		boolean neg = false;
		if(altitude > currentAltitude)
		{
			opposite = altitude - currentAltitude;
		}
		else
		{
			opposite = currentAltitude - altitude;
			neg = true;
		}
		inclination = (float) Math.atan(((double)opposite/distance));
		if(neg)
			opposite = opposite * -1;
	}
}
