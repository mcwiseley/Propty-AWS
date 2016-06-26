package io.propty.propty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

//This is an activity meant to display the user input from the settings page and show
//That the settings have been properly stored and transmitted via an intent.

public class SettingsResultsActivity extends AppCompatActivity {

    private TextView numBedrooms_fill;
    private TextView numBathrooms_fill;
    private TextView minPrice_fill;
    private TextView maxPrice_fill;
    private TextView squareFootage_fill;
    private TextView structure_fill;
    private TextView within_fill;
    private TextView zip_fill;
    private TextView keywords_fill;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_results);

        numBedrooms_fill = (TextView) findViewById(R.id.numBedrooms_fill);
        numBathrooms_fill = (TextView) findViewById(R.id.numBathrooms_fill);
        minPrice_fill = (TextView) findViewById(R.id.minPrice_fill);
        maxPrice_fill = (TextView) findViewById(R.id.maxPrice_fill);
        squareFootage_fill = (TextView) findViewById(R.id.squareFootage_fill);
        structure_fill = (TextView) findViewById(R.id.structure_fill);
        within_fill = (TextView) findViewById(R.id.within_fill);
        zip_fill = (TextView) findViewById(R.id.zip_fill);
        keywords_fill = (TextView) findViewById(R.id.keywords_fill);


        Intent intent = getIntent();

        float bedTemp = intent.getFloatExtra(SettingsActivity.numBedrooms_string, 0);
        numBedrooms_fill.setText(Float.toString(bedTemp));
        float bathTemp = intent.getFloatExtra(SettingsActivity.numBathrooms_string, 0);
        numBathrooms_fill.setText(Float.toString(bathTemp));
        int minTemp = intent.getIntExtra(SettingsActivity.minPrice_string, 0);
        minPrice_fill.setText(Integer.toString(minTemp));
        int maxTemp = intent.getIntExtra(SettingsActivity.maxPrice_string, 0);
        maxPrice_fill.setText(Integer.toString(maxTemp));
        int sqTemp = intent.getIntExtra(SettingsActivity.squareFootage_string, 0);
        squareFootage_fill.setText(Integer.toString(sqTemp));
        int structureTemp = intent.getIntExtra(SettingsActivity.structure_string, 0);
        structure_fill.setText(Integer.toString(structureTemp));
        int withinTemp = intent.getIntExtra(SettingsActivity.within_string, 0);
        within_fill.setText(Integer.toString(withinTemp));
        int zipTemp = intent.getIntExtra(SettingsActivity.zip_string, 0);
        zip_fill.setText(Integer.toString(zipTemp));
        String keywordsTemp = intent.getStringExtra(SettingsActivity.keywords_string);
        keywords_fill.setText(keywordsTemp);

    }
}
