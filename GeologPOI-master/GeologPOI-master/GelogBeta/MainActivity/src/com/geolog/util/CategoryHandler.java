package com.geolog.util;

import java.util.ArrayList;
import java.util.HashMap;

import com.geolog.dominio.Category;

public class CategoryHandler {
	
		private ArrayList<Category> categorie;
		private ArrayList<Category> categorieSelezionate;
		private HashMap<String,String> categorySelected2;
		
		private static CategoryHandler gestoreCategorie = null;
		
		
		public static synchronized CategoryHandler getGestoreCategorie() {
	        if (gestoreCategorie == null) 
	            gestoreCategorie = new CategoryHandler();
	        return gestoreCategorie;
	    	}
		public CategoryHandler()
		{
			categorie = richiediCategorie();
			categorieSelezionate = categorie;
			updateCategorySelected2();
		}
		
		public ArrayList<Category> richiediCategorie()
		{
		//contatta il webServer per le categorie
		Category categoria = new Category("categoria di prova",0,0);
		Category categoria2 = new Category("categoria di prova2",0,0);
		ArrayList<Category> categorie = new ArrayList<Category>();
		categorie.add(categoria);
		categorie.add(categoria2);
		return categorie;
		}
	
		public ArrayList<Category> richiediCategorieSelezionate()
		{
			return categorieSelezionate;
		}
		
		public void salvaSelezione(ArrayList<Category> categorieScelte)
		{
			categorieSelezionate = categorieScelte;
			
		}
		
		public void updateCategorySelected2()
		{
			categorySelected2 = new HashMap<String,String>();
			for(Category categoria: categorieSelezionate)
			{
				categorySelected2.put(categoria.getNomeCategoria(), "true");
			}
		}
		
		
		public HashMap<String, String> getCategorySelected2() {
			return categorySelected2;
		}
		public ArrayList<String> ottieniNomiCategorie()
		{
			ArrayList<String> nomi = new ArrayList<String>();
		
			for(Category categoria : categorie){
				nomi.add(categoria.getNomeCategoria());
			}
			return nomi;
		}
		
		public String getIdSelectedCategory()
		{
			String id = "";
			for(Category cat: categorieSelezionate)
			{
				id +=String.valueOf(cat.getId())+",";
			}
			return id;
		}
}
