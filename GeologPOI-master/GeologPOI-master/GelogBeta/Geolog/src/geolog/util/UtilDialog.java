package geolog.util;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.provider.Settings;
import android.widget.Toast;

/**
 * Messaggi di utilià per il sistema.
 * 
 * @author Lorenzo
 * 
 */
@SuppressLint("ShowToast")
public class UtilDialog {

	/**
	 * VIsualizza un dialogo base con un messaggio e un bottone neutrale
	 * 
	 * @param context
	 *            contesto dell'attività
	 * @param message
	 *            messaggio da visualizzare
	 * @return il dialogo
	 */
	public static AlertDialog alertDialog(Context context, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Avviso");
		builder.setMessage(message);
		builder.setCancelable(true);
		builder.setNeutralButton("Continua",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});

		final AlertDialog alert = builder.create();
		return alert;

	}

	/**
	 * Crea un nuovo toast con il messaggio passato come paramentro
	 * 
	 * @param message
	 *            messaggio da visualizzare
	 * @param context
	 *            contesto dell'attività
	 * @return Toast il dialogo da visualizzare
	 */
	public static Toast createBaseToast(String message, Context context) {
		Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
		return toast;
	}

	/**
	 * Crea un dialogo per allertare l'utente che il gps non attivo, mostrato un
	 * dialogo per l'attivazione del gps
	 * 
	 * @param context
	 *            contesto dell'attività
	 * @return
	 */
	public static AlertDialog createAlertNoGps(final Context context,final Activity activity) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(
				"Yout GPS seems to be disabled, do you want to enable it?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog,
									final int id) {
								Intent intent = new Intent(
										Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								context.startActivity(intent);
								activity.finish();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog,
							final int id) {
						dialog.dismiss();
						activity.finish();
					}
				});
		final AlertDialog alert = builder.create();
		return alert;

	}

	/**
	 * Dialogo che allerta l'utente che il device non supporta il gps
	 * 
	 * @param context
	 *            contesto dell'attività
	 * @return AlertDialog il dialogo di allerta
	 */
	public static AlertDialog createAlertNoProviderGps(final Context context,final Activity activity) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(context.getString(com.geolog.activity.R.string.noGpsProvider));
		builder.setCancelable(false);
		builder.setNeutralButton("Chiudi", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				activity.finish();
			}
		});
		final AlertDialog alert = builder.create();
		return alert;
	}

}
