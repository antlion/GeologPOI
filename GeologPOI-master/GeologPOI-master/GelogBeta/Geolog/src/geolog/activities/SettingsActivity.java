package geolog.activities;

import com.geolog.activity.R;

import geolog.managers.ResourcesManager;
import geolog.util.ParametersBridge;
import geolog.util.ui.UtilDialog;
import geolog.web.WebService;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener{

	// Contesto dell'attivit�
	private Context context;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref);
	
/*		// Elementi della lista delle opzioni della memoria
		String memoryItem[] = { "Elimina dati applicazione" };
		// Ottengo il riferimento alla lista
		final ListView memoryList = (ListView) findViewById(com.geolog.activity.R.id.memoryList);
		memoryList.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, memoryItem));
		// Aggiungo un listenr alla lista
		memoryList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				// Se � stata cliccata l'opzione elmina dati applicazione, viene
				// mostrato un dialogo in cui si
				// chiede la conferma per l'eliminazione dei dati
				if (memoryList.getAdapter().getItem(arg2)
						.equals("Elimina dati applicazione")) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							context);
					builder.setMessage(
							"Sei sicuro di volere eliminare i dati dell'applicazione?")
							.setPositiveButton("Si",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											ResourcesManager
													.clearApplicationData(context);
											UtilDialog.createBaseToast(
													"Dati eliminati", context)
													.show();
										}
									})
							.setNegativeButton("Chiudi",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {

										}
									});
					builder.show();
				}
			}
		});*/
		

	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences arg0, String key) {
		// TODO Auto-generated method stub
		 if (key.equals("Dev options")) {
	            Preference connectionPref = findPreference(key);
	            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
	            String  string = prefs.getString("Dev options", "<unset>");
				// Set summary to be the user-description for the selected value
	            String stringURL = string+":8080/web/services/WS";
	    		String stringURLRes = string+":8080/web";
	    		WebService.setWEB_SERVICE_URL(stringURL);
	    		WebService.setWEB_SERVICE_URL_RES(stringURLRes);
	        }
	}

	//

}
