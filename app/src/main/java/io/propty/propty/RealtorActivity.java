package io.propty.propty;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
//TODO: Henry: Adjust this activity to have everything the list has, and then some!

// Activity that shows more info of Realtor
//along with button for user to choose Realtor
public class RealtorActivity extends AppCompatActivity {

    private TextView nameText;
    private Button pickButton;
    private String realtorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtor);

//        Bundle mBundle = getIntent().getExtras();

        //get string of realtor's name from RealtorListActivity
        Intent mIntent = getIntent();
        realtorName = mIntent.getStringExtra("string key");

        //set TextView to realtor's name
        nameText = (TextView) findViewById(R.id.realtor_name);
        nameText.setText(realtorName);

        //setup button
        pickButton = (Button) findViewById(R.id.realtor_button);
        pickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get data with boolean and string value, and bring back to RealtorListActivity
                Intent data = new Intent(getApplicationContext(), RealtorListActivity.class);
                data.putExtra("picked_realtor", realtorName);
                data.putExtra("picked_", true);
                setResult(RESULT_OK, data);
                finish();
            }
        });


    }

}
