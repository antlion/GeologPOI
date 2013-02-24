package geolog.ar;

import geolog.activities.PoiAugmentedRealityManager;
import geolog.managers.ResourcesManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

import com.geolog.dominio.*;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.util.Log;

public class PoiOnCameraManager {

	int highestCheckin = 0;

	@SuppressWarnings({ "unused", "rawtypes" })
	private void adjustAz(Vector v) {
		Enumeration e = v.elements();
		while (e.hasMoreElements()) {
			PoiOnCamera poi = (PoiOnCamera) e.nextElement();
			poi.inclination = (poi.checkins / highestCheckin) * 100;
			Log.e("inc", "Inclination is " + poi.inclination);
		}
	}

	public Vector<PoiOnCamera> createPoiOnCameraFromPoi(Location Loc, ArrayList<Poi> poi,
			Context context) {

		Vector<PoiOnCamera> poisOnCamera = new Vector<PoiOnCamera>();
		Location location1 = new Location("locazione1");
		location1.setLatitude(41.850319);
		location1.setLongitude(12.632983);
		Location location2 = new Location("locazione2");
		location2.setLatitude(43.856808);
		location2.setLongitude(19.632855);
		Poi poi1 = new Poi(new Category("Ristoro", 0, "0"), "Bar", "ssdsa",
				new Date(), location1);
		Poi poi2 = new Poi(new Category("Emergenza", 0, "0"), "Ristor",
				"ssdsa", new Date(), location2);
		poi = new ArrayList<Poi>();
		poi.add(poi1);
		poi.add(poi2);

		for (Poi newPoi : poi) {
			Location location = newPoi.getPOILocation();
			location.setLatitude(newPoi.getLatitude());
			location.setLongitude(newPoi.getLongitude());

			PoiOnCamera poiOnCamera = new PoiOnCamera(
					PoiAugmentedRealityManager.ctx);

			poiOnCamera.location = location;
			poiOnCamera.name = newPoi.getNome();
			poiOnCamera.mylocation = Loc;

			if (ResourcesManager.controlImageResource(newPoi.getCategory()
					.getIcon(), context)) {

				Bitmap icon2 = BitmapFactory.decodeFile((context.getFilesDir()
						.toString() + "//" + ResourcesManager
						.getNameFileFromUrl((newPoi.getCategory().getIcon()))));
				poiOnCamera.icon = icon2;
			} else
				poiOnCamera.icon = null;
			poisOnCamera.add(poiOnCamera);

		}

		return poisOnCamera;
	}

}
