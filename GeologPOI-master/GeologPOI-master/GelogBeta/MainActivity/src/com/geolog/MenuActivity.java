package com.geolog;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.geolog.dominio.Category;
import com.geolog.dominio.Poi;
import com.geolog.dominio.Suggestion;
import com.geolog.util.AuthGoogle;
import com.geolog.util.CategoryAdapter;

import com.geolog.util.MyParser;
import com.geolog.util.UtilDialog;
import com.geolog.util.XmlCategoryCreator;
import com.geolog.web.FindNearbyService;
import com.geolog.web.Services;
import com.geolog.web.WebService;
import com.geolog.web.domain.AddPOIResponse;
import com.geolog.web.domain.BaseResponse;
import com.geolog.web.domain.CategoryListResponse;
import com.geolog.web.domain.PoiListResponse;
import com.geolog.web.domain.SuggestionResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;


import android.provider.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

public class MenuActivity extends Activity implements OnClickListener {
	
	ProgressDialog progressBar;
	private int progressBarStatus = 0;
	private Handler progressBarHandler = new Handler();
	private long fileSize = 0;
	private Activity activity;
	private Context context;
	private Thread prova;
	private boolean menuCategoryOpen;
	private CategoryHandler categoryHandler;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        
        setContentView(R.layout.menu_activity_layout);
        
        final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
        menuCategoryOpen = false;
        categoryHandler = new CategoryHandler();
        
        context = getBaseContext();
        activity = this;
        
    
       // aaa();
      
        Location location = new Location("nuova");
        location.setLatitude(41.857);
        location.setLongitude(12.632897);
        final CategoryHandler cc = new CategoryHandler();
        
        final ListView listView = (ListView)findViewById(R.id.listCategory);
        final CategoryAdapter categoryAdapter = new CategoryAdapter(getApplicationContext(), cc.richiediCategorie());
        cc.setSelectionCategory(listView, getApplicationContext(),categoryAdapter);
      
      
       /* listView.setAdapter(categoryAdapter);
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
		});*/
        
        
        
        
       listView.setVisibility(View.GONE);
        actionBar.addAction(new Action() {
            public void performAction(View view) {
          
           	ListView listView = (ListView)findViewById(R.id.listCategory);
                    	
        
    	      if( menuCategoryOpen == false){
    	        listView.setVisibility(View.VISIBLE);
    	        
    	        menuCategoryOpen = true;}
    	      else
    	      {
    	    	  menuCategoryOpen = false;
    	    	  listView.setVisibility(View.GONE);
    	    	  categoryHandler.checkMenuCategory(categoryAdapter);
    	      }
           	
            }
            public int getDrawable() {
                return R.drawable.poi_of_interest;
            }

		
        });
        
        //FindNearbyService.prova();
      Services services = new Services();
     
      
     /* AddPOIResponse result =(AddPOIResponse) services.addPOI(new Poi(new Category("mergenza", 0, 0), "ospdale","bello",0, null, location, 0), this);
      //  SuggestionResponse result =(SuggestionResponse) services.addSuggestion(new Suggestion(0, "io", new Poi(new Category("mergenza", 0, 0), "ospdale","bello",0, null, location, 0), null), this);
        if (result == null)
        {
        	Log.d("result", "null");
        }*/
      //  WebService service = new WebService(this);
       // service.findNearby(location, null);
      // PoiListResponse poi= FindNearbyService.prova();
      // poi = FindNearbyService.getPoiListResponse();
       //Log.d("numeroPois",String.valueOf(poi.getCount()));
  
        
        
        
     
        
        //services.getListCategory(context);
      
      Button aggiungiPOI = (Button) findViewById(R.id.Aggiungi_POI);
      aggiungiPOI.setOnClickListener(this);
	}
	
	
	
	

	public void aaa()
	{
		
		progressBar = new ProgressDialog(this);
		 ImageView image = new ImageView(getBaseContext());
	       
	        image.setImageResource(R.drawable.inizializzazione2);
		progressBar.setTitle("Inizializzazione");
		progressBar.setCancelable(true);
		progressBar.setMessage("Autenticazione in corso ...");
		progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressBar.setProgress(0);
		progressBar.setMax(100);
		progressBar.show();

		//reset progress bar status
		progressBarStatus = 0;

		//reset filesize
		fileSize = 0;

	prova =  new Thread(new Runnable() {
		  public void run() {
			while (progressBarStatus < 100) {

			  // process some tasks
			  progressBarStatus = doSomeTasks();
			 
			  // your computer is too fast, sleep 1 second
			  try {
				Thread.sleep(1000);
			  } catch (InterruptedException e) {
				e.printStackTrace();
			  }

			  // Update the progress bar
			  progressBarHandler.post(new Runnable() {
				public void run() {
				  progressBar.setProgress(progressBarStatus);
				  if(progressBarStatus == 10){
					  progressBar.setMessage("Autenticazione in corso");
					  if(AuthGoogle.googleServiceAviable(activity) == null)
					  {
						  AlertDialog.Builder builder = new AlertDialog.Builder(activity);
					        builder.setMessage("Non sei collegato con nessun account google, vuoi collegarti ora?")
					        .setCancelable(false)
					               .setPositiveButton("ok", new DialogInterface.OnClickListener() {
					                   public void onClick(DialogInterface dialog, int id) {
					                       // FIRE ZE MISSILES!
					                	   //collegati account goole
					                	   Intent intent = new Intent(Settings.ACTION_SYNC_SETTINGS);
					                	   intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					                	   context.startActivity(intent);
					                	  
					                	   if(AuthGoogle.googleServiceAviable(activity) == null){
					                		   AlertDialog.Builder builder = new AlertDialog.Builder(activity);
										        builder.setMessage("Devi essere collegato ad account google per potere continuare");
										        builder.setCancelable(false);
										        builder.setNeutralButton("Esci",  new DialogInterface.OnClickListener() {
										                   public void onClick(DialogInterface dialog, int id) {
										                       // User cancelled the dialog
										                	  finish();
										                   }
										               });
										              
										        builder.show();
					                	  finish();
					                	   }
					                   }
					               })
					               .setNegativeButton("no", new DialogInterface.OnClickListener() {
					                   public void onClick(DialogInterface dialog, int id) {
					                       // User cancelled the dialog
					                	   AlertDialog.Builder builder = new AlertDialog.Builder(activity);
									        builder.setMessage("Devi essere collegato ad account google per potere continuare");
									        builder.setCancelable(false);
									        builder.setNeutralButton("Esci",  new DialogInterface.OnClickListener() {
									                   public void onClick(DialogInterface dialog, int id) {
									                       // User cancelled the dialog
									                	  finish();
									                   }
									               });
									              
									        builder.show();
					                   }
					               });
					        builder.show();
					  }
					  
					  }
				  if(progressBarStatus == 15)
					  progressBar.setMessage("Autenticazione riuscita");
				  if(progressBarStatus == 20)
					  progressBar.setMessage("Recupero la lista delle categorie");
				  if(progressBarStatus == 50 ){
					  progressBar.setMessage("Lista categorie recuperata");
					  
					  }
				  if(progressBarStatus == 90 )
					  progressBar.setMessage("Fine inizializzazione...");
				}
			  });
			}

			// ok, file is downloaded,
			if (progressBarStatus >= 100) {

				// sleep 2 seconds, so that you can see the 100%
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				// close the progress bar dialog
				progressBar.dismiss();
			}
		  }
	       });
	prova.start();

           }
	public int doSomeTasks() {
		
		
		while (fileSize <= 1000000) {
 
			fileSize++;
 
			if (fileSize == 100000) {
				return 10;
			} else if (fileSize == 150000) {
				return 15;
			} else if (fileSize == 200000) {
				return 20;
			} else if (fileSize == 300000){
				CategoryHandler gestoreCategorie = new CategoryHandler();
				Services services = new Services();
				services.asd();
				
			}
			else if (fileSize == 500000) {
				
				return 50;
			} else if (fileSize == 900000){
				return 90;
			}
			// ...add your own
 
		}
 
		return 100;
 
	}
	
	public void onClick(View v) {
			if (v.getId() == R.id.Aggiungi_POI){ 
				Intent intent2 = new Intent(v.getContext(), AddPOIActivity.class);
		        startActivity(intent2);
			}
			/*Intent intent = new Intent(v.getContext(), POISearch.class);
	        startActivity(intent);
	
		
	        
			Intent intent2 = new Intent(v.getContext(), AddPOIActivity.class);
	        startActivity(intent);
	
		Intent intent3 = new Intent(v.getContext(), ChoseCategoryActivity.class);
        startActivity(intent);*/
	}
	public void inizializzaFileCategorie()
	{
		MyParser parser = new MyParser();
		parser.parseXml(this);
		String path = (this.getFilesDir().toString());
		XmlCategoryCreator.createNewXml(parser.getParsedData(),path);
		
	}
	
	

		
	
}
