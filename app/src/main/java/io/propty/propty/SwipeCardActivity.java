package io.propty.propty;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Swipe Card Main Activity
 * Called when the user wants to see local property listings in a swipe-card format
 * Uses an ArrayList of Property objects to populate the list
 * Also
 */
public class SwipeCardActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private NavigationView left_drawer;
    private NavigationView right_drawer;
    private ActionBarDrawerToggle mDrawerToggle;

    private FragmentManager mfragmentManager;

    private TextView mMinTextView;
    private TextView mMaxTextView;
    private RadioButton mCurrentZipButton;
    private RadioButton mOtherZipButton;
    private EditText mEditMiles;
    private EditText mEditZip;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_preferences);

        //set up the Toolbar with Up Navigation
        Toolbar toolbar = (Toolbar) findViewById(R.id.swipecard_toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Find Your Home");

        //Set up DrawerLayout with ActionBarToggle for navigation drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        left_drawer = (NavigationView) findViewById(R.id.nav_view_left);
        right_drawer = (NavigationView) findViewById(R.id.nav_view_right);


        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                if(drawerView.equals(right_drawer)) {

                }

                super.onDrawerSlide(drawerView, slideOffset);
            }

            @Override
            public void onDrawerOpened(View drawerView) {

                if (drawerView.equals(left_drawer)) {

                    invalidateOptionsMenu();
                    mDrawerToggle.syncState();
                    mDrawerLayout.closeDrawer(right_drawer);
                }

                if (drawerView.equals(right_drawer)) {

                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
                    mDrawerLayout.closeDrawer(left_drawer);
                }

                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {

                if(drawerView.equals(left_drawer)) {

//                    supportInvalidateOptionsMenu();
                    mDrawerToggle.syncState();
                }

                if(drawerView.equals(right_drawer)) {

                    invalidateOptionsMenu();
                    mDrawerToggle.syncState();

                    //unlock drawer when it is completely closed
                    //in order to allow opening via swipe from right
                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

                }

                super.onDrawerClosed(drawerView);
            }

        };

        //toolbar listener to still allow clicks on toggle/UP arrow
        //when drawer toggle has been disabled
        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        mDrawerLayout.setDrawerListener(mDrawerToggle);
        //listener created for when user taps the screen not filled
        //by the drawer when it is open
        mDrawerLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //closes the drawer if the user taps the area outside of it
                if (mDrawerLayout.isDrawerOpen(right_drawer)) {
                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                }

                return false;
            }
        });

        left_drawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
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
//                    prefs.edit().putBoolean("logged_in", false).apply();
                    UserFunctions logout = new UserFunctions();
                    logout.logoutUser(getApplicationContext());
                    Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                    login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(login);
                    //commented out finish() due to improper UP and
                    //BACK button navigation
//                finish();
                } else if (id == R.id.nav_swipecard) {

                    mDrawerLayout.closeDrawer(left_drawer);

                } else if (id == R.id.nav_settings) {
                    Intent settings = new Intent(getApplicationContext(), SettingsActivity.class);
                    startActivity(settings);
                } else if (id == R.id.nav_realtor_list) {
                    Intent realtor = new Intent(getApplicationContext(), RealtorListActivity.class);
                    startActivity(realtor);
                } else if (id == R.id.nav_database) {
                    Intent database = new Intent(getApplicationContext(), PropertyDatabaseActivity.class);
                    startActivity(database);
                } else if (id == R.id.nav_chat) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    ChatFragment chatFragment = new ChatFragment();
                    chatFragment.show(fragmentManager, "Test");
                }

                mDrawerLayout.closeDrawer(left_drawer);
                return true;
            }
        });

        //set up Fragment Manager
        mfragmentManager = getSupportFragmentManager();
        //create new listener for when Back Stack changes amounts
        mfragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                //if Swipecard fragment is the only one present
                if(mfragmentManager.getBackStackEntryCount() == 0) {
                    //replace UP arrow with drawer toggle
                    mDrawerToggle.setDrawerIndicatorEnabled(true);
                    actionBar.setDisplayHomeAsUpEnabled(false);
                    //unlock both drawers
                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                }
                //else multiple fragments are in the stack
                else {
                    //replace toggle with UP arrow
                    mDrawerToggle.setDrawerIndicatorEnabled(false);
                    actionBar.setDisplayHomeAsUpEnabled(true);
                    //lock both drawers
                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                }
                mDrawerToggle.syncState();
            }
        });

        //Load shared preferences to fill in settings values
        SharedPreferences settings = getSharedPreferences(SettingsActivity.PREFS, 0);

        //Set up the Bedroom Number Picker with minimum and maximum values
        NumberPicker mBedroomPicker = (NumberPicker) findViewById(R.id.num_bedroom);
        mBedroomPicker.setMaxValue(10);
        mBedroomPicker.setMinValue(1);
        mBedroomPicker.setWrapSelectorWheel(false);

        mBedroomPicker.setValue((int)settings.getFloat(SettingsActivity.numBedrooms_string, 1));

        // /Set up Bathroom number picker with minimum and maximum values
        NumberPicker mBathroomPicker = (NumberPicker) findViewById(R.id.num_bathroom);
        mBathroomPicker.setMaxValue(10);
        mBathroomPicker.setMinValue(1);
        mBathroomPicker.setWrapSelectorWheel(false);

        mBathroomPicker.setValue((int)settings.getFloat(SettingsActivity.numBathrooms_string, 1));

        //set up adapter for drop down menu of home types
        Spinner typeSpinner = (Spinner) findViewById(R.id.type_drop);
        //Create an ArrayAdapter using the home_array
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this, R.array.structure_array,
                android.R.layout.simple_spinner_item);
        //Specify layout and apply adapter
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);
        typeSpinner.setSelection(settings.getInt(SettingsActivity.structure_string, 0));

        //set up adapter for drop down menu of square feet
        Spinner sqFtSpinner = (Spinner) findViewById(R.id.sq_ft_drop);
        //Create an ArrayAdapter using the home_array
        ArrayAdapter<CharSequence> sqFtAdapter = ArrayAdapter.createFromResource(this, R.array.squareFootage_array,
                android.R.layout.simple_spinner_item);
        //Specify layout and apply adapter
        sqFtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sqFtSpinner.setAdapter(sqFtAdapter);
        sqFtSpinner.setSelection(settings.getInt(SettingsActivity.squareFootage_string, 0));

        //initialize the radio buttons
        mCurrentZipButton = (RadioButton) findViewById(R.id.current_zip_radio);
        mOtherZipButton = (RadioButton) findViewById(R.id.other_zip_radio);

        //create custom listener for when radio buttons are clicked
        mCurrentZipButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    //get id of checked button
                    int id = buttonView.getId();

                    //uncheck other buttons
                    mOtherZipButton.setChecked(false);
                }
            }
        });
        //custom listener for other radio button
        mOtherZipButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    int id = buttonView.getId();

                    //uncheck other radio buttons
                    mCurrentZipButton.setChecked(false);
                }
            }
        });
        mOtherZipButton.setChecked(settings.getBoolean(SettingsActivity.radioZip_string, true));
        mCurrentZipButton.setChecked(settings.getBoolean(SettingsActivity.radioCurrent_string, false));

        //Set up the EditTexts
        mEditMiles = (EditText) findViewById(R.id.edit_miles);
        mEditMiles.setText(Integer.toString(settings.getInt(SettingsActivity.within_string, 0)));
        mEditZip = (EditText) findViewById(R.id.edit_zip);
        mEditZip.setText(Integer.toString(settings.getInt(SettingsActivity.zip_string, 0)));

        //Set up the price slider and TextView for Minimum Price
        mMinTextView = (TextView) findViewById(R.id.min_price);
        SeekBar mMinSeekBar = (SeekBar) findViewById(R.id.min_seekbar);
        mMinSeekBar.setMax(10);
        //listener for price slider
        mMinSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int mProgress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mProgress = progress;
                //change display of TextView when user presses slider
                mMinTextView.setText("Min Price: $" + (mProgress * 1000));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mMinSeekBar.setProgress(settings.getInt(SettingsActivity.minPrice_string, 0) / 1000);
        mMinTextView.setText("Min Price: $" + settings.getInt(SettingsActivity.minPrice_string, 0));


        //Set up price slider and TextView for Maximum Price
        mMaxTextView = (TextView) findViewById(R.id.max_price);
        SeekBar mMaxSeekBar = (SeekBar) findViewById(R.id.max_seekbar);
        mMaxSeekBar.setMax(10);
        //listener for price slider
        mMaxSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int mProgress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                mProgress = progress;
                //change display of TextView when user presses slider
                mMaxTextView.setText("Max Price: $" + (mProgress * 100000));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mMaxSeekBar.setProgress(settings.getInt(SettingsActivity.maxPrice_string, 0) / 100000);
        mMaxTextView.setText("Max Price: $" + settings.getInt(SettingsActivity.maxPrice_string, 0));

        //Set up the Update button and apply listener to close drawer
        Button mUpdateButton = (Button) findViewById(R.id.update_button);
        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send variables to proper areas!!!
                //TODO: get all variables and apply back to swipe card activity
                Toast.makeText(getApplicationContext(), "Preferences have been updated",
                        Toast.LENGTH_SHORT).show();

                //close the drawer
                mDrawerLayout.closeDrawer(right_drawer);
            }
        });

        //START FRAGMENT TRANSACTION TO ADD SWIPECARD FRAGMENT
        SwipeCardFragment swipeCardFragment = new SwipeCardFragment();
        FragmentTransaction fragmentTransaction = mfragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, swipeCardFragment).commit();

        //END OF ONCREATE METHOD
    }

    //close drawers when back button is pressed
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(right_drawer)) {
            drawer.closeDrawer(right_drawer);
        }
        else if(drawer.isDrawerOpen(left_drawer)) {
            drawer.closeDrawer(left_drawer);
        }
        else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    //create icons and actions in toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_preferences, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //user presses gear icon, opens righ drawer
        if (id == R.id.update_preferences_icon) {

            mDrawerLayout.openDrawer(right_drawer);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

} // END OF SWIPECARD ACTIVITY CLASS