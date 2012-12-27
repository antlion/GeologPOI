package geolog.poi.visualization;



import java.util.ArrayList;

import com.geolog.dominio.Poi;

import android.content.Context;
import android.content.Intent;
import android.location.Location;




/**
 * Gestione della visualizzazione dei Poi. La classe si occupa di resituire una delle visualizzazione disponibili per visualizzare
 * i poi. 
 * 
 * Uso del pattern Factory
 * 
 * @author Lorenzo
 * 
 */
public class PoiViewManager {

	// Array list di poi
	/**
	 * @uml.property  name="pois"
	 */
	private ArrayList<Poi> pois;
	// Contesto dell'applicazione che richiama la classe
	/**
	 * @uml.property  name="ctx"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private Context ctx;
	// location dell'utente
	/**
	 * @uml.property  name="mylocation"
	 * @uml.associationEnd  
	 */
	private Location mylocation;

	/**
	 * Costruttore
	 * 
	 * @param context
	 *            contesto dell'applicazione che richiama la classe
	 */
	public PoiViewManager(Context context) {
		ctx = context;
		// inzializzo l'array di poi
		pois = new ArrayList<Poi>();
	}

	/**
	 * @return
	 * @uml.property  name="mylocation"
	 */
	public Location getMylocation() {
		return mylocation;
	}

	/**
	 * @param mylocation
	 * @uml.property  name="mylocation"
	 */
	public void setMylocation(Location mylocation) {
		this.mylocation = mylocation;
	}

	public ArrayList<Poi> getPois() {
		return pois;
	}

	public void setPois(ArrayList<Poi> pois) {
		this.pois = pois;
	}

	public Context getContext() {
		return ctx;
	}

	public void setContext(Context context) {
		this.ctx = context;
	}

	/**
	 * Richiesta di una particolare visulizzazione dei poi.
	 * 
	 * @param typeOfView
	 *            tipo di visualizzazione che si vuole ottenere
	 * @return Intent l'attività per la visualizzazione dei poi richiesta
	 *         dall'utente
	 */
	public Intent createNewViewIntent(String typeOfView) {
		return PoiViewFactory.getView(typeOfView, ctx, mylocation, getPois());
	}

}
