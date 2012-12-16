package com.geolog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.geolog.dominio.Category;
import com.geolog.util.CategoryAdapter;
import com.geolog.web.Services;
import com.geolog.web.domain.CategoryListResponse;

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
		private CategoryHandler()
		{
			
		}
		
		
		
		public CategoryListResponse richiediCategorieFromWeb()
		{
			Services services = new Services();
			CategoryListResponse categoryListResponse = services.getListCategory();
			return categoryListResponse;
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
		
		public  int getCategoryIdFromSource(String string)
		{
			for(Category categoria : categorie){
				if(categoria.getNomeCategoria().equals(string))
				return categoria.getId();
			}
			return -1;
		}
		
		public Category getCategoryFromId(int idCateogry)
		{
			for(Category categoria : categorie){
				if(categoria.getId() == idCateogry)
				return categoria;
			}
			return null;
		}
		public void checkMenuCategory(CategoryAdapter categoryChoose)
		{
			ArrayList<Category> categoriaSelezionate = new ArrayList<Category>();
			
			int number = categoryChoose.getCount();
			
			HashMap<Category,String> map = categoryChoose.aaaa();
			Set<Category> list  = map.keySet();
			Iterator iter = list.iterator();
						
			while(iter.hasNext()) {
			     Category key = (Category) iter.next();
			     String value = map.get(key);
			     if (value.equals("true"))
			    	 categoriaSelezionate.add(key);
			}
		
			salvaSelezione(categoriaSelezionate);
			
			}
		
		public void setSelectionCategory(ListView listView,Context context,CategoryAdapter category)
		{
			
			listView.setAdapter(category);
	        listView.setBackgroundResource(R.drawable.customshape);
	        listView.setClickable(true);
	        listView.setItemsCanFocus(false);
	        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	        listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					// TODO Auto-generated method stub
					CheckBox cb = (CheckBox) arg1.findViewById(R.id.category_checkbox);
					CategoryAdapter nuovo =(CategoryAdapter)arg0.getAdapter();
					Category category = (Category) nuovo.getItem(arg2);
					if (cb.isChecked()){
						cb.setChecked(false);
						nuovo.modifyHash(category, "false");
						}
					else{
						cb.setChecked(true);
						nuovo.modifyHash(category, "true");
						}
					
				}
			});
		}
		public ArrayList<Category> getCategorie() {
			return categorie;
		}
		public void setCategorie(ArrayList<Category> categorie) {
			this.categorie = categorie;
		}
		public ArrayList<Category> getCategorieSelezionate() {
			return categorieSelezionate;
		}
		public void setCategorieSelezionate(ArrayList<Category> categorieSelezionate) {
			this.categorieSelezionate = categorieSelezionate;
		}
		public void setCategorySelected2(HashMap<String, String> categorySelected2) {
			this.categorySelected2 = categorySelected2;
		}
		public static void setGestoreCategorie(CategoryHandler gestoreCategorie) {
			CategoryHandler.gestoreCategorie = gestoreCategorie;
		}
		
		
		
}