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
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText email;
    private TextView alert;
    private Button resetpass, login;

    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword);

        //set up the Toolbar with Up Navigation
        Toolbar toolbar = (Toolbar) findViewById(R.id.reset_password_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Reset Password");



        email = (EditText) findViewById(R.id.forpas);
        alert = (TextView) findViewById(R.id.alert);
        resetpass = (Button) findViewById(R.id.respass);
        login = (Button) findViewById(R.id.bktolog);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), LoginActivity.class);
                startActivityForResult(myIntent, 0);
                finish();
            }
        });

        resetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { NetAsync(view); }
        });
    }

    private class NetCheck extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            nDialog = new ProgressDialog(ResetPasswordActivity.this);
            nDialog.setMessage("Loading..");
            nDialog.setTitle("Checking Network");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

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
                alert.setText("Error in Network Connection");
            }
        }
    }

    private class ProcessRegister extends AsyncTask<String, Void, JSONObject> {
        private ProgressDialog pDialog;
        String forgotpassword;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            forgotpassword = email.getText().toString();
            pDialog = new ProgressDialog(ResetPasswordActivity.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.forPass(forgotpassword);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            // Checks if the Password Change Process is sucesss
            try {
                if (json.getString(KEY_SUCCESS) != null) {
                    alert.setText("");
                    String res = json.getString(KEY_SUCCESS);
                    String red = json.getString(KEY_ERROR);

                    if (Integer.parseInt(res) == 1) {
                        pDialog.dismiss();
                        alert.setText(R.string.recovery_email);

                    }
                    else if (Integer.parseInt(red) == 2) {
                        pDialog.dismiss();
                        alert.setText(R.string.error_email_not_found);
                    }
                    else {
                        pDialog.dismiss();
                        alert.setText(R.string.error_pw_change);
                    }

                }
            } catch (JSONException e) { e.printStackTrace(); }
        }
    }

    public void NetAsync(View view) { new NetCheck().execute(); }
}
