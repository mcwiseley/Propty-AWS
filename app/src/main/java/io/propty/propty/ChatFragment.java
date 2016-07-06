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
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends DialogFragment {

    String totalMessage;

    NestedScrollView scrollView;
    LinearLayout chatWindow;
    LinearLayout.LayoutParams params;

    EditText chatEdit;
    Button cancelButton;
    Button sendButton;

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        //get the chatWindow LinearLayout
        chatWindow = (LinearLayout) view.findViewById(R.id.chat_window);

        //get parameters of margin size for new texts added
        params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0,16,0,16);


        chatEdit = (EditText) view.findViewById(R.id.chat_edit);

        cancelButton = (Button) view.findViewById(R.id.chat_cancel_button);
        sendButton = (Button) view.findViewById(R.id.chat_send_button);

        //set listener for cancel button
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //reset EditText to null and dismiss Fragment
                chatEdit.setText(null);
                dismiss();
            }
        });

        //TODO: Create Send image alongside EditText!!!
        //set listener for send button
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tempMessage;

                //check if chatEdit is empty or not
                if(TextUtils.isEmpty(chatEdit.getText())) {
                    Toast.makeText(getActivity(), "NULL", Toast.LENGTH_SHORT).show();
                }
                //else get message to add to scrollview
                else {

                    //get message sent and add to total message
                    tempMessage = chatEdit.getText().toString();
                    totalMessage += "\n" + tempMessage;

                    //create new TextView for tempMessage
                    TextView tv = new TextView(getContext());
                    //set text to tempMessage and change text size
                    tv.setText(tempMessage);
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    //set to right side on bottom, with 16px margin
                    tv.setGravity(GravityCompat.END);
                    tv.setLayoutParams(params);
                    //add to LinearLayout within ScrollView
                    chatWindow.addView(tv);

                    Toast.makeText(getActivity(), tempMessage, Toast.LENGTH_SHORT).show();
                }

                //reset chatEdit back to empty
                chatEdit.setText("");

            }
        });

//        setCancelable(false);

        return view;
    }

}
