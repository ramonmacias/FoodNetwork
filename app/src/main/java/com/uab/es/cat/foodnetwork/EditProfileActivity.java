package com.uab.es.cat.foodnetwork;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class EditProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_profile);

        Spinner spinner = (Spinner) findViewById(R.id.spinner_neighborhood);
        Spinner spinner_districts = (Spinner) findViewById(R.id.spinner_district);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.neighborhoods, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterDistricts = ArrayAdapter.createFromResource(this,
                R.array.districts, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterDistricts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner_districts.setAdapter(adapterDistricts);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_edit_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateUserInfo(View view){

        EditText streetNameText = (EditText) findViewById(R.id.streetName);
        EditText buildingNumberText = (EditText) findViewById(R.id.buildingNumber);
        EditText floorText = (EditText) findViewById(R.id.floor);
        EditText doorText = (EditText) findViewById(R.id.door);
        EditText cityText = (EditText) findViewById(R.id.city);
        Spinner spinner = (Spinner) findViewById(R.id.spinner_neighborhood);
        Spinner spinner_districts = (Spinner) findViewById(R.id.spinner_district);

        String neighborhood = spinner.getSelectedItem().toString();
        String district = spinner_districts.getSelectedItem().toString();
    }
}
