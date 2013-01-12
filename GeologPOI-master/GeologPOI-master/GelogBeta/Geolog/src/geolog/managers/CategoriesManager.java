package geolog.managers;

import geolog.util.ui.CategoriesAdapter;
import geolog.web.WebService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import java.util.Set;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;


import com.geolog.activity.R;
import com.geolog.dominio.Category;
import com.geolog.dominio.Poi;
import com.geolog.web.domain.CategoryListResponse;

/**
 * Classe che si occupa di gestire le categorie. Ogni modifica e aggiunta di una
 * categoria, viene gestita da questa classe. Viene salvata anche la selezione
 * delle categorie da parte dell'utente. Questa classe viene gestita come un
 * sigleton. Questa scelta è dovuto al fatto che le cateogorie non cambiano
 * durante l'esecuzione dell'applicazione. Poichè le categorie devono essere
 * passate a più oggetti, non c'è bisogno di istanziarle più volte. Inoltre per
 * passare le categorie selezionate dall'utente, la creazione di nuove istanze
 * di questa classe elminerebbero sia le scelte dell'utente e creerebbero la
 * necessità di recuperare di nuovo la lsita delle categorie dal servizio web.
 * 
 * @author Lorenzo
 * 
 */
public class CategoriesManager {

	// Lista delle categorie
	/**
	 * @uml.property  name="categories"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="com.geolog.dominio.Category"
	 */
	private ArrayList<Category> categories;

	// Lista delle categorie selezionate dall'utente
	/**
	 * @uml.property  name="categoriesSelected"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="com.geolog.dominio.Category"
	 */
	private ArrayList<Category> categoriesSelected;

	// Vairiabile per la gestione del singleton
	private static CategoriesManager categoriesManager = null;

	// Metodo statico per l'accesso
	public static synchronized CategoriesManager getCategoriesManager() {
		if (categoriesManager == null)
			categoriesManager = new CategoriesManager();
		return categoriesManager;
	}

	// Costruttore della classe
	private CategoriesManager() {
		categories = new ArrayList<Category>();
		categoriesSelected = new ArrayList<Category>();
	}

	/**
	 * Richiedi al servizio web le categorie di ricerca dei punti di interesse
	 * 
	 * @return CategoryListResponse la risposta del web server alla richiesta
	 */
	public CategoryListResponse askCategoriesFromWebService() {
		// Crea un nuovo accesso al servizio Web
		WebService services = new WebService();

		// Richiedi le categorie al servizio
		CategoryListResponse categoryListResponse = services.getListCategory();

		// Ritorna la risposta
		return categoryListResponse;
	}

	/**
	 * Costruzione di una stringa conenente tutti gli id delle categorie
	 * selezionante dall'utente
	 * 
	 * @return String string di tutti gli id delle categorie selezionante
	 *         dall'utente
	 */
	public String getStringIdFromSelectedCategory() {
		// Creo una nuova stringa
		String categoryIdSelected = "";

		// Per ogni categoria selezionata dall'utente, aggiungo l'id alla
		// stringa
		for (Category category : categoriesSelected) {
			categoryIdSelected += String.valueOf(category.getId()) + ",";
		}

		// ritorno la stringa selezionata
		return categoryIdSelected;
	}

	/**
	 * Dalla lista di categorie vengono estratti i nomi.
	 * 
	 * @return String[] array di stringhe contente i nomi delle categorie
	 */
	public String[] getCategoriesName() {
		// Definizione di un nuovo array di stringhe
		String[] nomi = new String[categories.size()];

		// variabile di appoggio per il ciclo
		int count = 0;
		// Per ogni categoria, ne estraggo il nome e la salvo nell'array di
		// stringhe
		for (Category categoria : categories) {
			nomi[count] = categoria.getNomeCategoria();
			count++;
		}

		// Resituisco l'array di stringhe
		return nomi;
	}

	/**
	 * Dato un nome di una categoria, viene resituito l'id corrispondendente
	 * della categoria.
	 * 
	 * @param categoryName
	 *            nome della categoria
	 * @return int id della categoria corrispondente al nome passato come
	 *         paramentro
	 */
	public int getCategoryIdFromSource(String categoryName) {
		// Cerco la categoria che il nome corrispondente al parametro
		// categoryName, se la categoria non esiste, restituisco un valore
		// negativo,altrimenti l'id della
		// categoria
		for (Category categoria : categories) {
			if (categoria.getNomeCategoria().equals(categoryName))
				return categoria.getId();
		}
		return -1;
	}

	/**
	 * Recupero della categoria a partire da un id di una categoria
	 * 
	 * @param idCateogry
	 *            l'id di una categoria
	 * @return Category la categoria corrispondente all'id passato come
	 *         paramentro.
	 */
	public Category getCategoryFromId(int idCateogry) {
		// Ricerco la catgoria corrispondente all'id passato come paramentro, se
		// la categoria non esiste termino l'operazione con esito negativo
		for (Category categoria : categories) {
			if (categoria.getId() == idCateogry)
				return categoria;
		}
		return null;
		// ricordare di gestire il caso in cui c'è una nuova categoria
	}

	/**
	 * Vengono controllate le categorie scelte dall'utente per effettuare la
	 * ricerca di punti di interesse. L'adattatore contiente le cateogorie
	 * scelte. Ogni categoria scelta viene aggiunta alla lista delle categorie
	 * scelte dall'utente.
	 * 
	 * @param categoryChoose
	 */
	public void checkMenuCategory(CategoriesAdapter categoryChoose) {
		// Definisco un nuovo array che conterrà le categorie scelte dall'utente
		ArrayList<Category> categoriaSelezionate = new ArrayList<Category>();

		// Ottengo l'hash map che contiene per ogni categoria, un stringa che
		// rappresenta un valore true,se la cateogoria è stata scelta,false
		// altrimenti.
		HashMap<Category, String> map = categoryChoose.getCategorySelected();
		// Trasformo le categorie dell'hashMap in una lista di categorie
		Set<Category> list = map.keySet();
		// creo un nuovo iterator
		Iterator<Category> iter = list.iterator();
		// Per ogni elemento della lista delle categorie, controllo il
		// corripondente valore, se è true,lacategoria viene aggiunta alla lista
		// delle categorie scelte dall'utente.
		while (iter.hasNext()) {
			Category key = (Category) iter.next();
			String value = map.get(key);
			if (value.equals("true"))
				categoriaSelezionate.add(key);
		}
		// aggiungo le categorie scelte alla lista delle categorie scelte
		// dall'utente
		setCategoriesSelected(categoriaSelezionate);
	}

	/**
	 * Gestione degli elementi di una ListView contenente una lista di
	 * categorie. Le categorie sono contenute in un adattatore di categorie.
	 * Ogni elemento selezionato viene salvato all'interno di un
	 * hashMap<Category,String>, dove per ogni categoria c'è un valore che
	 * rappresenta se la categoria è stata selezionata.
	 * 
	 * 
	 * @param listView
	 *            listView contenente le categorie che possono essere
	 *            selezionate
	 * @param context
	 *            contesto dell'attività che chiama il metodo
	 * @param category
	 *            adattatore di categorie contenente le categorie
	 */
	public void setSelectionCategory(ListView listView, Context context,
			CategoriesAdapter category) {
		// Setto l'adattore di categorie alla listView
		listView.setAdapter(category);
		listView.setBackgroundResource(R.drawable.customshape);
		listView.setClickable(true);
		listView.setItemsCanFocus(false);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		// Ogni elemento della categoria viene aggiunto ad un listener.Quando
		// una categoria viene selezionata, viene estratta la categoria
		// dall'adattatore e viene salvata
		// òa selezione
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				// Recupero la checkBox della selezione
				CheckBox cb = (CheckBox) arg1
						.findViewById(R.id.category_checkbox);

				// Recupero l'adattore di categorie dalla slezione dell'utente
				CategoriesAdapter categoryAdapter = (CategoriesAdapter) arg0
						.getAdapter();
				// Recupero la categoria scelta dall'utente
				Category category = (Category) categoryAdapter.getItem(arg2);

				// Controlo se la categoria è stata selezionata, se è
				// false,aggiorno il valore della selezione a false,true
				// altrimenti.
				if (cb.isChecked()) {
					cb.setChecked(false);
					categoryAdapter.updateCategorySelected(category, "false");
					
					checkMenuCategory(categoryAdapter);
				} else {
					cb.setChecked(true);
					categoryAdapter.updateCategorySelected(category, "true");
					checkMenuCategory(categoryAdapter);

				}
			}
		});
	}
	
	public List<Poi> setCategoriesOnPoi(List<Poi> poiList)
	{
		for(Poi poi: poiList)
		{
			Category category = getCategoryFromId(poi.getCategory_id());
			poi.setCategory(category);
		}
		return poiList;
	}

	public ArrayList<Category> getCategories() {
		return categories;
	}

	public void setCategories(ArrayList<Category> categories) {
		this.categories = categories;
	}

	public ArrayList<Category> getCategoriesSelected() {
		return categoriesSelected;
	}

	public void setCategoriesSelected(ArrayList<Category> categoriesSelected) {
		this.categoriesSelected = categoriesSelected;
	}

}
