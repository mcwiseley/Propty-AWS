package io.propty.propty;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class QueryDatabaseActivity extends AppCompatActivity {

    public static final String PREFS = "MyPrefsFile";
    TextView id1;
    TextView id2;
    TextView id3;
    TextView id4;
    TextView id5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_database);

        id1 = (TextView) findViewById(R.id.id1);
        id2 = (TextView) findViewById(R.id.id2);
        id3 = (TextView) findViewById(R.id.id3);
        id4 = (TextView) findViewById(R.id.id4);
        id5 = (TextView) findViewById(R.id.id5);

        SharedPreferences settings = getSharedPreferences(PREFS, 0);
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        ArrayList<Property> emptyList = new ArrayList<Property>();
        ArrayList<Property> fullList = dbHandler.listProperties(this, emptyList, 5);

        if(fullList.size() != 0)
            id1.setText(fullList.get(0).getListId());
        if(fullList.size() > 1)
            id2.setText(fullList.get(1).getListId());
        if(fullList.size() > 2)
            id3.setText(fullList.get(2).getListId());
        if(fullList.size() > 3)
            id4.setText(fullList.get(3).getListId());
        if(fullList.size() > 4)
            id5.setText(fullList.get(4).getListId());

    }
}
