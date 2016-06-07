package io.propty.propty;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * Details Fragment opens in the SwipeCard Activity
 * and uses a ListView to display data members of
 * a SwipeCard object. It also has a button that
 * opens a Google Maps Fragment to show the location
 * of the current SwipeCard
 */
public class DetailsFragment extends Fragment {

    private GoogleMap mGoogleMap;
    ArrayList<String> details;
    int card_num;

    public DetailsFragment() {
        // Required empty public constructor
    }

    //TODO: WHEN GOING BACK TO SWIPECARD FRAGMENT, KEEP SAME CARD ON SCREEN. CURRENTLY RESETS
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View mView = inflater.inflate(R.layout.fragment_details, container, false);

        card_num = getArguments().getInt("CARD_NUM");


        //create new ArrayList and add data from SwipeCard from Bundle arguments
        details = new ArrayList<>();
        details.add("Name: " + getArguments().getString("DESC"));
        details.add("Type: " + getArguments().getString("TYPE"));
        details.add("Bedrooms: " + getArguments().getInt("BEDS"));
        details.add("Bathrooms: " + getArguments().getDouble("BATHS"));
        details.add(getArguments().getInt("AREA") + " square feet");
        details.add("Price: " + NumberFormat.getCurrencyInstance()
                .format(getArguments().getDouble("PRICE")));

        //create new Adapter and set to ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout
                .simple_list_item_1, details);
        ListView mListView = (ListView) mView.findViewById(R.id.details_listview);
        mListView.setAdapter(adapter);

        //create new Button and set new listener
        ImageButton mButton = (ImageButton) mView.findViewById(R.id.map_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            //open new Google Map Fragment when button is pressed
            @Override
            public void onClick(View v) {
                //get Fragment Manager
                FragmentManager fm = getActivity().getSupportFragmentManager();
                //create new SupportMap Fragment
                SupportMapFragment mapFragment = new SupportMapFragment();
                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        mGoogleMap = googleMap;

                        // Add a marker in Gainesville and move the camera
                        LatLng gainesville = new LatLng(29.6516, -82.3248);
                        mGoogleMap.addMarker(new MarkerOptions().position(gainesville).title("Marker in Gainesville"));
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(gainesville, 15));
                    }
                });
                //replace the current fragment with this one
                fm.beginTransaction().add(R.id.fragment_container, mapFragment)
                        .addToBackStack(null).commit();

            }
        });



        return mView;



    }

 /*   @Override
    public void onDestroyView() {
        super.onDestroyView();
        Bundle bundle = new Bundle();
        bundle.putInt("CARD", card_num);
        setArguments(bundle);
    }*/


}