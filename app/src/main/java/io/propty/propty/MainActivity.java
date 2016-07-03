package io.propty.propty;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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
        setContentView(R.layout.drawer_main);

        //set up the Toolbar with Up Navigation
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.app_name);

        //Set up DrawerLayout with ActionBarToggle for navigation drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

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

        //Set up NavigationView with listener for clicking items in drawer
        NavigationView navigationView = (NavigationView) findViewById(R.id.main_nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                // Handle navigation view item clicks here.
                int id = item.getItemId();

                if (id == R.id.nav_login) {
                    // Handle the camera action
                    Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(login);
                } else if (id == R.id.nav_change_password) {
                    Intent chgPass = new Intent(getApplicationContext(), ChangePasswordActivity.class);
                    startActivity(chgPass);
                } else if (id == R.id.nav_logout) {
                    prefs.edit().putBoolean("logged_in", false).apply();
                    UserFunctions logout = new UserFunctions();
                    logout.logoutUser(getApplicationContext());
                    Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                    login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(login);
                } else if (id == R.id.nav_swipecard) {
                    Intent swipecard = new Intent(getApplicationContext(), SwipeCardActivity.class);
                    startActivity(swipecard);
                } else if (id == R.id.nav_settings) {
                    Intent settings = new Intent(getApplicationContext(), SettingsActivity.class);
                    startActivity(settings);
                } else if (id == R.id.nav_realtor_list) {
                    Intent realtor = new Intent(getApplicationContext(), RealtorListActivity.class);
                    startActivity(realtor);
                } else if (id == R.id.nav_database) {
                    Intent database = new Intent(getApplicationContext(), DatabaseActivity.class);
                    startActivity(database);
                } else if (id == R.id.nav_chat) {

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    ChatFragment chatFragment = new ChatFragment();
                    chatFragment.show(fragmentManager, "Test");
                }



                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        btnDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent database = new Intent(getApplicationContext(), DatabaseActivity.class);
                startActivity(database);
            }
        });

    } //END OF ONCREATE()

    @Override
    protected void onStop(){
        super.onStop();
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, 0);
        prefs.edit().putBoolean("logged_in", false).apply();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


}
