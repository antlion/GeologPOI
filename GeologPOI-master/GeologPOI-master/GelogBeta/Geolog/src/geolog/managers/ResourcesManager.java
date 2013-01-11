package geolog.managers;

import geolog.web.WebService;

import java.io.ByteArrayOutputStream;
import java.io.File;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Base64;

/**
 * @author Lorenzo
 * 
 *         Classe per la gestione delle risorse dell'applicazione
 */
public class ResourcesManager {

	/**
	 * Metodo utile pe rla conversione di immagini in formato Bitmap in un array
	 * di bayte codificato in base64. Questo metodo è utile quando bisogna
	 * mandare un immagine ad un web service
	 * 
	 * @param bitmap
	 * @return byte[]
	 */
	public static byte[] covertBitmapToByte(Bitmap bitmap) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output); // bm is the
																	// bitmap
																	// object
		byte[] bytes = output.toByteArray();
		byte[] base64Image = Base64.encode(bytes, Base64.DEFAULT);
		return base64Image;
	}

	/**
	 * Controllo se la risorsa corrispondente all'url sia già presente nel
	 * sistema
	 * 
	 * @param uri
	 *            url della risorsa
	 * @param context
	 *            contesto del sistema
	 * @return true se la risorsa è presente nel sistema, false altrimenti
	 */
	public static boolean controlImageResource(String uri, Context context) {
		try {

			// Prendo la lista dei file dell'applicazione
			String[] fileList = context.fileList();
			// Controllo se almeno un file corrisponde a quello descritto
			// dall'url
			for (String string : fileList) {
				// se il file corrisponde resituisco true
				if (string.equals(getNameFileFromUrl(uri)))
					return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Trasforma una risorsa in un formato drawable
	 * 
	 * @param uri
	 *            url della risorsa
	 * @param context
	 *            contesto del sistema
	 * @return Drawable la risorsa in formato Drawable
	 */
	public static Drawable getDrawableFromUri(String uri, Context context) {

		// Creo l'accesso al servizio web
		WebService service = new WebService();
		// ritorno la risorsa scaricata
		return service.downloadResource(uri, context);

	}

	/**
	 * A partire dalla url viene preso il nome della risorsa
	 * 
	 * @param url
	 *            url della risorsa
	 * @return il nome della risorsa a partire dall'url
	 */
	public static String getNameFileFromUrl(String url) {
		int index = url.lastIndexOf('/');

		return url.substring(index + 1);
	}
	
	/**
	 * Recuper di un custom typeface per customizzare le view dell'applicazione
	 * @param context contesto dell'applicazione
	 * @return il typeface da assegnare
	 */
	public static Typeface getCustomTypeFace(Context context)
	{
		Typeface myTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/bebe.otf");
		return myTypeface;
	}
	
	/**
	 * Viene elminata la cartella dei dati dell'applicazione.
	 * @param context contesto dell'attività
	 */
	public static void clearApplicationData(Context context) {
		File cache = context.getCacheDir();
		File appDir = new File(cache.getParent());
		if (appDir.exists()) {
			String[] children = appDir.list();
			for (String s : children) {
				if (!s.equals("lib")) {
					deleteDir(new File(appDir, s));
					
				}
			}
		}
	}

	/**
	 * Elminazione di una directory.
	 * @param dir directory che deve essere elminata
	 * @return true se la cancellazione è avvenuta con successo, false altrimenti.
	 */
	public static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}

		return dir.delete();
	}
	
}
