package io.propty.propty;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private LoginButton fbLoginBtn;
    private Button btnLogin;
    private Button btnCancel;
    private Button btnRegister;
    private Button btnPassReset;
    private Button btnNext;
    private EditText inputEmail;
    private EditText inputPassword;
    private TextView loginResult;
    private TextView fbLoginResult;
    private Toolbar toolbar;
    private Button btnLoginAsHenry;
    private Button btnLoginAsDanny;

    private static String KEY_SUCCESS = "success";
    private static String KEY_UID = "uid";
    private static String KEY_USERNAME = "uname";
    private static String KEY_FIRSTNAME = "fname";
    private static String KEY_LASTNAME = "lname";
    private static String KEY_EMAIL = "email";
    private static String KEY_CREATED_AT = "created_at";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SharedPreferences prefs = getSharedPreferences(MainActivity.PREFS_NAME, 0);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);

        //set up the Toolbar with Up Navigation
        Toolbar toolbar = (Toolbar) findViewById(R.id.login_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Login");


        fbLoginBtn = (LoginButton) findViewById(R.id.fb_login_button);
        btnLogin = (Button) findViewById(R.id.login);
        btnCancel = (Button) findViewById(R.id.cancel);
        btnRegister = (Button) findViewById(R.id.registerbtn);
        btnPassReset = (Button) findViewById(R.id.passres);
        btnNext = (Button) findViewById(R.id.nextBtn);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.pword);
        loginResult = (TextView) findViewById(R.id.loginResult);
        fbLoginResult = (TextView) findViewById(R.id.fbLoginResult);
        btnLoginAsHenry = (Button) findViewById(R.id.loginAsHenry);
        btnLoginAsDanny = (Button) findViewById(R.id.loginAsDanny);

        if (prefs.getBoolean("logged_in", false)) {
            btnNext.setVisibility(View.VISIBLE);
        } else {
            btnNext.setVisibility(View.GONE);
        }

        fbLoginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            Resources res = getResources();

            @Override
            public void onSuccess(LoginResult loginResult) {
                String userId = loginResult.getAccessToken().getUserId();
                String token = loginResult.getAccessToken().getToken();
                String text = String.format(res.getString(R.string.facebook_login_success), userId, token);
                fbLoginResult.setText(text);
                btnNext.setVisibility(View.VISIBLE);
                prefs.edit().putBoolean("logged_in", true).apply();
            }

            @Override
            public void onCancel() {
                String text = res.getString(R.string.facebook_login_cancel);
                fbLoginResult.setText(text);
            }

            @Override
            public void onError(FacebookException e) {
                String text = res.getString(R.string.facebook_login_error);
                fbLoginResult.setText(text);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs.edit().putBoolean("logged_in", false).apply();
                Intent myIntent = new Intent(view.getContext(), MainActivity.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(myIntent);
            }
        });

        btnPassReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), ResetPasswordActivity.class);
                startActivityForResult(myIntent, 0);
                //commented out finish() due to improper UP and
                //BACK button navigation
//                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), RegisterActivity.class);
                startActivityForResult(myIntent, 0);
                //commented out finish() due to improper UP and
                //BACK button navigation
//                finish();
            }
        });

        /**
         * Login button click event
         * A Toast is set to alert when the Email and Password field is empty
         **/
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((!inputEmail.getText().toString().isEmpty()) && (!inputPassword.getText().toString().isEmpty())) {
                    // TODO: log the user in
                    Toast.makeText(getApplicationContext(),
                            "Logging in...", Toast.LENGTH_SHORT).show();
                    btnNext.setVisibility(View.VISIBLE);
                    prefs.edit().putBoolean("logged_in", true).apply();
                }
                else if ((!inputEmail.getText().toString().isEmpty())) {
                    Toast.makeText(getApplicationContext(),
                            "Password field is empty", Toast.LENGTH_SHORT).show();
                }
                else if ((!inputPassword.getText().toString().isEmpty())) {
                    Toast.makeText(getApplicationContext(),
                            "Email field is empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),
                            "Email and Password fields are empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SwipeCardActivity.class));
            }
        });

        btnLoginAsHenry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHandler db = new DatabaseHandler(getApplicationContext());

                db.addUser("Henry","Kerins","hkerins@ufl.edu","hkerins","uniqueID","06.26.2016");

                //Write the UID into MyPrefsFile so we can read settings from the settings table
                SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString(KEY_UID, "uniqueID");
                editor.apply();
            }
        });

        btnLoginAsDanny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHandler db = new DatabaseHandler(getApplicationContext());

                db.addUser("Danny","McBride","dmcbride@gmail.com","dmcbride","uniqueID2","06.27.2016");

                //Write the UID into MyPrefsFile so we can read settings from the settings table
                SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString(KEY_UID, "uniqueID2");
                editor.apply();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Async Task to check whether internet connection is working.
     **/
    private class NetCheck extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            nDialog = new ProgressDialog(LoginActivity.this);
            nDialog.setTitle("Checking Network");
            nDialog.setMessage("Loading..");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        /**
         * Gets current device state and checks for working internet connection by trying Google.
         **/
        @Override
        protected Boolean doInBackground(String... args) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL("http://www.google.com");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200) return true;
                } catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean th) {
            if (th) {
                nDialog.dismiss();
                new ProcessLogin().execute();
            }
            else {
                nDialog.dismiss();
                loginResult.setText(R.string.error_network_connection);
            }
        }
    }

    /**
     * Async Task to get and send data to My Sql database through JSON respone.
     **/
    private class ProcessLogin extends AsyncTask<String, Void, JSONObject> {
        private ProgressDialog pDialog;
        private String email, password;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            inputEmail = (EditText) findViewById(R.id.email);
            inputPassword = (EditText) findViewById(R.id.pword);
            email = inputEmail.getText().toString();
            password = inputPassword.getText().toString();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Logging in ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            UserFunctions userFunction = new UserFunctions();
            return userFunction.loginUser(email, password);
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                if (json.getString(KEY_SUCCESS) != null) {
                    String res = json.getString(KEY_SUCCESS);

                    if (Integer.parseInt(res) == 1) {
                        pDialog.setMessage("Loading User Space");
                        pDialog.setTitle("Getting Data");
                        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                        JSONObject json_user = json.getJSONObject("user");
                        // Clear all previous data in SQlite database.
                        UserFunctions logout = new UserFunctions();
                        logout.logoutUser(getApplicationContext());
                        db.addUser(json_user.getString(KEY_FIRSTNAME),json_user.getString(KEY_LASTNAME),json_user.getString(KEY_EMAIL),json_user.getString(KEY_USERNAME),json_user.getString(KEY_UID),json_user.getString(KEY_CREATED_AT));

                        //Write the UID into MyPrefsFile so we can read settings from the settings table
                        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(KEY_UID, json_user.getString(KEY_UID));
                        editor.apply();

                        // If JSON array details are stored in SQlite it launches the User Panel.
                        Intent upanel = new Intent(getApplicationContext(), MainActivity.class);
                        upanel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pDialog.dismiss();
                        startActivity(upanel);
                        // Close Login Screen
                        finish();
                    }
                    else {
                        pDialog.dismiss();
                        loginResult.setText(R.string.error_incorrect_user_pw);
                    }
                }
            } catch (JSONException e) { e.printStackTrace(); }
        }
    }

    public void NetAsync(View view) { new NetCheck().execute(); }



}
