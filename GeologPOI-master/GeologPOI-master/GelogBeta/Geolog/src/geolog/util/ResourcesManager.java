package geolog.util;

import geolog.web.WebService;

import java.io.ByteArrayOutputStream;


import android.content.Context;
import android.graphics.Bitmap;
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
}
