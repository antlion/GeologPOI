package com.geolog.util;

import java.io.ByteArrayOutputStream;

import com.geolog.dominio.Resource;
import com.geolog.web.Services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Base64;

/**
 * @author Lorenzo
 *
 *Classe per la gestione delle risorse dell'applicazione 
 */
public class ResourcesHandler {

	
	/**
	 * @param bitmap
	 * @return byte[]
	 * Metodo utile pe rla conversione di immagini in formato Bitmap in un array di bayte codificato in base64. Questo metodo è utile quando bisogna
	 * mandare un immagine ad un web service
	 */
	public static byte[] covertBitmapToByte(Bitmap bitmap)
	{
		ByteArrayOutputStream output = new ByteArrayOutputStream();  
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output); //bm is the bitmap object   
		byte[] bytes = output.toByteArray();
		byte[] base64Image = Base64.encode(bytes, Base64.DEFAULT);
		return base64Image;
	}
	
	public static boolean controlImageResource(String uri,Context context)
	{
		String path = (context.getFilesDir().toString());
		String[] fileList = context.fileList();
		for ( String string: fileList)
		{
			if ( string.equals(uri))
				return true;
		}
		return false;
	}
	
	
	
	public static Drawable getDrawableFromUri(String uri, Context context,String nome){
		Services service= new Services();
		return service.downloadResource(uri, context,nome);
			
		
	}
}
