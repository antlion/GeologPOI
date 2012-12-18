package com.geolog;

import java.util.ArrayList;
import com.geolog.dominio.Category;
import com.geolog.util.AuthGoogle;
import com.geolog.util.CategoriesAdapter;
import com.geolog.util.MyParser;
import com.geolog.util.UtilDialog;
import com.geolog.util.XmlCategoryCreator;
import com.geolog.web.Services;
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
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * @author Lorenzo
 * 
 */

public class GeologActivity extends Activity implements OnClickListener {

	//Barra di progresso della inzializzazione dell'applicazione
	private ProgressDialog progressBar;
	
	//Stato di avanzamento della progressBar
	private int progressBarStatus;
	
	//Gestore della barra di progresso
	private Handler progressBarHandler;
	
	//Stato avanzamento barra di progresso
	private long progress ;
	
	//Attirubuto contentente l'intera attività android
	private Activity activity;
	
	//Contesto dell'atitività
	private Context context;
	
	//Thread per l'inizializzazione dell'applicazione
	private Thread threadInzialization;
	
	//Stato del menù delle categorie
	private boolean menuCategoryOpen;
	
	//Gestore delle categorie
	private CategoriesHandler categoriesHandler;
	
	//Adattatore delle categorie
	private CategoriesAdapter categoriesAdapter;
	
	//Lista delle categorie
	private ListView listViewCategories;

	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.menu_activity_layout);
		
		//Inzializzazione dello stato della progressBar
		progressBarStatus = 0;
		
		//Creazione di unuovo gestore della progress Bar
		progressBarHandler = new Handler();
		
		//Inzializzazione del progresso della progressBar
		progress = 0;
		
		//Variabile contenente l'ActionBar dell'attività
		final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		
		//Settaggio dello stato dell'apertura del menù delle categorie
		setMenuCategoryOpen(false);
		
		// Inzializzazione del gestore delle categorie
		categoriesHandler = CategoriesHandler.getGestoreCategorie();

		//Salvataggio il contesto dell'attività
		context = getBaseContext();
		
		//Salvataggio dell'attività
		activity = this;

		//Inzializzo la lista delle cateogorie
		listViewCategories = (ListView) findViewById(R.id.listCategory);
		
		//
		
		// CONTROLLARE QUANDO NON SI RECUPERA LA LISTA DELLE CATEGORIE
		
		//Inzializzo l'applicaizone
		//doInizalizeTasks();

		
	

		listViewCategories.setVisibility(View.GONE);
		actionBar.addAction(new Action() {
			public void performAction(View view) {
				if (categoriesHandler.getCategorie() != null) {
					ListView listView = (ListView) findViewById(R.id.listCategory);

					if (menuCategoryOpen == false) {
						listView.setVisibility(View.VISIBLE);

						menuCategoryOpen = true;
					} else {
						menuCategoryOpen = false;
						listView.setVisibility(View.GONE);
						categoriesHandler.checkMenuCategory(categoriesAdapter);
					}
				}
			}

			public int getDrawable() {
				return R.drawable.poi_of_interest;
			}

		});

		// FindNearbyService.prova();
		Services services = new Services();
		services.downloadResource(
				"http://ia.media-imdb.com/images/M/MV5BMTc5MjgyMzk4NF5BMl5BanBnXkFtZTcwODk2OTM4Mg@@._V1._SY314_CR3,0,214,314_.jpg",
				context);

		/*
		 * AddPOIResponse result =(AddPOIResponse) services.addPOI(new Poi(new
		 * Category("mergenza", 0, 0), "ospdale","bello",0, null, location, 0),
		 * this); // SuggestionResponse result =(SuggestionResponse)
		 * services.addSuggestion(new Suggestion(0, "io", new Poi(new
		 * Category("mergenza", 0, 0), "ospdale","bello",0, null, location, 0),
		 * null), this); if (result == null) { Log.d("result", "null"); }
		 */
		// WebService service = new WebService(this);
		// service.findNearby(location, null);
		// PoiListResponse poi= FindNearbyService.prova();
		// poi = FindNearbyService.getPoiListResponse();
		// Log.d("numeroPois",String.valueOf(poi.getCount()));

		// services.getListCategory(context);

		Button aggiungiPOI = (Button) findViewById(R.id.Aggiungi_POI);
		aggiungiPOI.setOnClickListener(this);
		Button ricercaPOI = (Button) findViewById(R.id.ricerca_poi);
		ricercaPOI.setOnClickListener(this);
	}

	
	public void onClick(View v) {
		if (categoriesHandler.getCategorie() == null)
			UtilDialog.alertDialog(v.getContext(),
					"Impossibile procedere,lista categorie non recuperata")
					.show();
		else {
			if (v.getId() == R.id.Aggiungi_POI) {
				Intent intent = new Intent(v.getContext(), AddPOIActivity.class);
				startActivity(intent);

			}
			if (v.getId() == R.id.ricerca_poi) {
				Intent intent = new Intent(v.getContext(), POISearch.class);
				startActivity(intent);

			}
			/*
			 * Intent intent = new Intent(v.getContext(), POISearch.class);
			 * startActivity(intent);
			 * 
			 * 
			 * 
			 * Intent intent2 = new Intent(v.getContext(),
			 * AddPOIActivity.class); startActivity(intent);
			 * 
			 * Intent intent3 = new Intent(v.getContext(),
			 * ChoseCategoryActivity.class); startActivity(intent);
			 */
		}
	}
	/**
	 * Metodo per l'inizializzazione dell'applicazione.Controllo autenticazione utente e recupero lista delle categorie.
	 */
	private void inizializeApplication() {

		progressBar = new ProgressDialog(this);
		ImageView image = new ImageView(getBaseContext());

		image.setImageResource(R.drawable.inizializzazione2);
		progressBar.setTitle("Inizializzazione");
		progressBar.setCancelable(true);
		progressBar.setMessage("Autenticazione in corso ...");
		progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressBar.setProgress(0);
		progressBar.setMax(100);
		progressBar.show();

		// reset progress bar status
		progressBarStatus = 0;

		// reset filesize
		progress = 0;

		threadInzialization = new Thread(new Runnable() {
			public void run() {
				while (progressBarStatus < 100) {

					// process some tasks
					progressBarStatus = doInizalizeTasks();

					// your computer is too fast, sleep 1 second
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					// Update the progress bar
					progressBarHandler.post(new Runnable() {
						public void run() {
							progressBar.setProgress(progressBarStatus);
							if (progressBarStatus == 10) {
								progressBar
										.setMessage("Autenticazione in corso");
								if (AuthGoogle.googleServiceAviable(activity) == null) {
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
															// FIRE ZE MISSILES!
															// collegati account
															// goole
															Intent intent = new Intent(
																	Settings.ACTION_SYNC_SETTINGS);
															intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
															context.startActivity(intent);

															if (AuthGoogle
																	.googleServiceAviable(activity) == null) {
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
																				// User
																				// cancelled
																				// the
																				// dialog
																				finish();
																			}
																		});

																builder.show();
																finish();
															}
														}
													})
											.setNegativeButton(
													"no",
													new DialogInterface.OnClickListener() {
														public void onClick(
																DialogInterface dialog,
																int id) {
															// User cancelled
															// the dialog
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
																			// User
																			// cancelled
																			// the
																			// dialog
																			finish();
																		}
																	});

															builder.show();
														}
													});
									builder.show();
								}

							}
							if (progressBarStatus == 15)
								progressBar
										.setMessage("Autenticazione riuscita");
							if (progressBarStatus == 20)
								progressBar
										.setMessage("Recupero la lista delle categorie");
							if (progressBarStatus == 40) {
								categoriesAdapter = new CategoriesAdapter(
										getApplicationContext(),
										categoriesHandler.getCategorie());
								categoriesHandler.setSelectionCategory(listViewCategories,
										getApplicationContext(),
										categoriesAdapter);
								progressBar
										.setMessage("Lista categorie recuperata");
							}
							if (progressBarStatus == 55)
								progressBar
										.setMessage("impossibile recupererare la lista delle categorie");
							if (progressBarStatus == 90)
								progressBar
										.setMessage("Fine inizializzazione...");
						}
					});
				}

				// ok, file is downloaded,
				if (progressBarStatus >= 100) {

					// sleep 2 seconds, so that you can see the 100%
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					// close the progress bar dialog
					progressBar.dismiss();
				}
			}
		});
		threadInzialization.start();

	}

	/**
	 * Metodo usato per visualizzare l'avanzamento dell'applicazione sulla GUI.
	 * @return int numero che rappresenta l'avanzamento della inizializzazione dell'applicazione
	 */
	private int doInizalizeTasks() {

		while (progress <= 1000000) {

			progress++;

			if (progress == 100000) {
				return 10;
			} else if (progress == 150000) {
				return 15;
			} else if (progress == 200000) {
				return 20;
			} else if (progress == 300000) {

				CategoryListResponse categoryListResponse = categoriesHandler.richiediCategorieFromWeb();
				if (categoryListResponse != null && categoryListResponse.getStatus() == 200) {
					categoriesHandler.setCategorie((ArrayList<Category>) categoryListResponse.getCategories());
					return 40;
				} else {
					return 55;
				}
			} else if (progress == 500000) {

				return 50;
			} else if (progress == 900000) {
				return 90;
			}
			// ...add your own

		}

		return 100;

	}


	
	/**
	 * Metodo che restituisce true se il menù delle categorie è stato aperto, false altrimenti.
	 * @return stato dell'apertura del menù delle categorie
	 */
	private boolean isMenuCategoryOpen() {
		return menuCategoryOpen;
	}
	
	/**
	 * Metodo che assegna una stato di apertura del menu delle categorie
	 * @param menuCategoryOpen stato del menu delle cateogrie
	 */
	private void setMenuCategoryOpen(boolean menuCategoryOpen) {
		this.menuCategoryOpen = menuCategoryOpen;
	}

	

	

}
