package io.propty.propty;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
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
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Swipe Card Main Activity
 * Called when the user wants to see local property listings in a swipe-card format
 * Uses an ArrayList of SwipeCard objects to populate the list
 * Also
 */
public class SwipeCardActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private NavigationView left_drawer;
    private NavigationView right_drawer;
    private ActionBarDrawerToggle mDrawerToggle;


    private TextView mMinTextView;
    private TextView mMaxTextView;
    private RadioButton mCurrentZipButton;
    private RadioButton mOtherZipButton;

    private ArrayList<SwipeCard> sc;
    private ArrayList<String> al;
    private ArrayAdapter<String> arrayAdapter;

    int current_card = 0;
    int idx = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_preferences);

        //set up the Toolbar with Up Navigation
        Toolbar toolbar = (Toolbar) findViewById(R.id.swipecard_toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Find Your Home");

        //TODO: Change the setDrawerListener to the toggle, and take out
        //TODO: all of the stuff about the right drawer
        
        //Set up DrawerLayout with ActionBarToggle for navigation drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);


        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                //lock drawer when opened completely

                if (drawerView.equals(left_drawer)) {
                    actionBar.setTitle(getTitle());
                    supportInvalidateOptionsMenu();
                    mDrawerToggle.syncState();
                }
                else {

                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
                }

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                //unlock drawer when it is completely closed
                //in order to allow opening via swipe from right
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

                if(drawerView.equals(left_drawer)) {

                    getSupportActionBar().setTitle(getString(R.string.app_name));
                    supportInvalidateOptionsMenu();
                    mDrawerToggle.syncState();
                }

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        //listener created for when user taps the screen not filled
        //by the drawer when it is open
        mDrawerLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //closes the drawer if the user taps the area outside of it
                if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                }

                return false;
            }
        });

        //Set up the Bedroom Number Picker with minimum and maximum values
        NumberPicker mBedroomPicker = (NumberPicker) findViewById(R.id.num_bedroom);
        mBedroomPicker.setMaxValue(10);
        mBedroomPicker.setMinValue(1);
        mBedroomPicker.setWrapSelectorWheel(false);
        //Set up Bathroom number picker with minimum and maximum values
        NumberPicker mBathroomPicker = (NumberPicker) findViewById(R.id.num_bathroom);
        mBathroomPicker.setMaxValue(10);
        mBathroomPicker.setMinValue(1);
        mBathroomPicker.setWrapSelectorWheel(false);

        //set up adapter for drop down menu of home types
        Spinner typeSpinner = (Spinner) findViewById(R.id.type_drop);
        //Create an ArrayAdapter using the home_array
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this, R.array.home_array,
                android.R.layout.simple_spinner_item);
        //Specify layout and apply adapter
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);

        //set up adapter for drop down menu of square feet
        Spinner sqFtSpinner = (Spinner) findViewById(R.id.sq_ft_drop);
        //Create an ArrayAdapter using the home_array
        ArrayAdapter<CharSequence> sqFtAdapter = ArrayAdapter.createFromResource(this, R.array.sq_ft_array,
                android.R.layout.simple_spinner_item);
        //Specify layout and apply adapter
        sqFtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sqFtSpinner.setAdapter(sqFtAdapter);

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

        //Set up the EditTexts
        //TODO: Get variables of EditTexts!!!

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
                mDrawerLayout.closeDrawer(GravityCompat.END);
            }
        });

        //START FRAGMENT TRANSACTION
        SwipeCardFragment swipeCardFragment = new SwipeCardFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, swipeCardFragment).commit();

        //END OF ONCREATE METHOD
    }

    //close drawer when back button is pressed
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        }
        else if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
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

        if(mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        //user presses gear icon, opens drawer and displays Toast
        if (id == R.id.update_preferences_icon) {
            mDrawerLayout.openDrawer(GravityCompat.END);

            return true;
        }
        //user presses UP navigation arrow
        if (id == android.R.id.home) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            //if fragments have been added to the stack
            // go to previous fragment
            if(fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStack();
                return true;
            }

        }

        return super.onOptionsItemSelected(item);
    }

}