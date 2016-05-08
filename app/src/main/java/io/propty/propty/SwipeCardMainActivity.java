package io.propty.propty;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
//import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
//import com.amazonaws.auth.CognitoCachingCredentialsProvider;
//import com.amazonaws.regions.Region;
//import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.io.*;
import java.util.ArrayList;

/**
 * Swipe Card Activity
 * Called when the user wants to see local property listings in a swipe-card format
 * Uses an ArrayList of SwipeCard objects to populate the list
 */
public class SwipeCardMainActivity extends AppCompatActivity {

    //private variables that Ryan added for DrawerLayout
    private DrawerLayout mDrawerLayout;
    private TextView mMinTextView;
    private TextView mMaxTextView;
    private RadioButton mCurrentZipButton;
    private RadioButton mOtherZipButton;
    private View mSwipeView;
    //end of Ryan's variables

    private ArrayList<SwipeCard> sc;
    private ArrayList<String> al;
    private ArrayAdapter<String> arrayAdapter;
    private Toolbar toolbar;

    private static final int MAX_CARDS_IN_STACK = 20;
    private static final int MIN_CARDS_IN_STACK = 6;
    private static final int MAX_RENDERED_CARDS = 4;
    private int current_card = 0;
    private int idx = 1;
//    private final String bucketName = "propty.mobilepull";
//    private String key = "samples/sample";

    @InjectView(R.id.frame) SwipeFlingAdapterView flingContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipecard_main);
        ButterKnife.inject(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.proptydog2);
        toolbar.setLogoDescription("logo");

        sc = new ArrayList<>();
        al = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, R.layout.swipecard, R.id.cardText, al);

        // Initialize the Amazon Cognito credentials provider
//        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
//                getApplicationContext(),
//                "us-east-1:bdd94561-cf5e-4693-995e-f4039ad7e234", // Identity Pool ID
//                Regions.US_EAST_1 // Region
//        );

        // Create an S3 client
//        final AmazonS3 s3 = new AmazonS3Client(credentialsProvider);

        // Set the region of your S3 bucket
//        s3.setRegion(Region.getRegion(Regions.US_EAST_1));

        // Populate Swipe Cards from the S3 bucket
//        pullDataFromBucket(s3, bucketName, key);

        // These are just examples for testing purposes.  TODO: Delete these before launch!
        sc.add(new SwipeCard("Property 1", "", 2, 2, 0, 0, 0, 2000, "sq ft", 960.00d, "condo", "", 1));
        sc.add(new SwipeCard("Property 2", "", 3, 2, 0, 1, 0, 3500, "sq ft", 2250.00d, "house", "", 2));
        sc.add(new SwipeCard("Property 3", "", 1, 1, 0, 0, 0, 1200, "sq ft", 625.50d, "apartment", "", 3));
        sc.add(new SwipeCard("Property 4", "", 1, 1, 0, 1, 0, 1500, "sq ft", 500.01d, "duplex", "", 4));
        sc.add(new SwipeCard("Property 5", "", 1, 0, 0, 1, 0, 150, "sq ft", 99.99d, "closet", "", 5));

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setMaxVisible(MAX_RENDERED_CARDS);
        flingContainer.setMinStackInAdapter(MIN_CARDS_IN_STACK);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                al.remove(0);
                current_card = (current_card + 1) % sc.size();
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                makeToast(getApplicationContext(), "Dislike!");
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                makeToast(getApplicationContext(), "Like!");
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
//                pullDataFromBucket(s3, bucketName, key);
                updateCardArray(sc.size() * 4);
                Log.d("LIST", "notified");
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });

        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                makeToast(getApplicationContext(), "Clicked!");
            }
        });

        updateCardArray(sc.size() * 4);

        //////////////////////////////////////////////////////////
        //Ryan's section of implementing DrawerLayout
        //and preferences menu
        /////////////////////////////////////////////////////////
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        mDrawerLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    mDrawerLayout.closeDrawer(GravityCompat.END);
                }

                return false;
            }
        });



        //Set up the Number Pickers
        NumberPicker mBedroomPicker = (NumberPicker) findViewById(R.id.num_bedroom);
        mBedroomPicker.setMaxValue(10);
        mBedroomPicker.setMinValue(1);
        mBedroomPicker.setWrapSelectorWheel(false);

        NumberPicker mBathroomPicker = (NumberPicker) findViewById(R.id.num_bathroom);
        mBathroomPicker.setMaxValue(10);
        mBathroomPicker.setMinValue(1);
        mBathroomPicker.setWrapSelectorWheel(false);

        //set up adapter for drop down menu of home types
        Spinner typeSpinner = (Spinner) findViewById(R.id.type_drop);
        //Create an ArrayAdapter using the home_array
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this, R.array.home_array,
                android.R.layout.simple_spinner_item);
        //Specify layout
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //apply typeAdapter
        typeSpinner.setAdapter(typeAdapter);

        //set up adapter for drop down menu of square feet
        Spinner sqFtSpinner = (Spinner) findViewById(R.id.sq_ft_drop);
        //Create an ArrayAdapter using the home_array
        ArrayAdapter<CharSequence> sqFtAdapter = ArrayAdapter.createFromResource(this, R.array.sq_ft_array,
                android.R.layout.simple_spinner_item);
        //Specify layout
        sqFtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //apply sqFtAdapter
        sqFtSpinner.setAdapter(sqFtAdapter);

        //Set up the radio buttons
        mCurrentZipButton = (RadioButton) findViewById(R.id.current_zip_radio);
        mOtherZipButton = (RadioButton) findViewById(R.id.other_zip_radio);

        mCurrentZipButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked) {
                    //get id of checked button
                    int id = buttonView.getId();

                    //uncheck other buttons
                    mOtherZipButton.setChecked(false);
                }
            }
        });

        mOtherZipButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked) {
                    int id = buttonView.getId();

                    //uncheck other radio buttons
                    mCurrentZipButton.setChecked(false);
                }
            }
        });

        //Set up the EditTexts

        //Set up the price sliders
        mMinTextView = (TextView) findViewById(R.id.min_price);
        SeekBar mMinSeekBar = (SeekBar) findViewById(R.id.min_seekbar);
        mMinSeekBar.setMax(10);
        mMinSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int mProgress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mProgress = progress;
                mMinTextView.setText("Min Price: $" + (mProgress * 1000));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        mMaxTextView = (TextView) findViewById(R.id.max_price);
        SeekBar mMaxSeekBar = (SeekBar) findViewById(R.id.max_seekbar);
        mMaxSeekBar.setMax(10);
        mMaxSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int mProgress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                mProgress = progress;
                mMaxTextView.setText("Max Price: $" + (mProgress * 100000));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Set up the Update button
        Button mUpdateButton = (Button) findViewById(R.id.update_button);
        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send variables to proper areas!!!



                //close the drawer
                mDrawerLayout.closeDrawer(GravityCompat.END);
//                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }
        });






    }

    static void makeToast(Context ctx, String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }

    /**
     * Trigger the right event manually.
     */
    @OnClick(R.id.right)
    public void right() { flingContainer.getTopCardListener().selectRight(); }

    /**
     * Trigger the left event manually.
     */
    @OnClick(R.id.left)
    public void left() { flingContainer.getTopCardListener().selectLeft(); }

    /**
     * Pull data objects from the Amazon S3 bucket until the card stack is full
     * Calls generateCardFromData() and updateCardArray()
     * @param s3 An AmazonS3Client object
     * @param bucketName The name of the S3 bucket
     * @param key The object key
     */
    protected void pullDataFromBucket(AmazonS3 s3, String bucketName, String key) {
        String key_0;
        int cardsInStack = sc.size();
        while (cardsInStack < MAX_CARDS_IN_STACK) {
            if (idx < 10) {
                key_0 = key + "0" + Integer.toString(idx) + ".txt";
            }
            else {
                key_0 = key + Integer.toString(idx) + ".txt";
            }
            idx++;
            try {
                S3Object object = s3.getObject(new GetObjectRequest(bucketName, key_0));
                InputStream objectData = object.getObjectContent();
                try {
                    sc.add(generateCardFromData(objectData));
                } finally {
                    try {
                        objectData.close();
                    } catch (Throwable ignore) {}
                }
            } catch (IOException e) { e.printStackTrace(); }
            cardsInStack = sc.size();
        }
        updateCardArray(sc.size());
    }

    /**
     * Parses the provided S3 bucket object data and generates a single SwipeCard object
     * @param objectData An InputStream object pulled from the S3 bucket
     * @return Returns a new SwipeCard object containing the data parsed from objectData
     * @throws IOException
     */
    protected SwipeCard generateCardFromData(InputStream objectData) throws IOException {
        // Default to null values
        String desc = "NULL";
        String listId = "NULL";
        String areaUnits = "NULL";
        String type = "NULL";
        String subType = "NULL";
        double price = -1d;
        int beds = -1;
        int bathsFull = -1;
        int baths3q = -1;
        int bathsHalf = -1;
        int baths1q = -1;
        int area = -1;

        BufferedReader reader = new BufferedReader(new InputStreamReader(objectData));
        String str;

        for (int i = 0; i < 12; i++) {
            if ((str = reader.readLine()) != null) {
                switch (i) {
                    case 0:
                        desc = str;
                        break;
                    case 1:
                        listId = str;
                        break;
                    case 2:
                        beds = Integer.parseInt(str);
                        break;
                    case 3:
                        bathsFull = Integer.parseInt(str);
                        break;
                    case 4:
                        baths3q = Integer.parseInt(str);
                        break;
                    case 5:
                        bathsHalf = Integer.parseInt(str);
                        break;
                    case 6:
                        baths1q = Integer.parseInt(str);
                        break;
                    case 7:
                        area = Integer.parseInt(str);
                        break;
                    case 8:
                        areaUnits = str;
                        break;
                    case 9:
                        if (str.charAt(0) == '$')
                            str = str.substring(1);
                        price = Double.parseDouble(str);
                        break;
                    case 10:
                        type = str;
                        break;
                    case 11:
                        subType = str;
                        break;
                }
            }
            else {
                throw new IOException("Error occurred while retrieving data from server");
            }
        }

        int img = 0;

        return new SwipeCard(desc, listId, beds, bathsFull, baths3q, bathsHalf, baths1q,
                area, areaUnits, price, type, subType, img);
    }

    /**
     * Converts SwipeCards from the ArrayList of SwipeCards (sc) into formatted Strings, then
     * adds the specified number of converted SwipeCards to the ArrayList of formatted Strings (al).
     * This allows the activity to properly display the formatted SwipeCard data.
     *
     * @param number_to_add The number of SwipeCards to add to the stack
     */
    protected void updateCardArray(int number_to_add) {
        for (int i = 0; i < number_to_add; i++) {
            SwipeCard card = sc.get(i % sc.size());
            al.add(card.getDescFormatted() +
                    card.getBedsFormatted() +
                    card.getBathsFormatted() +
                    card.getAreaFormatted() +
                    card.getPriceFormatted() +
                    card.getTypeFormatted());
        }
        arrayAdapter.notifyDataSetChanged();
    }

    ////////////////////////////////////////////////////
    //Ryan's section overridden methods
    //specifically for the drawer and action bar icon
    ///////////////////////////////////////////////////

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if(drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
//            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
        else {
            super.onBackPressed();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.update_preferences_icon) {
            Toast.makeText(this, "Update Preferences", Toast.LENGTH_SHORT).show();
            mDrawerLayout.openDrawer(GravityCompat.END);
//            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);




            return true;
        }

        return super.onOptionsItemSelected(item);
    }





//    protected void renderCardBackground() {
//        if (flingContainer.getChildAt(0) == null) return;
//        View view = flingContainer.getChildAt(0).findViewById(R.id.cardText);
//        switch (current_card) {
//            case 0:
//                view.setBackgroundResource(R.drawable.house_0);
//                break;
//            case 1:
//                view.setBackgroundResource(R.drawable.house_1);
//                break;
//            case 2:
//                view.setBackgroundResource(R.drawable.house_2);
//                break;
//            case 3:
//                view.setBackgroundResource(R.drawable.house_3);
//                break;
//            case 4:
//                view.setBackgroundResource(R.drawable.house_4);
//                break;
//            default:
//                break;
//        }
//    }

//    private class downloadFromBucket extends AsyncTask<S3Object, Void, Void> {
//        protected S3Object doInBackground(AmazonS3 s3, String key_0) {
//            return s3.getObject(new GetObjectRequest(bucketName, key_0));
//        }
//    }
}
