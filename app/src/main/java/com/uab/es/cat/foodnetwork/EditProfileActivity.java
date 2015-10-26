package com.uab.es.cat.foodnetwork;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.uab.es.cat.foodnetwork.database.CacheDbHelper;
import com.uab.es.cat.foodnetwork.database.FoodNetworkDbHelper;
import com.uab.es.cat.foodnetwork.dto.LocationDTO;
import com.uab.es.cat.foodnetwork.dto.UserDTO;

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

        UserDTO userDTO = new UserDTO();
        LocationDTO locationDTO = new LocationDTO();

        locationDTO.setStreetName(streetNameText.getText().toString());
        locationDTO.setBuildingNumber(buildingNumberText.getText().toString());
        locationDTO.setFloor(floorText.getText().toString());
        locationDTO.setDoor(doorText.getText().toString());
        locationDTO.setCity(cityText.getText().toString());
        locationDTO.setNeighborhood(neighborhood);
        locationDTO.setDistrict(district);

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.user_type), Context.MODE_PRIVATE);
        int defaultValue = 0;
        int userId = sharedPref.getInt(getString(R.string.user_id), defaultValue);

        userDTO.setIdUser(userId);

        FoodNetworkDbHelper mDbHelper = new FoodNetworkDbHelper(getApplicationContext());

        CacheDbHelper cacheDbHelper = new CacheDbHelper();
        userDTO = (UserDTO) cacheDbHelper.getById(userDTO, mDbHelper);

        long idLocation = userDTO.getIdLocation();
        if(idLocation != 0){
            //Realizar update
        }else {
            long idLocationNew = cacheDbHelper.insert(locationDTO, mDbHelper);
            userDTO.setIdLocation(idLocationNew);
            cacheDbHelper.update(userDTO, mDbHelper);
        }
    }
}
