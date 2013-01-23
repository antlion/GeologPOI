package geolog.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

/**
 * Controllo degli account per l'accesso all'applicazione. L'autenticazione
 * avviene tramite il servizio esterno dell'account di google. Se L'utente e'
 * connesso al proprio account google, puo' accedere all'applicazione
 * 
 * @author Lorenzo
 * 
 */
public class AuthGoogle {



	/**
	 * Controllo che l'utente sia collegato ad ccount google
	 * 
	 * @param activity
	 *            attivita' che chiama il servizio
	 * @return Account dell'utente
	 */
	public static Account googleServiceAviable(Activity activity) {
		// Creo un nuovo accountManager
		AccountManager accountManager = AccountManager.get(activity);
		// prelevo tutti gli account disponibili registrati al servizio di
		// google
		Account[] accounts = accountManager.getAccountsByType("com.google");

		// creo una nuova variabile di tipo account
		Account myAccount = null;
		// Vedo se e' stato trovato un account, appena lo trovo interrompo il
		// ciclo
		for (Account account : accounts) {

			myAccount = account;
			break;

		}
		// Ritorno l'account trovato
		return myAccount;
	}
	
	public static String getAccountName(Activity activity){
		
		Account account = googleServiceAviable(activity);
		if (account!= null)
			return account.name;
		return null;
	}
	
	/**
	 * Viene controllato che l'utente sia connesso al servizio google.
	 * @param activity attività che richiama il metodo
	 * @return true, se l'utente e' connesso al servizio google,false altrimenti
	 */
	public static boolean isGoogleServiceAviable(Activity activity)
	{
		//Controllo se l'utente è connesso al servizio google
		if ( googleServiceAviable(activity) != null)
			return true;
		return false;
	}

	
}
