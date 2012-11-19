package com.geolog.util;

import com.geolog.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;

public class  UtilDialog {

	
	public static void alertDialog(Context context,String message)
	{
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Avviso");
        builder.setMessage(message);
        builder.setCancelable(true);
       // AlertDialog dialog = builder.create();
      builder.show();

	}
	
		
}
