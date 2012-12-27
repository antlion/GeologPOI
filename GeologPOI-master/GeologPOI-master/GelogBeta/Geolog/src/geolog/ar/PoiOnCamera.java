package geolog.ar;




import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Location;

public class PoiOnCamera extends ARSphericalView
{
	/**
	 * @uml.property  name="name"
	 */
	public String name;
	/**
	 * @uml.property  name="icon"
	 */
	public int icon;
	/**
	 * @uml.property  name="checkins"
	 */
	public int checkins;
	/**
	 * @uml.property  name="mylocation"
	 * @uml.associationEnd  
	 */
	public Location mylocation;
	/**
	 * @return
	 * @uml.property  name="mylocation"
	 */
	public Location getMylocation() {
		return mylocation;
	}

	/**
	 * @param mylocation
	 * @uml.property  name="mylocation"
	 */
	public void setMylocation(Location mylocation) {
		this.mylocation = mylocation;
	}

	public PoiOnCamera(Context ctx)
	{
		super(ctx);
		inclination = 0;
		
	}

	public void draw(Canvas c)
	{
		int numero =(int) location.distanceTo(deviceLocation) ; 
		p.setColor(Color.WHITE);
		p.setTextSize(25);
		if(name != null)
			c.drawText(name+" distanza: "+String.valueOf(numero), getLeft(), getTop(), p);
		Bitmap icona = BitmapFactory.decodeResource(getContext().getResources(),
               icon);
		if(icona !=null)
		c.drawBitmap(icona, getLeft(), getTop(), p);
			
	}
}
