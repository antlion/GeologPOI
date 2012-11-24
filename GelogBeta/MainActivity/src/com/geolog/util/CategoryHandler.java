package com.geolog.util;

import java.util.ArrayList;
import java.util.HashMap;

import com.geolog.dominio.Categoria;

public class CategoryHandler {
	
		private ArrayList<Categoria> categorie;
		private ArrayList<Categoria> categorieSelezionate;
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
		
		public ArrayList<Categoria> richiediCategorie()
		{
		//contatta il webServer per le categorie
		Categoria categoria = new Categoria("categoria di prova",0);
		Categoria categoria2 = new Categoria("categoria di prova2",0);
		ArrayList<Categoria> categorie = new ArrayList<Categoria>();
		categorie.add(categoria);
		categorie.add(categoria2);
		return categorie;
		}
	
		public ArrayList<Categoria> richiediCategorieSelezionate()
		{
			return categorieSelezionate;
		}
		
		public void salvaSelezione(ArrayList<Categoria> categorieScelte)
		{
			categorieSelezionate = categorieScelte;
			
		}
		
		public void updateCategorySelected2()
		{
			categorySelected2 = new HashMap<String,String>();
			for(Categoria categoria: categorieSelezionate)
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
		
			for(Categoria categoria : categorie){
				nomi.add(categoria.getNomeCategoria());
			}
			return nomi;
		}
}
