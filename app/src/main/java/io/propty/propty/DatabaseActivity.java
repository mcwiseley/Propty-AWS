package io.propty.propty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DatabaseActivity extends AppCompatActivity {

    TextView idView;
    EditText desc;
    EditText listId;
    EditText areaUnits;
    EditText type;
    EditText beds;
    EditText area;
    EditText image;
    EditText baths;
    EditText price;
    EditText address;
    EditText zip;
    Button queryListings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        //set up the Toolbar with Up Navigation
        Toolbar toolbar = (Toolbar) findViewById(R.id.database_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Database");

        idView = (TextView) findViewById(R.id.productID);
        desc = (EditText) findViewById(R.id.desc);
        listId = (EditText) findViewById(R.id.listId);
        areaUnits = (EditText) findViewById(R.id.areaUnits);
        type = (EditText) findViewById(R.id.type);
        beds = (EditText) findViewById(R.id.beds);
        area = (EditText) findViewById(R.id.area);
        image = (EditText) findViewById(R.id.image);
        baths = (EditText) findViewById(R.id.baths);
        price = (EditText) findViewById(R.id.price);
        address = (EditText) findViewById(R.id.address);
        zip = (EditText) findViewById(R.id.zip);
        queryListings = (Button) findViewById(R.id.btQueryListings);

        queryListings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent queryDatabase = new Intent(getApplicationContext(), QueryDatabaseActivity.class);
                startActivity(queryDatabase);
            }
        });

    }

    public void newProperty(View view) {
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);

        if(desc.getText().toString().isEmpty() || listId.getText().toString().isEmpty() ||
                areaUnits.getText().toString().isEmpty() || type.getText().toString().isEmpty() ||
                beds.getText().toString().isEmpty() || area.getText().toString().isEmpty() ||
                image.getText().toString().isEmpty() || baths.getText().toString().isEmpty() ||
                price.getText().toString().isEmpty() || address.getText().toString().isEmpty() ||
                zip.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "Please fill out all fields.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Property property = new Property(desc.getText().toString(), listId.getText().toString(),
                Integer.parseInt(beds.getText().toString()), Double.parseDouble(baths.getText().toString()),
                Integer.parseInt(area.getText().toString()), areaUnits.getText().toString(),
                Double.parseDouble(price.getText().toString()), type.getText().toString(),
                "", Integer.parseInt(image.getText().toString()), address.getText().toString(),
                Integer.parseInt(zip.getText().toString()));

        dbHandler.addProperty(property);
        desc.setText("");
        listId.setText("");
        areaUnits.setText("");
        type.setText("");
        beds.setText("");
        area.setText("");
        image.setText("");
        baths.setText("");
        price.setText("");
        address.setText("");
        zip.setText("");
    }

    public void lookupProperty(View view) {
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);

        Property property = dbHandler.findProperty(listId.getText().toString());

        if (property != null) {
            idView.setText(String.valueOf(property.getId()));
            desc.setText(String.valueOf(property.getDesc()));
            listId.setText(String.valueOf(property.getListId()));
            areaUnits.setText(String.valueOf(property.getAreaUnits()));
            type.setText(String.valueOf(property.getType()));
            beds.setText(String.valueOf(property.getBeds()));
            area.setText(String.valueOf(property.getArea()));
            image.setText(String.valueOf(property.getImage()));
            baths.setText(String.valueOf(property.getBaths()));
            price.setText(String.valueOf(property.getPrice()));
            address.setText(String.valueOf(property.getAddress()));
            zip.setText(String.valueOf(property.getZip()));
        } else {
            idView.setText("No Match Found");
        }
    }

    public void removeProperty(View view) {
        MyDBHandler dbHandler = new MyDBHandler(this, null,
                null, 1);

        boolean result = dbHandler.deleteProperty(
                listId.getText().toString());


        if (result)
        {
            idView.setText("Record Deleted");
            desc.setText("");
            listId.setText("");
            areaUnits.setText("");
            type.setText("");
            beds.setText("");
            area.setText("");
            image.setText("");
            baths.setText("");
            price.setText("");
            address.setText("");
            zip.setText("");
        }
        else
            idView.setText("No Match Found");
    }




}
