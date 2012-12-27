package geolog.util;


import geolog.managers.CategoriesManager;
import android.content.Context;
import android.view.View;
import android.widget.ListView;



/**
 * Menù delle categorie. Questa classe si occupa di gestire un menu delle
 * categorie. Salva la selezione delle categorie dell'utente.
 * 
 * @author Lorenzo
 * 
 */
public class MenuCategory {

	// Stato dell'espansione del menu delle categorie
	/**
	 * @uml.property  name="isExpandedMenuCateogories"
	 */
	private boolean isExpandedMenuCateogories;

	// ListView delle categorie
	/**
	 * @uml.property  name="listCategory"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private ListView listCategory;

	// Adattatore delle categorie
	/**
	 * @uml.property  name="categoryAdapter"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private CategoriesAdapter categoryAdapter;

	// gestore delle categorie
	/**
	 * @uml.property  name="categoriesManager"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private CategoriesManager categoriesManager;

	// Contesto dell'attività che richiedo l'uso del menu
	/**
	 * @uml.property  name="context"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private Context context;

	/**
	 * Csotruttore della classe
	 * 
	 * @param isExpandedMenuCateogories
	 *            stato di apertura del menu delle categorie
	 * @param listCategory
	 *            contenitore della lista delle categorie
	 * @param categoriesManager
	 *            gestore delle categorie
	 * @param context
	 *            contesto dell'attività che richiama il menù
	 */
	public MenuCategory(boolean isExpandedMenuCateogories,
			ListView listCategory, CategoriesManager categoriesManager,
			Context context) {
		super();
		this.isExpandedMenuCateogories = isExpandedMenuCateogories;
		this.listCategory = listCategory;

		this.categoriesManager = categoriesManager;

		this.context = context;

		this.categoryAdapter = new CategoriesAdapter(context,
				categoriesManager.getCategories());

		// Aggiunge le categorie che possono essere selezionate al contenitore
		// delle categorie
		categoriesManager.setSelectionCategory(listCategory, context,
				categoryAdapter);
	}

	/**
	 * @return
	 * @uml.property  name="isExpandedMenuCateogories"
	 */
	public boolean isExpandedMenuCateogories() {
		return isExpandedMenuCateogories;
	}

	public void setExpandedMenuCateogories(boolean isExpandedMenuCateogories) {
		this.isExpandedMenuCateogories = isExpandedMenuCateogories;
	}

	/**
	 * @return
	 * @uml.property  name="listCategory"
	 */
	public ListView getListCategory() {
		return listCategory;
	}

	/**
	 * @param listCategory
	 * @uml.property  name="listCategory"
	 */
	public void setListCategory(ListView listCategory) {
		this.listCategory = listCategory;
	}

	/**
	 * @return
	 * @uml.property  name="categoryAdapter"
	 */
	public CategoriesAdapter getCategoryAdapter() {
		return categoryAdapter;
	}

	/**
	 * @param categoryAdapter
	 * @uml.property  name="categoryAdapter"
	 */
	public void setCategoryAdapter(CategoriesAdapter categoryAdapter) {
		this.categoryAdapter = categoryAdapter;
	}

	/**
	 * @return
	 * @uml.property  name="categoriesManager"
	 */
	public CategoriesManager getCategoriesManager() {
		return categoriesManager;
	}

	/**
	 * @param categoriesManager
	 * @uml.property  name="categoriesManager"
	 */
	public void setCategoriesManager(CategoriesManager categoriesManager) {
		this.categoriesManager = categoriesManager;
	}

	/**
	 * Imposta la visibilità del menù delle categorie
	 * 
	 * @param visibility
	 *            stato della visibilità del menu delle categorie
	 */
	public void setVisibilityListCategory(boolean visibility) {
		if (visibility)
			listCategory.setVisibility(View.VISIBLE);
		else
			listCategory.setVisibility(View.GONE);
	}

	/**
	 * Controlla il menù delle categorie per vedere se sono state selezionate
	 * delle categorie per la ricerca di poi
	 * 
	 * @return boolean stato del controllo del menù delle categorie
	 */
	public boolean checkMenuCategory() {

		// Se il menù è aperto, controlla le categorie e chiudi il
		// menu,altrimenti apri il menù
		if (isExpandedMenuCateogories()) {

			// aggiorno lo stato dell'apertua delle categorie
			setExpandedMenuCateogories(false);
			// La lista delle categorie non è più visibile
			setVisibilityListCategory(false);

			// Controllo delle categorie selezionate
			categoriesManager.checkMenuCategory(categoryAdapter);

			return true;

		} else {
			setExpandedMenuCateogories(true);

			setVisibilityListCategory(true);
			return false;
		}
	}

	/**
	 * Aggiorna le categorie visibili nella lista delle categorie
	 */
	public void updateMenuCategory() {
		this.categoryAdapter = new CategoriesAdapter(context, CategoriesManager
				.getCategoriesManager().getCategories());
		categoriesManager.setSelectionCategory(listCategory, context,
				categoryAdapter);
	}
}
