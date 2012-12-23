package geolog.ar;




import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Location;

public class PoiOnCamera extends ARSphericalView
{
	public String name;
	public int icon;
	public int checkins;
	public Location mylocation;
	public Location getMylocation() {
		return mylocation;
	}

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
