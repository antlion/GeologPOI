package activity.util;
import java.util.ArrayList;
import java.util.HashMap;

import com.geolog.R;


import activity.dominio.Category;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;


/**
 * Questa classe è una adattore personalizzato di categorie per popolare gli elementi di una listView. Ogni elemento della listView
 * contiene una categoria e una checkBox associata. Questa checkBox rappresenta se la categoria è stata scelta dall'utente.
 * 
 * @author Lorenzo
 *
 */
public class CategoriesAdapter extends BaseAdapter{


	//Contesto dell'attività che usa il categoriesAdapter
	private Context context;
	
	//Lista delle categorie 
	private ArrayList<Category> categories; 

	//HashMaps contenente per ogni categoria un valore che rappresenta se la categoria è stata scelta dall'utente.
	private HashMap<Category,String> categoriesSelected;

	//CheckBox per la selezione della categoria
	private CheckBox checkBox;

	//Costruttore della categoria
	public CategoriesAdapter(Context context, ArrayList<Category> categorie) {
		this.context = context; 
		this.categories = categorie; 

		//Inzializzo le categorie selezionate con tutti valori false
		categoriesSelected = new HashMap<Category,String>();
		for(Category catcategory: categorie){
			categoriesSelected.put(catcategory, "false");
		}

	} 

	
	public HashMap<Category, String> getCategorySelected() {
		return categoriesSelected;
	}

	
	public void setCategorySelected(HashMap<Category, String> categorySelected) {
		this.categoriesSelected = categorySelected;
	}

	
	public int getCount() { 
		return categories.size(); 
	} 
	
	
	public Object getItem(int position) { 
		return categories.get(position); 
	} 
	
	
	public long getItemId(int position) { 
		return categories.get(position).hashCode(); 
	} 
	
	
	public boolean getCheckBoxstatus()
	{
		return checkBox.isChecked();
	}

	
	/**
	 * Viene aggiornato il valore contenuto nella struttura dati delle categorie selezionate.
	 * 
	 * @param category categoria che deve essere aggiornata
	 * @param valore valore della categoria che deve essere aggiornata
	 */
	public void updateCategorySelected(Category category, String valore)
	{
		//Rimuovo la vecchia categoria con il corrispondente valore
		categoriesSelected.remove(category);
		//Aggiungo la categoria aggiornata alla lista delle categorie selezionate
		categoriesSelected.put(category, valore);
	}

	
	public View getView(final int position, View convertView, ViewGroup parent) {

		//Recupero gli elementi della GU
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.layout.category_row_list, null);

		//Recupero la checkBox contenente la specifica categoria
		CheckBox chk = (CheckBox) convertView.findViewById(R.id.category_checkbox);
		chk.setClickable(false);
		
		//Recupero il nome che rappresenta la categoria specifica
		TextView tx = (TextView) convertView.findViewById(R.id.textView1);
		tx.setText(categories.get(position).getNomeCategoria());

		return convertView;

	}

}

