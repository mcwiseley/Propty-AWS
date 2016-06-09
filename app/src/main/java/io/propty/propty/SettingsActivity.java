package io.propty.propty;

//This is the relevant activity for the settings menu.  It requires activity_settings.xml
//(obviously) and the string arrays in values/strings.xml to work properly.

//TODO: Still need code for:
//updating zip code when zip radio button is already selected and user changes zip EditText.
//

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;


public class SettingsActivity extends AppCompatActivity {

    //unique tag for intents:
    public final static String EXTRA = "io.propty.propty.MESSAGE";

    private NumberPicker numBedrooms;
    private NumberPicker numBathrooms;
    private EditText minPrice;
    private EditText maxPrice;
    private Spinner squareFootage;
    private Spinner structure;
    private CheckBox pool;
    private CheckBox garage;
    private EditText within;
    private RadioButton radioCurrentLocation;
    private RadioButton radioZip;
    private EditText zip;
    private Button submit;

    float numBedrooms_val;
    float numBathrooms_val;
    int minPrice_val;
    int maxPrice_val;
    int squareFootage_val;
    int structure_val;
    boolean pool_val;
    boolean garage_val;
    int within_val;
    int zip_val;

    final static String numBedrooms_string = EXTRA + "numBedrooms";
    final static String numBathrooms_string = EXTRA + "numBathrooms";
    final static String minPrice_string = EXTRA + "minPrice";
    final static String maxPrice_string = EXTRA + "maxPrice";
    final static String squareFootage_string = EXTRA + "squareFootage";
    final static String structure_string = EXTRA + "structure";
    final static String pool_string = EXTRA + "pool";
    final static String garage_string = EXTRA + "garage";
    final static String within_string = EXTRA + "within";
    final static String zip_string = EXTRA + "zip";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        numBedrooms = (NumberPicker) findViewById(R.id.numBedrooms);
        numBathrooms = (NumberPicker) findViewById(R.id.numBathrooms);
        minPrice = (EditText) findViewById(R.id.minPrice);
        maxPrice = (EditText) findViewById(R.id.maxPrice);
        squareFootage = (Spinner) findViewById(R.id.squareFootage);
        structure = (Spinner) findViewById(R.id.structure);
        pool = (CheckBox) findViewById(R.id.pool);
        garage = (CheckBox) findViewById(R.id.garage);
        within = (EditText) findViewById(R.id.within);
        radioCurrentLocation = (RadioButton) findViewById(R.id.radioCurrentLocation);
        radioZip = (RadioButton) findViewById(R.id.radioZip);
        zip = (EditText) findViewById(R.id.zip);
        submit = (Button) findViewById(R.id.submit);


        //Populate Number Pickers for number of bathrooms and bedrooms

        populateNumberPicker(numBedrooms, 1, 10, 2);
        populateNumberPicker(numBathrooms, 1, 5, 4);
        numBedrooms.setWrapSelectorWheel(false);
        numBathrooms.setWrapSelectorWheel(false);

        //Populate Spinner of possible square footage options from string array in strings.xml

        ArrayAdapter<CharSequence> footage_adapter = ArrayAdapter.createFromResource(this,
                R.array.squareFootage_array, android.R.layout.simple_spinner_item);
        footage_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        squareFootage.setAdapter(footage_adapter);

        //Populate Spinner of possible structures from string array in strings.xml

        ArrayAdapter<CharSequence> structure_adapter = ArrayAdapter.createFromResource(this,
                R.array.structure_array, android.R.layout.simple_spinner_item);
        structure_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        structure.setAdapter(structure_adapter);

        //Make editing Zip field update zip radio button
    }

    //Method to populate a number picker with fractional values.  Takes a NumberPicker, minimum
    //value, maximum value, and divisor as argument.  For instance, (NumberPicker, 1, 2, 4) will
    //return a NumberPicker with the values 1, 1.25, 1.5, 1.75, and 2.
    protected void populateNumberPicker(NumberPicker num, int min, int max, int divisor){
        num.setMinValue(min * divisor);
        num.setMaxValue(max * divisor);
        String[] fractionRooms = new String[num.getMaxValue() - num.getMinValue() + 1];
        for(int i = num.getMinValue(); i <= num.getMaxValue(); i++){
            double j = i;
            fractionRooms[i - num.getMinValue()] = Double.toString(j / num.getMinValue());
        }
        num.setDisplayedValues(fractionRooms);
    }

    //This sets zip code according to which radio button is checked
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.radioCurrentLocation:
                if (checked)
                    // set zip code variable to current location zip.  Using 98765 as a placeholder
                    zip_val = 98765;
                break;
            case R.id.radioZip:
                if (checked)
                    // set zip code variable to whatever is in the zip field
                    // needs checks for empty field or illegitimate zip code.  dummy check below
                    if(zip.getText().length()==0){
                        zip_val = 00000;
                        break;
                    }
                zip_val = Integer.parseInt(zip.getText().toString());
                break;
        }
    }



    public void submitSettings(View view) {

        //Check for an empty minimum price field
        if(minPrice.getText().toString().length() == 0){
            Toast.makeText(getApplicationContext(), "Please enter a minimum price.", Toast.LENGTH_SHORT).show();
            return;
        }

        //Check for an empty maximum price field
        if(maxPrice.getText().toString().length() == 0){
            Toast.makeText(getApplicationContext(), "Please enter a maximum price.", Toast.LENGTH_SHORT).show();
            return;
        }

        //Check if minimum price is greater than maximum price
        if(Integer.parseInt(maxPrice.getText().toString()) <
                Integer.parseInt(minPrice.getText().toString())){
            Toast.makeText(getApplicationContext(), "Please enter a maximum price" +
                    " that is greater than the minimum price.", Toast.LENGTH_SHORT).show();
            return;
        }

        //Check to make sure the Within field has been filled
        if(within.getText().toString().length() == 0){
            Toast.makeText(getApplicationContext(), "Please enter a distance.", Toast.LENGTH_SHORT).show();
            return;
        }

        //Check to make sure the distance isn't ridiculously large
        if(within.getText().toString().length() > 3){
            Toast.makeText(getApplicationContext(), "Please enter a distance " +
                    "less than 1,000 miles.", Toast.LENGTH_SHORT).show();
            return;
        }

        //Check that one of the radio buttons is selected
        if(!radioCurrentLocation.isChecked() && !radioZip.isChecked()){
            Toast.makeText(getApplicationContext(), "Please select either Current Location" +
                    " or ZIP code.", Toast.LENGTH_SHORT).show();
            return;
        }

        //If ZIP is selected, check to make sure there is something in it

        if(radioZip.isChecked()){
            if(zip.getText().toString().length() == 0){
                Toast.makeText(getApplicationContext(), "Please enter a ZIP code.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        //Check that ZIP code is 5 digits long
        if(radioZip.isChecked() && zip.getText().toString().length() != 5){
            Toast.makeText(getApplicationContext(), "Please enter a valid ZIP code.", Toast.LENGTH_SHORT).show();
            return;
        }

        //If everything's gravy, make an intent and go to next activity

        Intent intent = new Intent(this, SettingsResultsActivity.class);

        numBedrooms_val = (float)(numBedrooms.getValue()) / 2;

        numBathrooms_val = (float)(numBathrooms.getValue()) / 4;

        minPrice_val = Integer.parseInt(minPrice.getText().toString());

        maxPrice_val = Integer.parseInt(maxPrice.getText().toString());

        //Square Footage and Structure values will be integers corresponding to the selected index,
        //These values will only be meaningful once we settle on what the options are.
        squareFootage_val = squareFootage.getSelectedItemPosition();

        structure_val = structure.getSelectedItemPosition();

        pool_val = pool.isChecked();

        garage_val = garage.isChecked();

        within_val = Integer.parseInt(within.getText().toString());

        if(radioZip.isChecked()) {
            zip_val = Integer.parseInt(zip.getText().toString());
        }

        intent.putExtra(numBedrooms_string, numBedrooms_val);
        intent.putExtra(numBathrooms_string, numBathrooms_val);
        intent.putExtra(minPrice_string, minPrice_val);
        intent.putExtra(maxPrice_string, maxPrice_val);
        intent.putExtra(squareFootage_string, squareFootage_val);
        intent.putExtra(structure_string, structure_val);
        intent.putExtra(pool_string, pool_val);
        intent.putExtra(garage_string, garage_val);
        intent.putExtra(within_string, within_val);
        intent.putExtra(zip_string, zip_val);
        startActivity(intent);

    }

}
