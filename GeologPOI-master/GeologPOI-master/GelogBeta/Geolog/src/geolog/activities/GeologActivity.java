package geolog.activities;

import geolog.managers.CategoriesManager;

import geolog.managers.ResourcesManager;
import geolog.util.AuthGoogle;
import geolog.util.ParametersBridge;
import geolog.util.ui.MenuCategory;
import geolog.util.ui.UtilDialog;

import java.util.ArrayList;

import com.geolog.activity.R;
import com.geolog.dominio.Category;
import com.geolog.web.domain.CategoryListResponse;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;

import android.provider.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TextView;

/**
 * @author Lorenzo
 * 
 *         Questa class rappresenta l'attività principale dell'applicazione. Da
 *         questa attività è possibile aggiungere e ricercare i poi.
 * 
 */

public class GeologActivity extends Activity {

	// Attirubuto contentente l'intera attività android
	private Activity activity;

	// Contesto dell'atitività
	private Context context;

	// Gestore delle categorie
	private CategoriesManager categoriesHandler;

	// Menù delle categorie
	private MenuCategory menuCategory;

	// Stato della inzializzazione
	private boolean inzializationFinsh;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.geolog_activity_layout);

		// Setto lo stato dell'inzializzazione
		inzializationFinsh = false;

		// Variabile contenente l'ActionBar dell'attività
		final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);

		// Inzializzazione del gestore delle categorie
		categoriesHandler = CategoriesManager.getCategoriesManager();

		// Salvataggio il contesto dell'attività
		context = getBaseContext();

		// Salvataggio dell'attività
		activity = this;

		// Creazione del menu delle categorie
		menuCategory = new MenuCategory(false,
				(ListView) findViewById(R.id.listView1), categoriesHandler,
				context);
		// Setto la visibilità del menu delle categorie
		menuCategory.setVisibilityListCategory(false);

		// Controllo che l'utente si autenticato al sistema. In caso positivo
		// l'applicazione continua il suo corso,
		// in caso negativo non è possibile procedere altrimenti.
		checkUserAccount();

		// Se l'inizializzazione non è stata eseguita, viene eseguita
		if (inzializationFinsh != true) {
			// Inzializzo l'applicaizone
			updateApplication(this);
			inzializationFinsh = false;
		}

		// Aggiungo l'azione di aprire il menù delle categorie sull'ActionBar
		// dell'activity
		actionBar.addAction(new Action() {
			public void performAction(View view) {

				// Controllo del menu delle categorie
				menuCategory.checkMenuCategory();
			}

			// Seleziona un'icona per rappresentare l'azione appena aggiunta
			public int getDrawable() {
				return R.drawable.list;
			}
		});

		// Aggiungo l'azione di apertura del menù delle opzioni.
		actionBar.addAction(new Action() {

			@Override
			public void performAction(View view) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, SettingsActivity.class);
				startActivity(intent);
			}

			@Override
			public int getDrawable() {
				// TODO Auto-generated method stub
				return R.drawable.work;
			}
		});

		// Aggiungo l'azione di aggiornamento dell'applicazione
		actionBar.addAction(new Action() {

			@Override
			public void performAction(View view) {
				// TODO Auto-generated method stub
				updateApplication(context);
			}

			@Override
			public int getDrawable() {
				// TODO Auto-generated method stub
				return R.drawable.recycle_b;
			}
		});

		// Aggiungo un custom typface al text view
		TextView myTextView = (TextView) findViewById(R.id.search_poi);
		myTextView.setTypeface(ResourcesManager.getCustomTypeFace(this));

		// Inizializzo l'animazione sull'immagine del drawer
		ImageView image = (ImageView) findViewById(R.id.handle);
		image.startAnimation(AnimationUtils.loadAnimation(context,
				R.layout.slidemenu));

		// Inizalizzo un nuovo sliding drawer
		final SlidingDrawer drawerSearchPoi = (SlidingDrawer) findViewById(R.id.drawerSearchPoi);
		drawerSearchPoi.setOnDrawerOpenListener(new OnDrawerOpenListener() {

			@Override
			public void onDrawerOpened() {
				// TODO Auto-generated method stub

				// Quando il drawer viene aperto, viene avviata l'attivià di
				// ricerca poi
				Intent intent = new Intent(context, PoiSearchActivity.class);

				startActivity(intent);
				drawerSearchPoi.close();
			}
		});

		// Aggiungo un custom typeface alla text view
		TextView searchPoi = (TextView) findViewById(R.id.search_poi);
		searchPoi.setTypeface(ResourcesManager.getCustomTypeFace(context));

		// Inzializzo uno sliding drawer
		final SlidingDrawer addPoiDrawer = (SlidingDrawer) findViewById(R.id.addPoiDrawer);

		addPoiDrawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {

			@Override
			public void onDrawerOpened() {
				// TODO Auto-generated method stub
				// Quando il drawer viene aperto, viene avviata l'attivià di
				// aggiungi poi
				Intent intent = new Intent(context, AddPoiActivity.class);
				startActivity(intent);
				addPoiDrawer.close();
			}
		});

		// Controllo se devono essere visualizzati i suggerimenti
		checkHint();

	}

	/**
	 * Controllo se devono essere visualizzati gli hint nell'applicazione
	 */
	public void checkHint() {
		// recupero il parametro del boolean hint
		String choose = (String) ParametersBridge.getInstance().getParameter(
				"hint");
		// Se il valore non è stato ancora inzializzato, verranno mostrati i
		// suggerimenti
		if (choose == null) {
			UtilDialog
					.createBaseToast(
							"Trascina le frecce per avviare le funzionalità dell'applicazione",
							context).show();
		} else if (choose.equals("true")) {
			// se il valore è uguale a true, vengono visualizzati i suggerimenti
			UtilDialog
					.createBaseToast(
							"Trascina le frecce per avviare le funzionalità dell'applicazione",
							context).show();
		}
	}

	private void updateApplication(final Context context) {
		AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {

			ProgressDialog dialog;
			private CategoryListResponse categoryListResponse = null;

			@Override
			protected void onPreExecute() {
				// richiedo la location dell'utente

				dialog = ProgressDialog.show(context, "Attendere...",
						"Aggiornamento sistema");

			}

			@Override
			protected String doInBackground(Void... params) {
				// Vengono richieste le categorie al servizio WEB
				categoryListResponse = categoriesHandler
						.askCategoriesFromWebService();
				// Se L'operazione è andata a buon termine, le ctaegorie vengono
				// salvate dal gestore delle categorie.A seconda dell'esito
				// dell'operazione viene visualizzato un diverso messagio
				if (categoryListResponse != null
						&& categoryListResponse.getStatus() == 200) {

					categoriesHandler
							.setCategories((ArrayList<Category>) categoryListResponse
									.getCategories());
					// Viene creato un nuovo categoriesAdapter.
					// L'adattatore è un contenitore di categorie

					menuCategory = new MenuCategory(false,
							(ListView) findViewById(R.id.listView1),
							categoriesHandler, context);
					menuCategory.setVisibilityListCategory(false);

				}

				return null;
			}

			protected void onPostExecute(String result) {
				dialog.dismiss();
				if (categoryListResponse == null
						|| categoryListResponse.getStatus() != 200)
					UtilDialog.createBaseToast(
							"Impossibile recuperare la lista delle categorie",
							context).show();

			}

		};
		task.execute((Void) null);
	}

	/**
	 * Controllo se l'utente ha effettuato l'accesso al servizio google.
	 */
	private void checkUserAccount() {
		// Se non è stato trovato nemmeno un account, viene mostrato un
		// messaggio in cui viene richiesto di
		// effttuare l'accesso al servizio.
		if (AuthGoogle.getAccountName(activity) == null) {
			// Costruisco un nuovo diaologo
			AlertDialog.Builder builder = new AlertDialog.Builder(activity);
			builder.setMessage(
					"Non sei collegato con nessun account google, vuoi collegarti ora?")
					.setCancelable(false)
					.setPositiveButton("ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									Intent intent = new Intent(
											Settings.ACTION_SYNC_SETTINGS);
									intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									context.startActivity(intent);

									// Ricontrollo che
									// l'utente sia
									// utenticato,in
									// caso positivo,
									// proseguo con
									// l'inizializzazione
									// altrimenti viene
									// mostrato un
									// messaggio di
									// esito negativo e
									// l'applicazione
									// viene terminata
									if (AuthGoogle
											.googleServiceAviable(activity) == null) {

										// Creo un nuovo
										// dialogo
										AlertDialog.Builder builder = new AlertDialog.Builder(
												activity);
										builder.setMessage("Devi essere collegato ad account google per potere continuare");
										builder.setCancelable(false);
										builder.setNeutralButton(
												"Esci",
												new DialogInterface.OnClickListener() {
													public void onClick(
															DialogInterface dialog,
															int id) {
														// Viene
														// terminata
														// l'applicazione
														finish();
													}
												});

										// Viene
										// mostrato il
										// Dialogo
										builder.show();

									}
								}
							})
					.setNegativeButton("no",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

									// Se l'utente
									// sceglie di non
									// autenticarsi,
									// verrà mostrato un
									// dialogo e verrà
									// terminata
									// l'applicazione

									AlertDialog.Builder builder = new AlertDialog.Builder(
											activity);
									builder.setMessage("Devi essere collegato ad account google per potere continuare");
									builder.setCancelable(false);
									builder.setNeutralButton(
											"Esci",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int id) {
													activity.finish();
												}
											});
									builder.show();
								}
							});
			builder.show();
		}

	}

}
