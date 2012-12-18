package com.geolog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlSerializer;

import com.geolog.dominio.Category;
import com.geolog.util.MyParser;
import com.geolog.util.UtilDialog;
import com.geolog.util.XmlCategoryCreator;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ChoseCategoryActivity extends Activity implements OnClickListener {
	private ArrayList<CheckBox> categorie; 
	private ArrayList<Category> parsedData;
	private LinearLayout linearLayout;
	private CategoriesHandler gestoreCategorie;	   
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.view_category);
	        
	        categorie = new ArrayList<CheckBox>();
	        linearLayout = (LinearLayout) findViewById(R.id.category_layout);
	        
	        Button selezionaCategorie = (Button)findViewById(R.id.selectCategories);
		    selezionaCategorie.setOnClickListener(this);
		       
		    Button selectAllCat = (Button)findViewById(R.id.selectAllCstegories);
		    selectAllCat.setOnClickListener(this);
		    
		    Button deselectAllCat = (Button)findViewById(R.id.deselectAll);
		    deselectAllCat.setOnClickListener(this);
	        
	       // String xmlUrl="http://www.xxxx.xx/myfiles/messages.xml";
		   // MyParser parser=new MyParser(); 
	       // parser.parseXml(this.getApplicationContext());
	        //parsedData = new ArrayList<Categoria>();
	        //parsedData = parser.getParsedData();
	        gestoreCategorie = CategoriesHandler.getGestoreCategorie();
	        parsedData = gestoreCategorie.getCategorie();
	        
	        for(Category categoria : parsedData)
	        {
	        	addCategorie(categoria,linearLayout);
	        }
	   
	       
	}
	    public void addCategorie(Category categoria,LinearLayout linearLayout)
	    {
	    	CheckBox cb = new CheckBox(this.getApplicationContext());
        	cb.setText(categoria.getNomeCategoria());
        	linearLayout.addView(cb);
        	categorie.add(cb);
	    }
	    
	   public void onClick(View v) {
			// TODO Auto-generated method stub
		   	if (v.getId() == (R.id.selectAllCstegories)){
		   		setCheckBox(true);
		   	}
		   	if(v.getId() == (R.id.deselectAll)){
		   		setCheckBox(false);
		   	}
			creaSceltaCategorie();
			
		}
	   public void setCheckBox(boolean set)
	   {
		   for(CheckBox cb : categorie){
				cb.setChecked(set);
				}
	   }
	   public void creaSceltaCategorie()
	    {
	    	ArrayList<Category> categorieSelezionate = new  ArrayList<Category>();
			for(CheckBox cb : categorie){
				if(cb.isChecked()){
					Category categoria = new Category((String)cb.getText(),0,0);
					
					categorieSelezionate.add(categoria);
					}
			}
			if(categorieSelezionate.size() == 0)
				categorieSelezionate = parsedData;
				
				gestoreCategorie.salvaSelezione(categorieSelezionate);
				
				Toast ImageToast = new Toast(getBaseContext());
		        LinearLayout toastLayout = new LinearLayout(getBaseContext());
		        toastLayout.setOrientation(LinearLayout.HORIZONTAL);
		        ImageView image = new ImageView(getBaseContext());
		       
		        image.setImageResource(R.drawable.selezione_salvata);
		        
		        toastLayout.addView(image);
		      //  toastLayout.addView(text);
		        ImageToast.setView(toastLayout);
		        ImageToast.setDuration(Toast.LENGTH_LONG);
		        ImageToast.show();
				//String path = this.getFilesDir().toString();
				//System.out.println(path);
				//XmlCategoryCreator.createNewXml(categorieSelezionate,path);
	    }
}
