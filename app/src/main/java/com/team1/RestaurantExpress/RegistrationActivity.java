package com.team1.RestaurantExpress;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class RegistrationActivity extends Activity {


    EditText usrTextBox;
    EditText emailAddress;
    private EditText phoneNumber;
    private EditText password1;
    private EditText password2;


    private View mProgressView;
    private View mRegFormView;

    private UserRegisterTask mRegTask;
    private CheckBox adminChk;


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        usrTextBox = (EditText) findViewById(R.id.username);

        emailAddress = (EditText) findViewById(R.id.emailAddress);

        phoneNumber = (EditText) findViewById(R.id.phoneNumber);

        password1 = (EditText) findViewById(R.id.password1);

        password2 = (EditText) findViewById(R.id.password2);

        adminChk = (CheckBox) findViewById(R.id.adminCheck);


        Button regButton = (Button) findViewById(R.id.regButton);
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });



        mRegFormView = findViewById(R.id.reg_form);
        mProgressView = findViewById(R.id.reg_progress);

    }


    private void registerUser(){

        boolean cancel = false;
        View focusView = null;

        String password1  = this.password1.getText().toString();
        String password2  = this.password2.getText().toString();

        String email  = this.emailAddress.getText().toString();
        String phone  = this.phoneNumber.getText().toString();
        String user  = this.usrTextBox.getText().toString();
        boolean isAdmin = this.adminChk.isChecked();


        if(TextUtils.isEmpty(email) || !isEmailValid(email)){
            this.emailAddress.setError(getString(R.string.error_field_required));
            focusView = this.emailAddress;
            cancel = true;

        } else 	if(TextUtils.isEmpty(user)){
            this.usrTextBox.setError(getString(R.string.invalid_user_name));
            focusView = this.usrTextBox;
            cancel = true;

        } else  if (TextUtils.isEmpty(password1) || TextUtils.isEmpty(password2) || !isPasswordValid(password1) || !password1.equals(password2)) {
            this.password1.setError(getString(R.string.error_invalid_password));
            focusView = this.password1;
            cancel = true;
        }

        if(cancel){
            focusView.requestFocus();
        }else  {
            showProgress(true);

            mRegTask  = new UserRegisterTask(email,password1,user,phone, isAdmin);

            mRegTask.execute((Void)null);
        }
    }



    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }




    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private final  String mPhone;
        private final String mUser;
        private final boolean mAdmin;


        UserRegisterTask(String email, String password, String user, String phone, boolean isAdmin) {
            mEmail = email;
            mPassword = password;
            mPhone = phone;
            mUser = user;
            mAdmin = isAdmin;
        }

        @Override
        protected Boolean doInBackground(Void... paramsData) {

            try {
                // Simulate network access.

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("user_email",mEmail));
                params.add(new BasicNameValuePair("user_password",mPassword));

                params.add(new BasicNameValuePair("user_contact",mPhone));

                params.add(new BasicNameValuePair("user_name",mUser));
                params.add(new BasicNameValuePair("is_admin", new Boolean(mAdmin).toString()));

                JSONParser jParser = new JSONParser();

                JSONObject json = jParser.makeHttpRequest(Config.register_page, "POST", params);

                if("1".equals(json.getString("success"))) {
                    return true;
                }

                // Check your log cat for JSON response
            } catch (Exception e) {
                //TODO Need To Show Error Page or Default Page
            }
            // TODO: register the new account here.
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mRegTask = null;
            showProgress(false);

            if (success) {
                //finish();
                Intent intent = new Intent(RegistrationActivity.this,LoginActivity.class);
                startActivity(intent);

            } else {
                usrTextBox.setError(getString(R.string.error_inregistration));
                usrTextBox.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mRegTask = null;
            showProgress(false);
        }
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRegFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mRegFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}