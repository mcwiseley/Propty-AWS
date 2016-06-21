package io.propty.propty;

/**
 * Created by micheal on 11/18/15.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private EditText inputFirstName, inputLastName, inputUsername, inputEmail, inputPassword;
    private Button btnRegister, btnLogin;
    private TextView registerErrorMsg;

    // JSON Response node names.
    private static String KEY_SUCCESS = "success";
    private static String KEY_UID = "uid";
    private static String KEY_FIRSTNAME = "fname";
    private static String KEY_LASTNAME = "lname";
    private static String KEY_USERNAME = "uname";
    private static String KEY_EMAIL = "email";
    private static String KEY_CREATED_AT = "created_at";
    private static String KEY_ERROR = "error";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //set up the Toolbar with Up Navigation
        Toolbar toolbar = (Toolbar) findViewById(R.id.register_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Register");



        inputFirstName = (EditText) findViewById(R.id.fname);
        inputLastName = (EditText) findViewById(R.id.lname);
        inputUsername = (EditText) findViewById(R.id.uname);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.pword);
        btnRegister = (Button) findViewById(R.id.register);
        btnLogin = (Button) findViewById(R.id.bktologin);
        registerErrorMsg = (TextView) findViewById(R.id.register_error);

        /**
         * Back to Login Button click event.
         * Switches back to the login screen when clicked
         */
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), LoginActivity.class);
                startActivityForResult(myIntent, 0);
                finish();
            }
        });

        /**
         * Register Button click event.
         * A Toast is set to alert when the fields are empty.
         * Another toast is set to alert Username must be 5 characters.
         **/
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( (!inputUsername.getText().toString().isEmpty()) &&
                     (!inputPassword.getText().toString().isEmpty()) &&
                     (!inputFirstName.getText().toString().isEmpty()) &&
                     (!inputLastName.getText().toString().isEmpty()) &&
                     (!inputEmail.getText().toString().isEmpty())       ) {
                    if (inputUsername.getText().toString().length() > 4) {
                        NetAsync(view);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),
                                "Username should be minimum 5 characters", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),
                            "One or more fields are empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Async Task to check whether internet connection is working
     **/
    private class NetCheck extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            nDialog = new ProgressDialog(RegisterActivity.this);
            nDialog.setMessage("Loading..");
            nDialog.setTitle("Checking Network");
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
                new ProcessRegister().execute();
            }
            else {
                nDialog.dismiss();
                registerErrorMsg.setText(R.string.error_network_connection);
            }
        }
    }

    private class ProcessRegister extends AsyncTask<String, Void, JSONObject> {
        private ProgressDialog pDialog;
        String email,password,fname,lname,uname;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            inputUsername = (EditText) findViewById(R.id.uname);
            inputPassword = (EditText) findViewById(R.id.pword);
            fname = inputFirstName.getText().toString();
            lname = inputLastName.getText().toString();
            email = inputEmail.getText().toString();
            uname= inputUsername.getText().toString();
            password = inputPassword.getText().toString();
            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Registering ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            UserFunctions userFunction = new UserFunctions();
            return userFunction.registerUser(fname, lname, email, uname, password);
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            // Checks for success message.
            try {
                if (json.getString(KEY_SUCCESS) != null) {
                    registerErrorMsg.setText("");
                    String res = json.getString(KEY_SUCCESS);
                    String red = json.getString(KEY_ERROR);

                    if (Integer.parseInt(res) == 1) {
                        pDialog.setTitle("Getting Data");
                        pDialog.setMessage("Loading Info");
                        registerErrorMsg.setText(R.string.register_success);

                        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                        JSONObject json_user = json.getJSONObject("user");

                        //Removes all the previous data in the SQlite database
                        UserFunctions logout = new UserFunctions();
                        logout.logoutUser(getApplicationContext());

                        // Stores registered data in SQlite Database
                        db.addUser(json_user.getString(KEY_FIRSTNAME),json_user.getString(KEY_LASTNAME),json_user.getString(KEY_EMAIL),json_user.getString(KEY_USERNAME),json_user.getString(KEY_UID),json_user.getString(KEY_CREATED_AT));

                        // Launch RegisteredActivity screen
                        Intent registered = new Intent(getApplicationContext(), RegisteredActivity.class);

                        // Close all views before launching RegisteredActivity screen
                        registered.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pDialog.dismiss();
                        startActivity(registered);

                        finish();
                    }
                    else if (Integer.parseInt(red) == 2) {
                        pDialog.dismiss();
                        registerErrorMsg.setText(R.string.error_user_exists);
                    }
                    else if (Integer.parseInt(red) == 3) {
                        pDialog.dismiss();
                        registerErrorMsg.setText(R.string.error_invalid_email);
                    }
                }
                else {
                    pDialog.dismiss();
                    registerErrorMsg.setText(R.string.error_register);
                }
            } catch (JSONException e) { e.printStackTrace(); }
        }
    }

    public void NetAsync(View view) { new NetCheck().execute(); }
}
