package com.windroilla.guru;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.windroilla.guru.api.ApiService;
import com.windroilla.guru.api.RequestNewRegistration;
import com.windroilla.guru.api.UserProfile;
import com.windroilla.guru.authenticator.GuruAuthenticatorActivity;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class SignUpActivity extends ActionBarActivity {

    public final static String TAG = SignUpActivity.class.getSimpleName();

    @Inject
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GuruApp.getsInstance().graph().inject(this);
        setContentView(R.layout.activity_sign_up);
        findViewById(R.id.signup_form_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = ((EditText) findViewById(R.id.signup_form_firstname)).getText().toString();
                String lastName = ((EditText) findViewById(R.id.signup_form_lastname)).getText().toString();
                final String email = ((EditText) findViewById(R.id.signup_form_email)).getText().toString();
                String mobileNumber = ((EditText) findViewById(R.id.signup_form_mobile_number)).getText().toString();
                final String password = ((EditText) findViewById(R.id.signup_form_password)).getText().toString();
                String confirmPassword = ((EditText) findViewById(R.id.signup_form_confirm_password)).getText().toString();
                String fatherName = ((EditText) findViewById(R.id.signup_form_father_name)).getText().toString();
                String ambition = ((EditText) findViewById(R.id.signup_form_ambition)).getText().toString();
                String address = ((EditText) findViewById(R.id.signup_form_address)).getText().toString();
                if (!password.equals(confirmPassword)) {
                    Log.d(TAG, "passwords do not match");
                    //TODO handle the passwords doesn't match case
                } else {
                    //TODO handle field validations for required field
                    apiService.registerNewUser(
                            new RequestNewRegistration(
                                    email,
                                    password,
                                    mobileNumber,
                                    ambition,
                                    firstName,
                                    lastName,
                                    fatherName,
                                    address
                            ))
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    new Action1<UserProfile>() {
                                        @Override
                                        public void call(UserProfile userProfile) {
                                            Intent intent = new Intent();
                                            intent.putExtra(GuruAuthenticatorActivity.PARAM_USER_NAME, email);
                                            intent.putExtra(GuruAuthenticatorActivity.PARAM_USER_PASS, password);
                                            setResult(RESULT_OK, intent);
                                            finish();
                                        }
                                    },
                                    new Action1<Throwable>() {
                                        @Override
                                        public void call(Throwable throwable) {
                                            Log.e(TAG, "SignUp failed!" + throwable);
                                            Toast.makeText(getBaseContext(), "User registration failed! Please try again!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                            );
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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
