package com.geolog;


import com.geolog.util.AuthGoogle;
import com.geolog.util.CategoryHandler;
import com.geolog.util.MyParser;
import com.geolog.util.XmlCategoryCreator;
import com.geolog.web.FindNearbyService;
import com.geolog.web.PoiListResponse;
import com.geolog.web.WebService;

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
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MenuActivity extends Activity implements OnClickListener {
	
	ProgressDialog progressBar;
	private int progressBarStatus = 0;
	private Handler progressBarHandler = new Handler();
	private long fileSize = 0;
	private Activity activity;
	private Context context;
	private Thread prova;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity_layout);
        
     ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
     // You can also assign the title programmatically by passing a
     // CharSequence or resource id.
     //actionBar.setTitle(R.string.some_title);
  
     //actionBar.setHomeLogo(R.drawable.arrow_down);
    // actionBar.addAction(new IntentAction(this, createShareIntent(), R.drawable.ic_title_share_default));
    // actionBar.addAction(new ToastAction());
     actionBar.addAction(new Action() {
         public void performAction(View view) {
            // Toast.makeText(HomeActivity.this, "Added action.", Toast.LENGTH_SHORT).show();
         }
         public int getDrawable() {
             return R.drawable.icon;
         }
     });
        
        context = getBaseContext();
        
        activity = this;
        //aaa();
        Location location = new Location("nuova");
        location.setLatitude(41.857);
        location.setLongitude(12.632897);
        CategoryHandler cc = new CategoryHandler();
      //  WebService service = new WebService(this);
       // service.findNearby(location, null);
      // PoiListResponse poi= FindNearbyService.prova();
      // poi = FindNearbyService.getPoiListResponse();
       //Log.d("numeroPois",String.valueOf(poi.getCount()));
      //ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar1);
     // You can also assign the title programmatically by passing a
     // CharSequence or resource id.
     //actionBar.setTitle("Geolog");
     //actionBar.setHomeAction(null);
     //actionBar.addAction(new IntentAction(this, createShareIntent(), R.drawable.ic_title_share_default));
     //actionBar.addAction(new ToastAction());
        
        
        
        Button b1 = (Button)findViewById(R.id.selezionaCategorie);
        b1.setOnClickListener(this);
        
        Button b2 = (Button)findViewById(R.id.visualizzaPOI);
        b2.setOnClickListener(this);
       
        Button b3 = (Button)findViewById(R.id.ricerca_poi);
        b3.setOnClickListener(this);
        
        Button b4 = (Button)findViewById(R.id.Aggiungi_POI);
        b4.setOnClickListener(this);
        
	}

	public void aaa()
	{
		
		progressBar = new ProgressDialog(this);
		 ImageView image = new ImageView(getBaseContext());
	       
	        image.setImageResource(R.drawable.inizializzazione2);
		progressBar.setCustomTitle(image);
		progressBar.setCancelable(true);
		progressBar.setMessage("Autenticazione in corso ...");
		progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
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
				  if(progressBarStatus == 30)
					  progressBar.setMessage("Recupero la lista delle categorie");
				  if(progressBarStatus == 50 )
					  progressBar.setMessage("Lista categorie recuperata");
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
			}else if (fileSize == 500000) {
				CategoryHandler gestoreCategorie = new CategoryHandler();
				return 50;
			} else if (fileSize == 900000){
				return 90;
			}
			// ...add your own
 
		}
 
		return 100;
 
	}
	
	public void onClick(View v) {
		if(v.getId() == (R.id.ricerca_poi)){
			Intent intent = new Intent(v.getContext(), POISearch.class);
	        startActivity(intent);
		}
		
		if(v.getId() == (R.id.visualizzaPOI)){
			
		}
		if(v.getId() == (R.id.Aggiungi_POI)){
			Intent intent = new Intent(v.getContext(), AddPOIActivity.class);
	        startActivity(intent);
		}
		if(v.getId() == (R.id.selezionaCategorie)) {
		Intent intent = new Intent(v.getContext(), ChoseCategoryActivity.class);
        startActivity(intent);}
	}
	
	public void inizializzaFileCategorie()
	{
		MyParser parser = new MyParser();
		parser.parseXml(this);
		String path = (this.getFilesDir().toString());
		XmlCategoryCreator.createNewXml(parser.getParsedData(),path);
		
	}

}
