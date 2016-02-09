package io.propty.propty;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import net.chainring.developer.propty.UserFunctions;
import net.chainring.developer.propty.DatabaseHandler;

import java.util.HashMap;

public class Main extends Activity {
    Button btnLogout;
    Button changepas;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        changepas = (Button) findViewById(R.id.btchangepass);
        btnLogout = (Button) findViewById(R.id.logout);

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        /**
         * Hashmap to load data from the Sqlite database
         **/
        HashMap user = new HashMap();
        user = db.getUserDetails();

        /**
         * Change Password Activity Started
         **/
        changepas.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){

                Intent chgpass = new Intent(getApplicationContext(), ChangePassword.class);

                startActivity(chgpass);
            }

        });

        /**
         *Logout from the User Panel which clears the data in Sqlite database
         **/
        btnLogout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                UserFunctions logout = new UserFunctions();
                logout.logoutUser(getApplicationContext());
                Intent login = new Intent(getApplicationContext(), Login.class);
                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(login);
                finish();
            }
        });
/**
 * Sets user first name and last name in text view.
 **/
        final TextView login = (TextView) findViewById(R.id.textwelcome);
        login.setText("Welcome  "+user.get("fname"));
        final TextView lname = (TextView) findViewById(R.id.lname);
        lname.setText(user.get("lname"));

    }}

