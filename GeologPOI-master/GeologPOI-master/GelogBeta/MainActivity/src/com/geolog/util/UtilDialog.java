package com.geolog.util;

import com.geolog.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.widget.Toast;

public class  UtilDialog {

	
	public static AlertDialog alertDialog(Context context,String message)
	{
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Avviso");
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setNeutralButton("Continua", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		
       // AlertDialog dialog = builder.create();
        final AlertDialog alert = builder.create();
		return alert;

	}
	
	public static Toast createBaseToast(String message,Context context)
	{
		Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
		return toast;
	}
	
	
	public static AlertDialog createAlertNoGps(final Context context)
	{
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
	    builder.setMessage("Yout GPS seems to be disabled, do you want to enable it?")
	           .setCancelable(false)
	           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	               public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
	            	   Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                	   intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                	   context.startActivity(intent);
	                   
	                   
	               }
	           })
	           .setNegativeButton("No", new DialogInterface.OnClickListener() {
	               public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
	                    dialog.cancel();
	               }
	           });
	    final AlertDialog alert = builder.create();
	    return alert;
		
	}
	
	public static AlertDialog createAlertNoProviderGps(final Context context)
	{
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(context.getString(R.string.noGpsProvider));
		builder.setCancelable(false);
		final AlertDialog alert = builder.create();
		return alert;
	}
	
	
	
	
}
