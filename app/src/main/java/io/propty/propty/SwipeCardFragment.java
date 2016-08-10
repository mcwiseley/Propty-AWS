package io.propty.propty;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class SwipeCardFragment extends Fragment {

    View mView;
    ArrayList<Property> sc;
    ArrayList<String> al;
    ArrayAdapter<String> arrayAdapter;
    Toolbar toolbar;

    static final int MAX_CARDS_IN_STACK = 20;
    static final int MIN_CARDS_IN_STACK = 6;
    static final int MAX_RENDERED_CARDS = 4;
    int current_card;
    int idx = 1;
    public static final String PREFS = "MyPrefsFile";

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

        SharedPreferences settings = getContext().getSharedPreferences(PREFS, 0);
        PropertyDatabaseHandler dbHandler = new PropertyDatabaseHandler(getContext(), null, null, 1);
        ArrayList<Property> newList = new ArrayList<Property>();
        newList = dbHandler.listProperties(getContext(), newList, 5);

        if(newList.size() != 0)
            sc.add(newList.get(0));
        if(newList.size() > 1)
            sc.add(newList.get(1));
        if(newList.size() > 2)
            sc.add(newList.get(2));
        if(newList.size() > 3)
            sc.add(newList.get(3));
        if(newList.size() > 4)
            sc.add(newList.get(4));
        if(newList.isEmpty()){
            sc.add(new Property("No matching properties", "", 2, 2, 0, 0, 0, 2000, "sq ft", 960.00d, "condo", "", 1));
        }
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
                //create Property object from current card in array
                Property card = sc.get(current_card);
                //create new Bundle and set arguments on
                //Property object members
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
     * Parses the provided S3 bucket object data and generates a single Property object
     * @param objectData An InputStream object pulled from the S3 bucket
     * @return Returns a new Property object containing the data parsed from objectData
     * @throws IOException
     */
    protected Property generateCardFromData(InputStream objectData) throws IOException {
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

        return new Property(desc, listId, beds, bathsFull, baths3q, bathsHalf, baths1q,
                area, areaUnits, price, type, subType, img);
    }

    /**
     * Converts SwipeCards from the ArrayList of SwipeCards (sc) into formatted Strings, then
     * adds the specified number of converted SwipeCards to the ArrayList of formatted Strings (al).
     * This allows the activity to properly display the formatted Property data.
     *
     * @param number_to_add The number of SwipeCards to add to the stack
     */
    protected void updateCardArray(int number_to_add) {
        for (int i = 0; i < number_to_add; i++) {
            Property card = sc.get(i % sc.size());
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