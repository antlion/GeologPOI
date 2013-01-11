package geolog.ar;







import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;


public class PoiOnCamera extends ARSphericalView
{
	
	public String name;
	
	public Bitmap icon;
	
	public int checkins;

	public Location mylocation;
	
	private Context context;
	
	public Location getMylocation() {
		return mylocation;
	}

	/**
	 * @param mylocation
	 * 
	 */
	public void setMylocation(Location mylocation) {
		this.mylocation = mylocation;
	}

	public PoiOnCamera(Context ctx)
	{
		super(ctx);
		inclination = 0;
		context = ctx;
	}

	public void draw(Canvas c)
	{
		int numero =(int) location.distanceTo(deviceLocation) ; 
		p.setColor(Color.WHITE);
		p.setTextSize(25);
		
		Typeface myTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/bebe.otf");
	   
		p.setTypeface(myTypeface);
		if(name != null)
			c.drawText(name+" distanza: "+String.valueOf(numero), getLeft(), getTop(), p);
		

		if(icon !=null)
		
		c.drawBitmap(icon, getLeft(), getTop(), p);
			
	}
}
