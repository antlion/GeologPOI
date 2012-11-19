package com.geolog;
import com.geolog.util.*;
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
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tx = (TextView)findViewById(R.id.textView1);
        Button button =(Button)findViewById(R.id.button1);
        
        Account account = new AuthGoogle().googleServiceAviable(this);
        if(account != null){
        button.setText(account.toString());
       
        tx.setText( "clicca per accedere");
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	   Intent intent = new Intent(v.getContext(), MenuActivity.class);
                startActivity(intent);
            }
        });
       // Intent intent = new Intent(this,MenuActivity.class);
        //startActivity(intent);
        }
        else{
        tx.setText("Devi essere connesso un account google");}
    
    
    
      /*  AccountManagerFuture<Bundle> getAuthToken(Account account, String authTokenType, boolean notifyAuthFailure, AccountManagerCallback<Bundle> callback, Handler handler);
        
        
        
       /* AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        Account[] list = manager.getAccounts();
        String gmail = null;

        for(Account account: list)
        {
            if(account.type.equalsIgnoreCase("com.google"))
            {
                gmail = account.name;
                break;
            }
        }
        System.out.println(gmail+"lkjkl");*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
   
}
