package geolog.util;

import android.accounts.Account;
import android.accounts.AccountManager;

import android.app.Activity;

/**
 * Controllo degli account per l'accesso all'applicazione. L'autenticazione
 * avviene tramite il servizio esterno dell'account di google. Se L'utente è
 * connesso al proprio account google, può accedere all'applicazione
 * 
 * @author Lorenzo
 * 
 */
public class AuthGoogle {

	/**
	 * Controllo che l'utente sia collegato ad ccount google
	 * 
	 * @param activity
	 *            attività che chiama il servizio
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
		// Vedo se è stato trovato un account, appena lo trovo interrompo il
		// ciclo
		for (Account account : accounts) {

			myAccount = account;
			break;

		}
		// Ritorno l'account trovato
		return myAccount;
	}

}
