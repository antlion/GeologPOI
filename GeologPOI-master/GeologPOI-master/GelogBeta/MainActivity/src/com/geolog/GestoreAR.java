package com.geolog;


import java.util.ArrayList;

import com.geolog.dominio.Poi;
import com.geolog.util.ParametersBridge;


import haseman.project.where4.CustomCameraView;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.FrameLayout;

public class GestoreAR extends Activity implements VisHandler{

	public static SensorManager sensorMan;
	

	private ArrayList<Poi> poi;
	private LocationManager locationManager;
	private LocationListener myLocationListener;

	private Location myLocation;
	public void onCreate(Bundle savedInstanceState) {
		
		//gps = new UtilGps(this,this);
		 	poi = new ArrayList<Poi>();
	        ParametersBridge bridge = ParametersBridge.getInstance();
	        poi = (ArrayList<Poi>) bridge.getParameter("listaPOI");
	        myLocation =(Location)ParametersBridge.getInstance().getParameter("location");
	        
	        
	        
		   try{
		      super.onCreate(savedInstanceState);
		      CustomCameraView cv = new CustomCameraView(
		         this.getApplicationContext());
		      FrameLayout rl = new FrameLayout(
		         this.getApplicationContext());
		      setContentView(rl);
		      rl.addView(cv);
		   } catch(Exception e){}
		   
		   SensorEventListener listener = new SensorEventListener(){

			  

			public void onSensorChanged(SensorEvent event) {
				// TODO Auto-generated method stub
				float vals[] = event.values;   
			     float direction = vals[0];
			     System.out.println(direction);
			}

			public void onAccuracyChanged(Sensor sensor, int accuracy) {
				// TODO Auto-generated method stub
				
			}
			};
			sensorMan = (SensorManager)this.getSystemService(this.SENSOR_SERVICE);
		      sensorMan.registerListener(
		         listener, 
		         sensorMan.getDefaultSensor(
		            SensorManager.SENSOR_ORIENTATION), 
		         SensorManager.SENSOR_DELAY_FASTEST);
		      
		      
		    }

		
	
	
	public void updateLocationData(Location location) {
		// TODO Auto-generated method stub
		
	}

}
