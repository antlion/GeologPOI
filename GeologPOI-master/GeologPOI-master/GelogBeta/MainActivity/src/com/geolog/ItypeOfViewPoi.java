package com.geolog;




import android.content.Context;
import android.location.Location;

/**
 * @author Lorenzo
 *
 * Interfaccia che per la rappresentazione delle vari tipi di visualizzazione dei poi. Ogni visualizzazione deve avere il memtodo che
 * aggiorna la posizione dell'utente(in base alla posizione è possibile ricercare i poi) e ricerca poi.
 */
public interface ItypeOfViewPoi {

	
	void updateLocationData(Location location);
	void searchPoi(Context context);
	
	
}
