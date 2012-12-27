package geolog.activities;


import geolog.managers.CategoriesManager;
import geolog.util.AuthGoogle;
import geolog.util.MenuCategory;
import geolog.util.UtilDialog;

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
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

/**
 * @author Lorenzo
 * 
 *         Questa class rappresenta l'attivit� principale dell'applicazione. Da
 *         questa attivit� � possibile aggiungere e ricercare i poi.
 * 
 */

public class GeologActivity extends Activity implements OnClickListener {

	// Barra di progresso della inzializzazione dell'applicazione
	/**
	 * @uml.property  name="progressBar"
	 * @uml.associationEnd  
	 */
	private ProgressDialog progressBar;

	// Stato di avanzamento della progressBar
	/**
	 * @uml.property  name="progressBarStatus"
	 */
	private int progressBarStatus;

	// Gestore della barra di progresso
	/**
	 * @uml.property  name="progressBarHandler"
	 * @uml.associationEnd  
	 */
	private Handler progressBarHandler;

	// Stato avanzamento dei Task dell'inizializzazione
	/**
	 * @uml.property  name="progress"
	 */
	private long progress;

	// Attirubuto contentente l'intera attivit� android
	/**
	 * @uml.property  name="activity"
	 * @uml.associationEnd  
	 */
	private Activity activity;

	// Contesto dell'atitivit�
	/**
	 * @uml.property  name="context"
	 * @uml.associationEnd  
	 */
	private Context context;

	// Thread per l'inizializzazione dell'applicazione
	/**
	 * @uml.property  name="threadInzialization"
	 */
	private Thread threadInzialization;

	// Gestore delle categorie
	/**
	 * @uml.property  name="categoriesHandler"
	 * @uml.associationEnd  
	 */
	private CategoriesManager categoriesHandler;

	//Men� delle categorie
	/**
	 * @uml.property  name="menuCategory"
	 * @uml.associationEnd  
	 */
	private MenuCategory menuCategory;
	
	//Stato della inzializzazione
	/**
	 * @uml.property  name="inzializationFinsh"
	 */
	private boolean inzializationFinsh;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.geolog_activity_layout);

		//Setto lo stato dell'inzializzazione
		inzializationFinsh = false;
		
		// Creazione di unuovo gestore della progress Bar
		progressBarHandler = new Handler();

		// Variabile contenente l'ActionBar dell'attivit�
		final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);

		// Inzializzazione del gestore delle categorie
		categoriesHandler = CategoriesManager.getCategoriesManager();

		// Salvataggio il contesto dell'attivit�
		context = getBaseContext();

		// Salvataggio dell'attivit�
		activity = this;

		// Inizializzo il bottone che permette di aprire l'attivit� di aggiunta
		// di poi
		Button addPoiButton = (Button) findViewById(R.id.add_poi);

		// Applico un listner al bottone di aggiunta di poi
		addPoiButton.setOnClickListener(this);

		// Inzializzo il bottone che permette di aprire l'attivit� di ricerca
		// dei poi
		Button searchPoiButton = (Button) findViewById(R.id.search_poi);

		// Applico un listner al bottone di ricerca dei poi
		searchPoiButton.setOnClickListener(this);

		//Creazione del menu delle categorie
		menuCategory = new MenuCategory(false,
				(ListView) findViewById(R.id.listCategory), categoriesHandler,
				context);
		//Setto la visibilit� del menu delle categorie
		menuCategory.setVisibilityListCategory(false);

		//Se l'inizializzazione non � stata eseguita, viene eseguita
		if(inzializationFinsh != true){
		// Inzializzo l'applicaizone
		inizializeApplication();
		inzializationFinsh = false;}

		// Aggiungo l'azione di aprire il men� delle categorie sull'ActionBar
		// dell'activity
		actionBar.addAction(new Action() {
			public void performAction(View view) {
				
				//Controllo del menu delle categorie
				menuCategory.checkMenuCategory();
			}

			// Seleziona un'icona per rappresentare l'azione appena aggiunta
			public int getDrawable() {
				return R.drawable.menucategories;
			}
		});

	

	}

	public void onClick(View v) {

		// Controllo se almeno una categoria di ricerca � stata scelta
		// dall'utente. Se almeno una � stata selezionata
		// non � possibile n� ricercare i poi, n� aggiungerne di nuovi.
		// Altrimenti, a seconda del bottone che � stato premuto, verr�
		// eseguita la corrispondente azione.

		if (categoriesHandler.getCategoriesSelected().size() > 0) {

			// Se il bottone premuto corrisponde all'addPoiButton, viene avviata
			// l'attivit� di aggiunta dei poi.
			if (v.getId() == R.id.add_poi) {
				Intent intent = new Intent(v.getContext(), AddPoiActivity.class);
				startActivity(intent);
			}

			// Se il bottone premuto corrisponde all'searchPoiButton, viene
			// avviata l'attivit� di ricerca dei poi.
			if (v.getId() == R.id.search_poi) {
				Intent intent = new Intent(v.getContext(),
						PoiSearchActivity.class);
				startActivity(intent);
			}
		} else
			UtilDialog.alertDialog(this,
					"Seleziona almeno una categoria per proseguire").show();

	}

	/**
	 * Metodo per l'inizializzazione dell'applicazione.Controllo autenticazione
	 * utente e recupero lista delle categorie.
	 */
	private void inizializeApplication() {

		// Viene creata una nuova barra di progresso
		progressBar = new ProgressDialog(this);

		// Viene settato il titolo della barra di progresso
		progressBar
				.setTitle(getString(R.string.title_progress_dialog_inzialization));

		// La progress bar non � cancellabile dall'utente
		progressBar.setCancelable(false);

		// Setto un messaggio per l'inizializzazione della barra di progresso
		progressBar
				.setMessage(getString(R.string.message_progress_dialog_inzialization));

		// Setto lo stile della barra di progresso
		progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

		// Seleziono da da quale percentuale deve partire la progressBar
		progressBar.setProgress(0);

		// Setto il valore massimo raggiungibile dalla progressBar
		progressBar.setMax(100);

		// Visualizzo la progressBar
		progressBar.show();

		// Rest dello stastus della progressBar
		progressBarStatus = 0;

		// Reset della percentuale di progresso dei task dell'inizializzazione
		progress = 0;

		// Start del Thread che gestisce la progressBar
		threadInzialization = new Thread(new Runnable() {
			public void run() {
				while (progressBarStatus < 100) {

					// Sato dei task dell'inizializzazione
					progressBarStatus = doInizalizeTasks();

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					// Update della progressBar
					progressBarHandler.post(new Runnable() {
						public void run() {

							// Setto l'avanzamento della progressBar, a seconda
							// della percentuale di completamento e dei task
							// eseguiti, veranno visualizati
							// diversi messaggi all'utente
							progressBar.setProgress(progressBarStatus);
							if (progressBarStatus == 10) {

								progressBar
										.setMessage(getString(R.string.message_autentication_progress_bar));

							}
							if (progressBarStatus == 15) {

								progressBar
										.setMessage(getString(R.string.message_autentication_progress_bar));

								// Se l'utente � autenticato, l'inizializazzione
								// procede a completare altri task, altrimenti
								// viene visualizzato un dialogo in cui si
								// richiede di autenticarsi
								if (AuthGoogle.googleServiceAviable(activity) == null) {

									// Creo un nuovo dialogo per
									// l'autenticazione
									AlertDialog.Builder builder = new AlertDialog.Builder(
											activity);
									builder.setMessage(
											"Non sei collegato con nessun account google, vuoi collegarti ora?")
											.setCancelable(false)
											.setPositiveButton(
													"ok",
													new DialogInterface.OnClickListener() {
														public void onClick(
																DialogInterface dialog,
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
											.setNegativeButton(
													"no",
													new DialogInterface.OnClickListener() {
														public void onClick(
																DialogInterface dialog,
																int id) {

															// Se l'utente
															// sceglie di non
															// autenticarsi,
															// verr� mostrato un
															// dialogo e verr�
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

							if (progressBarStatus == 20)

								progressBar
										.setMessage(getString(R.string.positive_auth_progress_bar));

							if (progressBarStatus == 30)
								progressBar
										.setMessage(getString(R.string.search_category_progress_bar));

							if (progressBarStatus == 40) {

								// Viene creato un nuovo categoriesAdapter.
								// L'adattatore � un contenitore di categorie

								menuCategory = new MenuCategory(
										false,
										(ListView) findViewById(R.id.listCategory),
										categoriesHandler, context);
								menuCategory.setVisibilityListCategory(false);
								progressBar
										.setMessage(getString(R.string.pos_search_category_progress_bar));

							}
							if (progressBarStatus == 60)

								progressBar
										.setMessage(getString(R.string.neg_search_categories));

							if (progressBarStatus == 90)

								progressBar
										.setMessage(getString(R.string.finish_inizialization_progress_bar));
						}
					});
				}

				// Se tutti i task sono stati completati, l'inizializzazione
				// termina, altrimenti continua ad eseguire nuovi task
				if (progressBarStatus >= 100) {

					// Sleep di due secondi per vedere il raggiungimento del
					// 100%
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					// La progress Bar viene chiusa
					progressBar.dismiss();
				}
			}
		});
		threadInzialization.start();

	}

	/**
	 * Metodo usato per visualizzare l'avanzamento dell'applicazione sulla GUI.
	 * 
	 * @return int numero che rappresenta l'avanzamento della inizializzazione
	 *         dell'applicazione
	 */
	private int doInizalizeTasks() {

		// Fin quando tutti i task non sono stati eseguiti rimane nel ciclo
		while (progress <= 1000000) {

			// Ad ogni iterazione aumento la percentuale di completamento dei
			// task
			progress++;

			// ad ogni soglia di completamento, viene eseguito un task
			if (progress == 100000) {

				return 10;
			} else if (progress == 150000) {

				return 15;

			} else if (progress == 200000) {

				return 20;

			} else if (progress == 300000) {

				return 30;
			} else if (progress == 400000) {

				// Vengono richieste le categorie al servizio WEB
				CategoryListResponse categoryListResponse = categoriesHandler
						.askCategoriesFromWebService();

				// Se L'operazione � andata a buon termine, le ctaegorie vengono
				// salvate dal gestore delle categorie.A seconda dell'esito
				// dell'operazione viene visualizzato un diverso messagio
				if (categoryListResponse != null
						&& categoryListResponse.getStatus() == 200) {

					categoriesHandler
							.setCategories((ArrayList<Category>) categoryListResponse
									.getCategories());

					return 40;
				} else {

					return 60;
				}

			} else if (progress == 500000) {

				return 50;

			} else if (progress == 900000) {

				return 90;
			}

		}

		return 100;

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	
	}
	
	
}