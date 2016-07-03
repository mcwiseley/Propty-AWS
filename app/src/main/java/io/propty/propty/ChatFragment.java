package io.propty.propty;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends DialogFragment {

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
        View view = inflater.inflate(R.layout.fragment_chat, null);

        setCancelable(false);

        chatEdit = (EditText) getActivity().findViewById(R.id.chat_edit);

        cancelButton = (Button) getActivity().findViewById(R.id.chat_cancel_button);
        sendButton = (Button) getActivity().findViewById(R.id.chat_send_button);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        return view;
    }

}
