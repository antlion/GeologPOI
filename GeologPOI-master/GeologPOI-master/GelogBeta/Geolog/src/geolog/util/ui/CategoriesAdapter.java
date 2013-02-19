package geolog.util.ui;
import geolog.managers.CategoriesManager;

import java.util.ArrayList;
import java.util.HashMap;

import com.geolog.activity.R;
import com.geolog.dominio.Category;



import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;


/**
 * Questa classe � una adattore personalizzato di categorie per popolare gli elementi di una listView. Ogni elemento della listView
 * contiene una categoria e una checkBox associata. Questa checkBox rappresenta se la categoria � stata scelta dall'utente.
 * 
 * @author Lorenzo
 *
 */
public class CategoriesAdapter extends BaseAdapter{


	//Contesto dell'attivit� che usa il categoriesAdapter
	/**
	 * @uml.property  name="context"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private Context context;
	
	//Lista delle categorie 
	/**
	 * @uml.property  name="categories"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="com.geolog.dominio.Category"
	 */
	private ArrayList<Category> categories; 

	//HashMaps contenente per ogni categoria un valore che rappresenta se la categoria � stata scelta dall'utente.
	/**
	 * @uml.property  name="categoriesSelected"
	 * @uml.associationEnd  qualifier="key:com.geolog.dominio.Category java.lang.String"
	 */
	private HashMap<Category,String> categoriesSelected;

	//CheckBox per la selezione della categoria
	/**
	 * @uml.property  name="checkBox"
	 * @uml.associationEnd  readOnly="true"
	 */
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
		
		if (CategoriesManager.getCategoriesManager().getCategoriesSelected().contains(categories.get(position))){
			chk.setChecked(true);
			updateCategorySelected(categories.get(position), "true");
		}
		//Recupero il nome che rappresenta la categoria specifica
		Typeface myTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/bebe.otf");
	  
		TextView tx = (TextView) convertView.findViewById(R.id.textView1);
		tx.setText(categories.get(position).getNomeCategoria());
		
		tx.setTypeface(myTypeface);

		return convertView;

	}

}

