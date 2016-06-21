package io.propty.propty;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.siyamed.shapeimageview.CircularImageView;

import java.util.HashMap;
import java.util.List;

//TODO: IMPLEMENT IMAGES TO BE DISPLAYED WITH REALTOR NAMES

/*
This is a custom adapter for the Expandable ListView being used for
the RealtorList Activity. It uses an ArrayList for the realtor names,
and a HashMap for the realtor info/descriptions. An array of ratings
are also added to implement a rating bar in a child view of each
group, along with a button that picks the realtor currently expanded
*/
public class RealtorExpandableAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<String> mParent;
    private HashMap<String, String> mChild;
    private String[] mRatings;
    private int[] imageIds;

    private GroupViewHolder mGroupViewHolder;
    private ChildViewHolder mChildViewHolder;

    //CONSTRUCTOR
    public RealtorExpandableAdapter(Context context, List<String> list,
                                    HashMap<String, String> hashMap, String[] strArray, int[] images) {

        //set all arguments to member variables
        mContext = context;
        mParent = list;
        mChild = hashMap;
        mRatings = strArray;
        imageIds = images;

    }

    @Override
    public int getGroupCount() {

        return mParent.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        //Note: A hard code of 1 is used, because only
        //one "child" is used for each parent, a string.
        return 1;

    }

    @Override
    public Object getGroup(int groupPosition) {

        return mParent.get(groupPosition);

    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        return mChild.get(mParent.get(groupPosition));

    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        //get a string for the name of the realtor
        String realtor_name = (String) getGroup(groupPosition);
        int realtor_image = imageIds[groupPosition];

        //if the view is brand new
        if(convertView == null) {
            //create a new LayoutInflator, and set it to inflate the brand new view
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.realtor_parent, parent, false);
            //create a new ViewHolder for the group, and set it to the proper xml id
            mGroupViewHolder = new GroupViewHolder();
            mGroupViewHolder.mGroupText = (TextView) convertView.findViewById(R.id.realtor_parent_textview);
            mGroupViewHolder.mGroupImage = (CircularImageView) convertView.findViewById(R.id.realtor_circle_image);
            //give the view a tag for future use
            convertView.setTag(mGroupViewHolder);

        }
        else {
            //make the view from the previous tag used
            mGroupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        //set the TextView in the parent to the name of the realtor, and make it BOLD
        mGroupViewHolder.mGroupText.setTypeface(null, Typeface.BOLD);
        mGroupViewHolder.mGroupText.setText(realtor_name);
        mGroupViewHolder.mGroupImage.setImageResource(realtor_image);

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        //get a string of the info/description of the realtor
        String realtor_info = (String) getChild(groupPosition, childPosition);
        //get the string "value" of the rating of the current realtor
        String rating = mRatings[groupPosition];

        //if the view is brand new
        if(convertView == null) {
            //create a new LayoutInflator, and set it to inflate the new view
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.realtor_child, parent, false);
            //create a new ViewHolder for the child, and set it to the proper xml id
            mChildViewHolder = new ChildViewHolder();
            //set the TextView, Button, and RatingBar views from the child to the proper xml id's
            mChildViewHolder.mChildText = (TextView) convertView.findViewById(R.id.realtor_child_textview);
            mChildViewHolder.mChildButton = (Button) convertView.findViewById(R.id.realtor_child_button);
            mChildViewHolder.mRatingBar = (RatingBar) convertView.findViewById(R.id.realtor_child_ratingbar);
            //make the RatingBar NOT able to be changed by the user
            mChildViewHolder.mRatingBar.setIsIndicator(true);

            //give the view a tag for future use
            convertView.setTag(R.layout.realtor_child, mChildViewHolder);

        }
        else {
            //make a view from the previous tag used
            mChildViewHolder = (ChildViewHolder) convertView.getTag(R.layout.realtor_child);
        }
        //set the TextView to the string value of the realtor info/description
        mChildViewHolder.mChildText.setText(realtor_info);
        //set the rating bar value to the float version of the rating string
        mChildViewHolder.mRatingBar.setRating(Float.parseFloat(rating));

        //create a new listener for when the user clicks the "Pick Realtor" button
        mChildViewHolder.mChildButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //get the string of the current realtor name in the group the button belongs to
                String pickedRealtor = (String) getGroup(groupPosition);
                //get the TextView that displays the current realtor, in the RealtorList Activity
                TextView tv = (TextView) ((Activity)mContext).findViewById(R.id.picked_realtor);
                //set the textview to the color green, with the new value
                tv.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
                tv.setText("Current Realtor: " + pickedRealtor);
                //make a Toast for confirmation of the picked realtor
                Toast.makeText(mContext, pickedRealtor + " is now your realtor", Toast.LENGTH_SHORT).show();

            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    /*
    a ViewHolder for all View variables
    in the Parents of the Expandable ListView
    */
    // TODO: add an ImageView to be used
    public final class GroupViewHolder {

        TextView mGroupText;
        ImageView mGroupImage;
    }

    /*
    a ViewHOlder for all View variables
    in the Child of each Parent in the
    Expandable ListView
    */
    public final class ChildViewHolder {

        TextView mChildText;
        Button mChildButton;
        RatingBar mRatingBar;

    }


}
