package io.propty.propty;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


//TODO: GET IMAGES FOR THE REALTORS AND SEND TO THE ADAPTER

public class RealtorListActivity extends AppCompatActivity {

    //request code for RealtorActivity
    final static int REALTOR_PICK = 1;

    String picked_realtor;
    private boolean isPicked;
    private TextView mTextView;

    //Variables to set up Expandable ListView
    ExpandableListView mExpandableListView;
    RealtorExpandableAdapter mRealtorAdapter;
    String[] realtorRatings;
    List<String> realtorNames;
    HashMap<String, String> realtorInfo;
    int[] realtorImages = {R.drawable.real1, R.drawable.real2, R.drawable.real3, R.drawable.real4};

    //int value that holds position of last group expanded by the user
    int previousPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtor_list);

        //////////////////////////////////////////////////////////////////////
        //All OF THE NEW EXPANDABLE LISTVIEW STUFF IS HERE!!!
        /////////////////////////////////////////////////////////////////////
        mExpandableListView = (ExpandableListView) findViewById(R.id.realtor_exp_listview);

        //calling function to get all of the data from xml files and other resources
        prepareListData();

        //call custom adapter and set to Expandable ListView
        mRealtorAdapter = new RealtorExpandableAdapter(this, realtorNames, realtorInfo, realtorRatings, realtorImages);
        mExpandableListView.setAdapter(mRealtorAdapter);
        mExpandableListView.setFooterDividersEnabled(true);
        mExpandableListView.addFooterView(new View(mExpandableListView.getContext()));

        //new event listener that collapses other groups when a new one expands
        mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {

                if(groupPosition != previousPosition) {
                    mExpandableListView.collapseGroup(previousPosition);
                }

                previousPosition = groupPosition;
            }
        });


    }

    //Prepare List Data function
    //adds all data from xml files and other resources
    //before calling the needed adapter for the Expandable ListView
    public void prepareListData() {

        realtorNames = new ArrayList<>();
        realtorInfo = new HashMap<>();

        //get realtor names from strings.xml and set to ArrayList
        String[] realtors = getResources().getStringArray(R.array.realtors);
        for(int count = 0; count < realtors.length; count++) {

            realtorNames.add(realtors[count]);
        }

        //get realtor info from strings.xml and set to HashMap
        String[] realtorDescriptions = getResources().getStringArray(R.array.realtor_info);
        for(int count = 0; count < realtorDescriptions.length; count++) {

            realtorInfo.put(realtorNames.get(count), realtorDescriptions[count]);
        }

        //get made up ratings from strings.xml for rating bars
        realtorRatings = getResources().getStringArray(R.array.realtor_ratings);

    }

}
