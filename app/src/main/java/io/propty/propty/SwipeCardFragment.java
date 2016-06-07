package io.propty.propty;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import java.io.*;
import java.util.ArrayList;


public class SwipeCardFragment extends Fragment {

    View mView;
    ArrayList<SwipeCard> sc;
    ArrayList<String> al;
    ArrayAdapter<String> arrayAdapter;
    Toolbar toolbar;

    static final int MAX_CARDS_IN_STACK = 20;
    static final int MIN_CARDS_IN_STACK = 6;
    static final int MAX_RENDERED_CARDS = 4;
    int current_card;
    int idx = 1;

    @InjectView(R.id.frame) SwipeFlingAdapterView flingContainer;

    public SwipeCardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        current_card = 0;
        sc = new ArrayList<>();
        al = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.swipecard, R.id.cardText, al);

        // These are just examples for testing purposes.  TODO: Delete these before launch!
        sc.add(new SwipeCard("Property 1", "", 2, 2, 0, 0, 0, 2000, "sq ft", 960.00d, "condo", "", 1));
        sc.add(new SwipeCard("Property 2", "", 3, 2, 0, 1, 0, 3500, "sq ft", 2250.00d, "house", "", 2));
        sc.add(new SwipeCard("Property 3", "", 1, 1, 0, 0, 0, 1200, "sq ft", 625.50d, "apartment", "", 3));
        sc.add(new SwipeCard("Property 4", "", 1, 1, 0, 1, 0, 1500, "sq ft", 500.01d, "duplex", "", 4));
        sc.add(new SwipeCard("Property 5", "", 1, 0, 0, 1, 0, 150, "sq ft", 99.99d, "closet", "", 5));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_swipe_card, container, false);
        ButterKnife.inject(this, mView);

        //set the adapter to the flingContainer
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
                makeToast(getActivity(), "Dislike!");
            }

            @Override
            public void onRightCardExit(Object dataObject) {

                makeToast(getActivity(), "Like!");
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

//              makeToast(getActivity(), "Clicked!");
                //create SwipeCard object from current card in array
                SwipeCard card = sc.get(current_card);
                //create new Bundle and set arguments on
                //SwipeCard object members
                Bundle bundle = new Bundle();
                bundle.putInt("CARD_NUM", current_card);
                bundle.putString("DESC", card.getDesc());
                bundle.putInt("BEDS", card.getBeds());
                bundle.putDouble("BATHS", card.getBaths());
                bundle.putInt("AREA", card.getArea());
                bundle.putDouble("PRICE", card.getPrice());
                bundle.putString("TYPE", card.getType());
                //create DetailsFragment object and set the Bundle arguments to it
                DetailsFragment detailsFragment = new DetailsFragment();
                detailsFragment.setArguments(bundle);
                //replace current fragment with DetailsFragment
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.enableDebugLogging(true);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, detailsFragment);
                fragmentTransaction.addToBackStack(null).commit();

            }
        });

        updateCardArray(sc.size() * 4);

        return mView;
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

}