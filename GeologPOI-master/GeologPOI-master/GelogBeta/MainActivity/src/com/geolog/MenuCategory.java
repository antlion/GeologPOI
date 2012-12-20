package com.geolog;

import geolog.util.CategoriesAdapter;
import android.content.Context;
import android.view.View;
import android.widget.ListView;


/**
 * Men� delle categorie. Questa classe si occupa di gestire un menu delle
 * categorie. Salva la selezione delle categorie dell'utente.
 * 
 * @author Lorenzo
 * 
 */
public class MenuCategory {

	// Stato dell'espansione del menu delle categorie
	private boolean isExpandedMenuCateogories;

	// ListView delle categorie
	private ListView listCategory;

	// Adattatore delle categorie
	private CategoriesAdapter categoryAdapter;

	// gestore delle categorie
	private CategoriesManager categoriesManager;

	// Contesto dell'attivit� che richiedo l'uso del menu
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
	 *            contesto dell'attivit� che richiama il men�
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

	public boolean isExpandedMenuCateogories() {
		return isExpandedMenuCateogories;
	}

	public void setExpandedMenuCateogories(boolean isExpandedMenuCateogories) {
		this.isExpandedMenuCateogories = isExpandedMenuCateogories;
	}

	public ListView getListCategory() {
		return listCategory;
	}

	public void setListCategory(ListView listCategory) {
		this.listCategory = listCategory;
	}

	public CategoriesAdapter getCategoryAdapter() {
		return categoryAdapter;
	}

	public void setCategoryAdapter(CategoriesAdapter categoryAdapter) {
		this.categoryAdapter = categoryAdapter;
	}

	public CategoriesManager getCategoriesManager() {
		return categoriesManager;
	}

	public void setCategoriesManager(CategoriesManager categoriesManager) {
		this.categoriesManager = categoriesManager;
	}

	/**
	 * Imposta la visibilit� del men� delle categorie
	 * 
	 * @param visibility
	 *            stato della visibilit� del menu delle categorie
	 */
	public void setVisibilityListCategory(boolean visibility) {
		if (visibility)
			listCategory.setVisibility(View.VISIBLE);
		else
			listCategory.setVisibility(View.GONE);
	}

	/**
	 * Controlla il men� delle categorie per vedere se sono state selezionate
	 * delle categorie per la ricerca di poi
	 * 
	 * @return boolean stato del controllo del men� delle categorie
	 */
	public boolean checkMenuCategory() {

		// Se il men� � aperto, controlla le categorie e chiudi il
		// menu,altrimenti apri il men�
		if (isExpandedMenuCateogories()) {

			// aggiorno lo stato dell'apertua delle categorie
			setExpandedMenuCateogories(false);
			// La lista delle categorie non � pi� visibile
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
