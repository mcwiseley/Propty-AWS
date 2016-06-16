package io.propty.propty;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
//import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "MyPrefsFile";

    Button btnChangePass;
    Button btnLogout;
    Button btnLogin;
    Button btnSwipeCard;
    Button btnSettings;
    Button btnRealtor;
    Button btnDatabase;
    Resources res;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnChangePass = (Button) findViewById(R.id.btchangepass);
        btnLogout = (Button) findViewById(R.id.btlogout);
        btnLogin = (Button) findViewById(R.id.btlogin);
        btnSwipeCard = (Button) findViewById(R.id.btswipecard);
        btnSettings = (Button) findViewById(R.id.settings);
        btnRealtor = (Button) findViewById(R.id.btrealtor);
        btnDatabase = (Button) findViewById(R.id.btdatabase);
        res = getResources();

        // Restore preferences
        final SharedPreferences prefs = getSharedPreferences(PREFS_NAME, 0);
        boolean new_user = prefs.getBoolean("new_user", true);
        boolean logged_in = prefs.getBoolean("logged_in", false);

        if (new_user) {
            prefs.edit().putBoolean("new_user", false).apply();
        } else if (logged_in) {
            startActivity(new Intent(getApplicationContext(), SwipeCardActivity.class));
        }

        /**
         * Change Password Activity Started
         **/
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chgPass = new Intent(getApplicationContext(), ChangePasswordActivity.class);
                startActivity(chgPass);
            }
        });

        /**
         *Logout from the User Panel which clears the data in Sqlite database
         **/
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs.edit().putBoolean("logged_in", false).apply();
                UserFunctions logout = new UserFunctions();
                logout.logoutUser(getApplicationContext());
                Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(login);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(login);
            }
        });

        btnSwipeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent swipecard = new Intent(getApplicationContext(), SwipeCardActivity.class);
                startActivity(swipecard);
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settings = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(settings);
            }
        });

        btnRealtor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent realtor = new Intent(getApplicationContext(), RealtorListActivity.class);
                startActivity(realtor);
            }
        });

        btnDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent database = new Intent(getApplicationContext(), DatabaseActivity.class);
                startActivity(database);
            }
        });
    }

    @Override
    protected void onStop(){
        super.onStop();
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, 0);
        prefs.edit().putBoolean("logged_in", false).apply();
    }

}
