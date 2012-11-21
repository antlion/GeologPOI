package com.geolog;


import com.geolog.util.GestoreCategorie;
import com.geolog.util.MyParser;
import com.geolog.util.XmlCategoryCreator;
import com.geolog.web.WebService;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class MenuActivity extends Activity implements OnClickListener {
	
	ProgressDialog progressBar;
	private int progressBarStatus = 0;
	private Handler progressBarHandler = new Handler();
	private long fileSize = 0;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);
        
        
        
       // aaa();
        WebService.prova2();
        
       
        
        
        
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
		progressBar.setMessage("Contatto il server ...");
		progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressBar.setProgress(0);
		progressBar.setMax(100);
		progressBar.show();

		//reset progress bar status
		progressBarStatus = 0;

		//reset filesize
		fileSize = 0;

		new Thread(new Runnable() {
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
				  if(progressBarStatus == 10)
					  progressBar.setMessage("Controllo aggiornamenti applicazione");
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
	       }).start();

           }
	public int doSomeTasks() {
		
		
		while (fileSize <= 1000000) {
 
			fileSize++;
 
			if (fileSize == 100000) {
				return 10;
			} else if (fileSize == 200000) {
				return 20;
			} else if (fileSize == 500000) {
				GestoreCategorie gestoreCategorie = new GestoreCategorie();
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
			Intent intent = new Intent(v.getContext(), RicercaPOI.class);
	        startActivity(intent);
		}
		
		if(v.getId() == (R.id.visualizzaPOI)){
			Intent intent = new Intent(v.getContext(), WebService.class);
	        startActivity(intent);
		}
		if(v.getId() == (R.id.Aggiungi_POI)){
			Intent intent = new Intent(v.getContext(), AggiungiPOIActivity.class);
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
