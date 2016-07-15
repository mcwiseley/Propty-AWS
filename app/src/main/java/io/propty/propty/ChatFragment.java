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
    NestedScrollView scrollView;
    LinearLayout chatWindow;
    LinearLayout.LayoutParams params;

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

        messages = new ArrayList<>();
        messages.add("Sample");

        //get the chatWindow LinearLayout
//        chatWindow = (LinearLayout) view.findViewById(R.id.chat_window);

        //get parameters of margin size for new texts added
        params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0,16,0,16);


        chatEdit = (EditText) view.findViewById(R.id.chat_edit);

        sendButton = (ImageButton) view.findViewById(R.id.chat_send_button);

//SET UP RECYCLERVIEW!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        chatRecyclerView = (RecyclerView) view.findViewById(R.id.chat_recycler_view);

        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setStackFromEnd(true);
        chatRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ChatAdapter(messages);
        chatRecyclerView.setAdapter(mAdapter);


        chatRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //TODO: Create Send image alongside EditText!!!
        //set listener for send button
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tempMessage;

                //check if chatEdit is empty or not
                if(TextUtils.isEmpty(chatEdit.getText())) {
                }
                //else get message to add to scrollview
                else {

                    //get message sent and add to total message
                    tempMessage = chatEdit.getText().toString();
                    totalMessage += "\n" + tempMessage;

/*                    //create new TextView for tempMessage
                    TextView tv = new TextView(getContext());
                    //set text to tempMessage and change text size
                    tv.setText(tempMessage);
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    //set to right side on bottom, with 16px margin
                    tv.setGravity(GravityCompat.END);
                    tv.setLayoutParams(params);
                    //add to LinearLayout within ScrollView
                    chatWindow.addView(tv);*/

                    mAdapter.addMessage(tempMessage);
                    chatRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
                }

                //reset chatEdit back to empty
                chatEdit.setText("");

            }
        });

//        setCancelable(false);

        return view;
    }

}
