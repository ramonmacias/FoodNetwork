package com.uab.es.cat.foodnetwork;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.uab.es.cat.foodnetwork.database.CacheDbHelper;
import com.uab.es.cat.foodnetwork.database.FoodNetworkDbHelper;
import com.uab.es.cat.foodnetwork.dto.LocationDTO;
import com.google.android.gms.location.LocationServices;
import com.uab.es.cat.foodnetwork.dto.UserDTO;
import com.uab.es.cat.foodnetwork.util.Constants;
import com.uab.es.cat.foodnetwork.util.FetchAddressIntentService;
import com.uab.es.cat.foodnetwork.util.UserSession;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class EditProfileActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private EditText streetNameText;
    private EditText buildingNumberText;
    private EditText floorText;
    private EditText doorText;
    private EditText cityText;
    private Spinner spinner;
    private Spinner spinner_districts;
    private String addresToFind;

    GoogleApiClient mGoogleApiClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_profile);

        streetNameText = (EditText) findViewById(R.id.streetName);
        buildingNumberText = (EditText) findViewById(R.id.buildingNumber);
        floorText = (EditText) findViewById(R.id.floor);
        doorText = (EditText) findViewById(R.id.door);
        cityText = (EditText) findViewById(R.id.city);
        spinner = (Spinner) findViewById(R.id.spinner_neighborhood);
        spinner_districts = (Spinner) findViewById(R.id.spinner_district);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.neighborhoods, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterDistricts = ArrayAdapter.createFromResource(this,
                R.array.districts, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterDistricts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner_districts.setAdapter(adapterDistricts);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }


    @Override
    public void onConnected(Bundle bundle) {
    }

    public void updateUserInfo(LocationDTO locationDTO){

        UserDTO userDTO = new UserDTO();
        long userId = UserSession.getInstance(getApplicationContext()).getUserId();

        userDTO.setIdUser(userId);

        FoodNetworkDbHelper mDbHelper = new FoodNetworkDbHelper(getApplicationContext());

        CacheDbHelper cacheDbHelper = new CacheDbHelper();
        userDTO = (UserDTO) cacheDbHelper.getById(userDTO, mDbHelper);

        long idLocation = userDTO.getIdLocation();
        if(idLocation != 0){
            locationDTO.setIdLocation(idLocation);
            cacheDbHelper.update(locationDTO, mDbHelper);
        }else {
            long idLocationNew = cacheDbHelper.insert(locationDTO, mDbHelper);
            userDTO.setIdLocation(idLocationNew);
            cacheDbHelper.update(userDTO, mDbHelper);
        }

        String userType = UserSession.getInstance(getApplicationContext()).getUserType();

        if("D".equals(userType)){
            startActivity(new Intent(getApplicationContext(), MainDonateActivity.class));
        }else {
            startActivity(new Intent(getApplicationContext(), MainReceptorActivity.class));
        }
    }

    public void beginUpdateUserInfo(View view){

        addresToFind = streetNameText.getText().toString() + ", " + buildingNumberText.getText().toString() + ", " + cityText.getText().toString();
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocationName(addresToFind, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);

                String neighborhood = spinner.getSelectedItem().toString();
                String district = spinner_districts.getSelectedItem().toString();


                LocationDTO locationDTO = new LocationDTO();

                locationDTO.setStreetName(streetNameText.getText().toString());
                locationDTO.setBuildingNumber(buildingNumberText.getText().toString());
                locationDTO.setFloor(floorText.getText().toString());
                locationDTO.setDoor(doorText.getText().toString());
                locationDTO.setCity(cityText.getText().toString());
                locationDTO.setNeighborhood(neighborhood);
                locationDTO.setDistrict(district);
                locationDTO.setLatitude(String.valueOf(address.getLatitude()));
                locationDTO.setLongitude(String.valueOf(address.getLongitude()));

                updateUserInfo(locationDTO);
            }
        } catch (IOException e) {
            ShowToast(getString(R.string.no_address_found));
        }
    }

    public void ShowToast(String message){
        Toast.makeText(this, message,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
