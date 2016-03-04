package io.propty.propty;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import io.propty.propty.SwipeCard;
import java.util.ArrayList;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class SwipeCardActivity extends Activity {

    private ArrayList<SwipeCard> sc;
    private ArrayList<String> al;
    private ArrayAdapter<String> arrayAdapter;
    private int i;

    @InjectView(R.id.frame) SwipeFlingAdapterView flingContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipecard);
        ButterKnife.inject(this);

        sc = new ArrayList<>();
        // These are just examples for testing purposes.  Delete these before launch!
        sc.add(new SwipeCard("Property 1", 2, 1, 1, 2000, 960.00d, "condo"));
        sc.add(new SwipeCard("Property 2", 3, 2, 1, 3500, 2250.00d, "house"));
        sc.add(new SwipeCard("Property 3", 1, 1, 0, 1200, 625.00d, "apartment"));
        sc.add(new SwipeCard("Property 4", 1, 1, 1, 1500, 500.00d, "duplex"));
        sc.add(new SwipeCard("Property 5", 1, 0, 1, 150, 99.99d, "closet"));
        // TODO: populate sc array with MLS data

        al = new ArrayList<>();
        for (int idx = 0; idx < sc.size(); idx++) {
            SwipeCard card = sc.get(idx);
            al.add( card.getDesc() + "\n\n" +
                    card.getBeds() + " bedroom(s)\n" +
                    card.getBaths() + " bathroom(s)\n" +
                    card.getSqFt() + " sq. ft.\n" +
                    "$" + card.getPrice() + "\n" +
                    "Type: " + card.getType()       );
        }

        arrayAdapter = new ArrayAdapter<>(this, R.layout.item, R.id.cardText, al );


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
                // Ask for more data here
                al.add("XML ".concat(String.valueOf(i)));
                arrayAdapter.notifyDataSetChanged();
                Log.d("LIST", "notified");
                i++;
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

    static void makeToast(Context ctx, String s){
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }


    @OnClick(R.id.right)
    public void right() {
        /**
         * Trigger the right event manually.
         */
        flingContainer.getTopCardListener().selectRight();
    }

    @OnClick(R.id.left)
    public void left() {
        flingContainer.getTopCardListener().selectLeft();
    }




}
