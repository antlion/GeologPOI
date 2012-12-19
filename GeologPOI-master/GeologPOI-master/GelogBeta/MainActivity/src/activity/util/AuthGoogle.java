package activity.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.widget.TextView;

import com.geolog.R;
import android.os.Bundle;
import android.os.Handler;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.widget.TextView;

public class AuthGoogle {
	
	
	
	
	public static Account googleServiceAviable(Activity activity)
	{
		  AccountManager accountManager = AccountManager.get(activity);
	        Account[] accounts = accountManager.getAccountsByType("com.google");
	        Account myAccount = null;
	        for (Account account : accounts) {
	        	System.out.println(account.toString());
	        	
	             
	             myAccount = account;
	                break;
	            
	        }
	        return myAccount;
	}

	
	public static void aaaaa(Activity activity)
	{
		AccountManager manager = AccountManager.get(activity);
		Account[] accounts = manager.getAccountsByType("com.google");
		manager.getAuthToken(AuthGoogle.googleServiceAviable(activity), "”Manage your tasks”", null,activity, new AccountManagerCallback<Bundle>() {
		    public void run(AccountManagerFuture<Bundle> future) {
		      try {
		        // If the user has authorized your application to use the tasks API
		        // a token is available.
		    	  
		    	  Bundle bundle = future.getResult();
		    	  String token = bundle.getString(AccountManager.KEY_AUTHTOKEN);
		        //String token = future.getResult().getString(AccountManager.KEY_AUTHTOKEN);
		        // Now you can use the Tasks API...
		        // Setting up the Tasks API Service
		        
		      } catch (OperationCanceledException e) {
		        // TODO: The user has denied you access to the API, you should handle that
		      } catch (Exception e) {
		        e.printStackTrace();
		      }
		    }
		  }, null);
	}
}
