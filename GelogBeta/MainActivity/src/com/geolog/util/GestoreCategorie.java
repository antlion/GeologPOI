package com.geolog.util;

import java.util.ArrayList;

import com.geolog.dominio.Categoria;

public class GestoreCategorie {
	
		private ArrayList<Categoria> categorie;
		private ArrayList<Categoria> categorieSelezionate;
		private static GestoreCategorie gestoreCategorie = null;
		
		
		public static synchronized GestoreCategorie getGestoreCategorie() {
	        if (gestoreCategorie == null) 
	            gestoreCategorie = new GestoreCategorie();
	        return gestoreCategorie;
	    	}
		public GestoreCategorie()
		{
			categorie = richiediCategorie();
			categorieSelezionate = categorie;
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
		
		public ArrayList<String> ottieniNomiCategorie()
		{
			ArrayList<String> nomi = new ArrayList<String>();
		
			for(Categoria categoria : categorie){
				nomi.add(categoria.getNomeCategoria());
			}
			return nomi;
		}
}
