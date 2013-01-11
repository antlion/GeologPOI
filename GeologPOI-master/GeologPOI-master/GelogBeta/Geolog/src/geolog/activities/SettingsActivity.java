package geolog.activities;

import geolog.managers.ResourcesManager;
import geolog.util.ParametersBridge;
import geolog.util.UtilDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

public class SettingsActivity extends Activity {

	// Contesto dell'attività
	private Context context;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(com.geolog.activity.R.layout.settings_layout);

		// Inzializzo il contesto
		context = this;
		// Item del menù delle opzioni di sistema
		String lv_items[] = { "Disabilita suggerimenti" };
		// Ottengo il riferimento alla list view delle opzioni
		final ListView optionsList = (ListView) findViewById(com.geolog.activity.R.id.optionsList);
		// Setto l'adapter della list view
		optionsList.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_multiple_choice, lv_items));
		optionsList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		// Aggiungo un listner alla lsita delle opzioni del menu
		optionsList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				// Se l'elemento cliccato corrisponde a l'opzione disabilità
				// suggerimenti, viene aggiornato
				// il valore booelano dello stato dei riferimenti
				if (optionsList.getAdapter().getItem(arg2)
						.equals("Disabilita suggerimenti")) {
					CheckedTextView check = (CheckedTextView) arg1;
					check.setChecked(!check.isChecked());
					boolean click = !check.isChecked();
					check.setChecked(click);
					if (click) {

						ParametersBridge.getInstance().addParameter("hint",
								"false");
					} else {

						ParametersBridge.getInstance().addParameter("hint",
								"true");
					}
				}
			}
		});
		// Elementi della lista delle informazioni dell'applicazione
		String infoItem[] = { "Info Applicazione" };
		// Ottengo il riferimento alla lista delle informazioni
		// dell'applicazioni.
		final ListView infoList = (ListView) findViewById(com.geolog.activity.R.id.infoList);
		infoList.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, infoItem));
		// aggiungo il listener alla lista
		infoList.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// Se è stata cliccata l'opzione info applicazione viene
				// mostrato un dialogo.
				if (infoList.getAdapter().getItem(arg2)
						.equals("Info Applicazione")) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							context);
					builder.setMessage("Geolog beta").setNegativeButton(
							"Chiudi", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

								}
							});
					builder.show();
				}
			};

		});

		// Elementi della lista delle opzioni della memoria
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
				// Se è stata cliccata l'opzione elmina dati applicazione, viene
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
		});

	}

	//

}
