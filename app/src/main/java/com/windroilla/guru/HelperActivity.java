package com.windroilla.guru;

import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.windroilla.guru.authenticator.GuruAuthenticatorActivity;

import javax.inject.Inject;


public class HelperActivity extends ActionBarActivity {

    @Inject
    AccountManager accountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_helper);
        //Account[] accounts = accountManager.getAccountsByType(AuthConstants.ACCOUNT_TYPE);
        //accountManager.getAuthToken(accounts[0],AuthConstants.AUTHTOKEN_TYPE,null,null,null,null);
        startActivity(new Intent(this, GuruAuthenticatorActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_helper, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
