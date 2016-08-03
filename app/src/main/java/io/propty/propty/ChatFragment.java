package io.propty.propty;


import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.text.TextUtilsCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends DialogFragment {

    ArrayList<String> messages;

    String totalMessage;

    //TODO: Get rid of remnants of ScrollView, clean up code in Fragments and Adapter, and get View to stay anchored on bottom when new texts are added!
    RecyclerView chatRecyclerView;
    LinearLayoutManager mLayoutManager;
    ChatAdapter mAdapter;

    EditText chatEdit;
    ImageButton sendButton;

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        //initialize ArrayList and add sample message in first element
        messages = new ArrayList<>();
        messages.add("Sample");

        //initialize EditText and ImageButton and sending messages
        chatEdit = (EditText) view.findViewById(R.id.chat_edit);
        sendButton = (ImageButton) view.findViewById(R.id.chat_send_button);

        //initialize RecyclerView and LayoutManager for the RecyclerView
        chatRecyclerView = (RecyclerView) view.findViewById(R.id.chat_recycler_view);
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setStackFromEnd(true);
        chatRecyclerView.setLayoutManager(mLayoutManager);

        //initialize and set ChatAdapter for RecyclerView
        mAdapter = new ChatAdapter(messages);
        chatRecyclerView.setAdapter(mAdapter);

        //set listener for send button
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //temporary String for message
                String tempMessage;

                //check if chatEdit is not empty
                if(!TextUtils.isEmpty(chatEdit.getText().toString().trim())) {

                    //get trimmed message sent and add to total message
                    tempMessage = chatEdit.getText().toString().trim();
                    //if tempMessage isn't empty after being trimmed
                    //if(!tempMessage.isEmpty()) {
                        totalMessage += "\n" + tempMessage;
                        //add message to ArrayList in ChatAdapter
                        mAdapter.addMessage(tempMessage);
                        chatRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
                    //}
                }

                //reset chatEdit back to empty
                chatEdit.setText("");

            }
        });

        return view;
    }

}
