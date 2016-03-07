package io.propty.propty;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import java.io.*;
import java.util.ArrayList;

/**
 * Swipe Card Activity
 * Called when the user wants to see local property listings in a swipe-card format
 * Uses an ArrayList of SwipeCard objects to populate the list
 **/
public class SwipeCardActivity extends Activity {

    private ArrayList<SwipeCard> sc;
    private ArrayList<String> al;
    private ArrayAdapter<String> arrayAdapter;

    private final int MAX_CARDS_IN_STACK = 20;
    private String bucketName = "";
    private String key = "";
    // TODO: Provide values for bucketName and key

    @InjectView(R.id.frame) SwipeFlingAdapterView flingContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipecard);
        ButterKnife.inject(this);

        sc = new ArrayList<>();
        al = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, R.layout.item, R.id.cardText, al);

        // TODO: Update this block with our AWS credentials:
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),    /* get the context for the application */
                "COGNITO_IDENTITY_POOL",    /* Identity Pool ID */
                Regions.US_EAST_1           /* Region for your identity pool--US_EAST_1 or EU_WEST_1*/
        );

        // Create an S3 client
        final AmazonS3 s3 = new AmazonS3Client(credentialsProvider);

        // Set the region of your S3 bucket
        s3.setRegion(Region.getRegion(Regions.US_EAST_1));

        // Populate Swipe Cards from the S3 bucket
        pullDataFromBucket(s3, bucketName, key);

//        // These are just examples for testing purposes.  Delete these before launch!
//        sc.add(new SwipeCard("Property 1", 2, 2, 0, 2000, 960.00d, "condo"));
//        sc.add(new SwipeCard("Property 2", 3, 2, 1, 3500, 2250.00d, "house"));
//        sc.add(new SwipeCard("Property 3", 1, 1, 0, 1200, 625.50d, "apartment"));
//        sc.add(new SwipeCard("Property 4", 1, 1, 1, 1500, 500.01d, "duplex"));
//        sc.add(new SwipeCard("Property 5", 1, 0, 1, 150, 99.99d, "closet"));

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                al.remove(0);
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
                pullDataFromBucket(s3, bucketName, key);
                arrayAdapter.notifyDataSetChanged();
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
     **/
    protected void pullDataFromBucket(AmazonS3 s3, String bucketName, String key) {
        int cardsInStack = sc.size();
        while (cardsInStack < MAX_CARDS_IN_STACK) {
            try {
                S3Object object = s3.getObject(new GetObjectRequest(bucketName, key));
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
        updateCardArray();
    }

    /**
     * Parses the provided S3 bucket object data and generates a single SwipeCard object
     * @param objectData An InputStream object pulled from the S3 bucket
     * @return Returns a new SwipeCard object containing the data parsed from objectData
     * @throws IOException
     **/
    protected SwipeCard generateCardFromData(InputStream objectData) throws IOException {
        // Default to null values
        String desc = "NULL";
        String type = "NULL";
        double price = -1d;
        int beds = -1;
        int bathsFull = -1;
        int bathsHalf = -1;
        int sqFt = -1;

        BufferedReader reader = new BufferedReader(new InputStreamReader(objectData));
        String str;

        for (int i = 0; i < 7; i++) {
            if ((str = reader.readLine()) != null) {
                switch (i) {
                    case 0:
                        desc = str;
                        break;
                    case 1:
                        beds = Integer.parseInt(str);
                        break;
                    case 2:
                        bathsFull = Integer.parseInt(str);
                        break;
                    case 3:
                        bathsHalf = Integer.parseInt(str);
                        break;
                    case 4:
                        sqFt = Integer.parseInt(str);
                        break;
                    case 5:
                        price = Double.parseDouble(str);
                        break;
                    case 6:
                        type = str;
                        break;
                }
            }
            else {
                throw new IOException("Error occurred while retrieving data from server");
            }
        }
        return new SwipeCard(desc, beds, bathsFull, bathsHalf, sqFt, price, type);
    }

    /**
     * Converts the ArrayList of SwipeCard objects (sc) into an ArrayList of Strings (al)
     * This allows the activity to properly display the formatted SwipeCard data
     **/
    protected void updateCardArray() {
        ArrayList<String> new_al = new ArrayList<>();
        for (int i = 0; i < sc.size(); i++) {
            SwipeCard card = sc.get(i);
            new_al.add( card.getDescFormatted() +
                    card.getBedsFormatted() +
                    card.getBathsFormatted() +
                    card.getSqFtFormatted() +
                    card.getPriceFormatted() +
                    card.getTypeFormatted()    );
        }
        al = new_al;
        arrayAdapter.notifyDataSetChanged();
    }
}
