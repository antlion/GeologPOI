package com.geolog.util;

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
	
	
	
	
	public Account googleServiceAviable(Activity activity)
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

}
