package com.geolog;

import android.content.Context;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.geolog.util.CategoriesAdapter;

public class MenuCategory {

	//Stato dell'espansione del menu delle categorie
		private boolean isExpandedMenuCateogories;

		//ListView delle categorie
		private ListView listCategory;

		
		//Adattatore delle categorie
		private CategoriesAdapter categoryAdapter;

		//gestore delle categorie
		private CategoriesManager categoriesManager;

		private View viewMenuCategory;

		private Context context;
		
		public MenuCategory(boolean isExpandedMenuCateogories,
				ListView listCategory,CategoriesAdapter categoryAdapter,
				CategoriesManager categoriesManager, View viewMenuCategory,Context context) {
			super();
			this.isExpandedMenuCateogories = isExpandedMenuCateogories;
			this.listCategory = listCategory;
			this.categoryAdapter = categoryAdapter;
			this.categoriesManager = categoriesManager;
			this.viewMenuCategory = viewMenuCategory;
			this.context = context;
			categoriesManager.setSelectionCategory(listCategory,context, categoryAdapter);
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

		public View getViewMenuCategory() {
			return viewMenuCategory;
		}

		public void setViewMenuCategory(View viewMenuCategory) {
			this.viewMenuCategory = viewMenuCategory;
		}
		
		public void setVisibilityListCategory(boolean visibility)
		{
			if (visibility)
				listCategory.setVisibility(View.VISIBLE);
			else
				listCategory.setVisibility(View.GONE);
		}
		
		public boolean checkMenuCategoty()
		{
				if (isExpandedMenuCateogories()) {
				
				//aggiorno lo stato dell'apertua delle categorie
				setExpandedMenuCateogories(false);
				//La lista delle categorie non è più visibile
				setVisibilityListCategory(false);
				
				//Controllo delle categorie selezionate
				categoriesManager.checkMenuCategory(categoryAdapter);
				
				return true;
				
				//aggiorno la poszione
				
				
			} else {
				setExpandedMenuCateogories(true);
			
				setVisibilityListCategory(true);
				return false;
			}
		}
		
}
