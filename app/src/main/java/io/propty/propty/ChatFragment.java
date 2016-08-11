package io.propty.propty;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends DialogFragment {

    ArrayList<String> messages;

    String totalMessage;

    RecyclerView chatRecyclerView;
    LinearLayoutManager mLayoutManager;
    ChatAdapter mAdapter;

    EditText chatEdit;
    ImageButton sendButton;

    String uid;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        //Load Shared preferences to get user's ID
        //TODO LOAD UID FROM SHAREDPREFS (THIS IS FUNCTIONAL ON OTHER BRANCHES)
        SharedPreferences settings = this.getContext().getSharedPreferences(MainActivity.PREFS_NAME, 0);
        uid = "DUMMY USER ID";

        //Load the ChatMessageDatabaseHandler and populate the ArrayList with (10) recent ChatMessages
        int numLoadedMessages = 10;
        final ChatDatabaseHandler dbHandler = new ChatDatabaseHandler(view.getContext(), null, null, 1);
        ArrayList<ChatMessage> emptyList = new ArrayList<ChatMessage>();
        ArrayList<ChatMessage> ChatMessageList = dbHandler.loadRecentMessages(emptyList, numLoadedMessages);

        //Check for empty ChatMessageList and if so, add a welcome message.
        if(ChatMessageList.isEmpty()){
            ChatMessageList.add(new ChatMessage("x", "Welcome to the Chat Window!"));
        }

        //Extract the message text from the ChatMessage objects and put in a separate ArrayList.
        messages = new ArrayList<String>();
        for(int i = 0; i < ChatMessageList.size(); i++){
            messages.add(ChatMessageList.get(i).getMessage());
        }

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

                //check if chatEdit is empty or not
                if(TextUtils.isEmpty(chatEdit.getText())) {
                }
                //else get message to add to scrollview
                else {

                    //get message sent and add to total message
                    tempMessage = chatEdit.getText().toString();
                    totalMessage += "\n" + tempMessage;

                    dbHandler.addMessage(new ChatMessage(uid, tempMessage));

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
        setCancelable(true);
        setStyle(STYLE_NO_FRAME, 0);

        return view;
    }//END OF onCreateView()

}//END OF CHATFRAGMENT CLASS
