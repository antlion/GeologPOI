package com.geolog.util;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.util.Base64;

/**
 * @author Lorenzo
 *
 *Classe per la gestione delle risorse dell'applicazione android. 
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
	
	
}
