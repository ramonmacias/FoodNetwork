package com.uab.es.cat.foodnetwork;

import android.app.ListActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.uab.es.cat.foodnetwork.database.CacheDbHelper;
import com.uab.es.cat.foodnetwork.database.FoodNetworkDbHelper;
import com.uab.es.cat.foodnetwork.dto.DonationDTO;
import com.uab.es.cat.foodnetwork.dto.FoodsDTO;
import com.uab.es.cat.foodnetwork.dto.LocationDTO;
import com.uab.es.cat.foodnetwork.dto.UserDTO;
import com.uab.es.cat.foodnetwork.util.UserSession;
import com.uab.es.cat.foodnetwork.util.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FoodDonationActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private TextView quantityTextView;
    private EditText productNameText;
    private int quantityCount;
    private int totalWeight;
    private List<FoodsDTO> foodsOfDonation;
    private CacheDbHelper cacheDbHelper;
    private FoodNetworkDbHelper mDbHelper;
    private MapFragment mapFragment;
    private GoogleMap map;
    protected Location mLastLocation;
    private LocationRequest mLocationRequest;
    private Toolbar mToolbar;

    //Atributes to manage the geopoint in the map
    double latitudeProfile;
    double longitudeProfile;
    double myLatitude;
    double myLongitude;
    String userName;
    private ListView ItemsLst;

    GoogleApiClient mGoogleApiClient;

    /** Items entered by the user is stored in this ArrayList variable */
    ArrayList list = new ArrayList();

    /** Declaring an ArrayAdapter to set items to ListView */
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_donation);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ItemsLst = (ListView) findViewById(R.id.listview);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create().setNumUpdates(1)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        mGoogleApiClient.connect();

        long userId = UserSession.getInstance(getApplicationContext()).getUserId();
        UserDTO userDTO = new UserDTO();
        LocationDTO locationDTO = new LocationDTO();
        userDTO.setIdUser(userId);

        cacheDbHelper = new CacheDbHelper();
        mDbHelper = new FoodNetworkDbHelper(getApplicationContext());

        userDTO = (UserDTO) cacheDbHelper.getById(userDTO, mDbHelper);
        userName = userDTO.getName();
        locationDTO.setIdLocation(userDTO.getIdLocation());
        locationDTO = (LocationDTO) cacheDbHelper.getById(locationDTO, mDbHelper);

        latitudeProfile = Double.valueOf(locationDTO.getLatitude());
        longitudeProfile = Double.valueOf(locationDTO.getLongitude());

        productNameText = (EditText) findViewById(R.id.productName);
        quantityTextView = (TextView) findViewById(R.id.quantity);
        quantityCount = 1;
        totalWeight = 0;

        foodsOfDonation = new ArrayList<FoodsDTO>();

        findViewById(R.id.plusButton).setOnClickListener(this);
        findViewById(R.id.minusButton).setOnClickListener(this);

        Spinner spinnerInitialHour = (Spinner) findViewById(R.id.initial_hour);
        Spinner spinnerFinalHour = (Spinner) findViewById(R.id.final_hour);

        ArrayAdapter<CharSequence> adapterInitialHour = ArrayAdapter.createFromResource(this,
                R.array.hours, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterFinalHour = ArrayAdapter.createFromResource(this,
                R.array.hours, android.R.layout.simple_spinner_item);

        adapterInitialHour.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterFinalHour.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerInitialHour.setAdapter(adapterInitialHour);
        spinnerFinalHour.setAdapter(adapterFinalHour);

        /** Defining the ArrayAdapter to set items to ListView */
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, list);

        ItemsLst.setAdapter(adapter);

        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOnBackPress();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.plusButton:
                quantityCount++;
                quantityTextView.setText(String.valueOf(quantityCount));
                break;
            case R.id.minusButton:
                if(quantityCount > 1){
                    quantityCount--;
                    quantityTextView.setText(String.valueOf(quantityCount));
                }
                break;
        }
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.my_location:
                if (checked) {
                    LatLng latLng = new LatLng(myLatitude, myLongitude);
                    MarkerOptions options = new MarkerOptions()
                            .position(latLng)
                            .title(userName);
                    map.addMarker(options);
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));
                }
                    break;
            case R.id.profile_location:
                if (checked) {
                    LatLng latLng = new LatLng(latitudeProfile, longitudeProfile);
                    MarkerOptions options = new MarkerOptions()
                            .position(latLng)
                            .title(userName);
                    map.addMarker(options);
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));
                }
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;

        LatLng latLng = new LatLng(latitudeProfile, longitudeProfile);

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(userName);
        map.addMarker(options);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));
    }

    public void addToDonation(View view){
        String productName = productNameText.getText().toString();

        FoodsDTO foodsDTO = new FoodsDTO();
        foodsDTO.setFoodName(productName);
        foodsDTO.setQuantity(quantityCount);
        totalWeight += quantityCount;

        foodsOfDonation.add(foodsDTO);

        list.add(String.valueOf(quantityCount) + " Kg/l " + productName);
        adapter.notifyDataSetChanged();

        quantityCount = 1;
        quantityTextView.setText("1");
        productNameText.setText("");
    }

    public void removeFromDonation(View view){

        /** Getting the checked items from the listview */
        SparseBooleanArray checkedItemPositions = ItemsLst.getCheckedItemPositions();
        int itemCount = ItemsLst.getCount();

        for(int i=itemCount-1; i >= 0; i--){
            if(checkedItemPositions.get(i)){
                adapter.remove(list.get(i));
                totalWeight -= foodsOfDonation.get(i).getQuantity();
                foodsOfDonation.remove(i);
            }
        }
        checkedItemPositions.clear();
        adapter.notifyDataSetChanged();

    }

    public void donate(View view){

        long userId = UserSession.getInstance(getApplicationContext()).getUserId();

        UserDTO userDTO = new UserDTO();
        userDTO.setIdUser(userId);

        userDTO = (UserDTO) cacheDbHelper.getById(userDTO, mDbHelper);

        Spinner spinnerInitialHour = (Spinner) findViewById(R.id.initial_hour);
        Spinner spinnerFinalHour = (Spinner) findViewById(R.id.final_hour);

        String initialHour = spinnerInitialHour.getSelectedItem().toString();
        String finalHour = spinnerFinalHour.getSelectedItem().toString();

        boolean myLocationChecked = ((RadioButton)findViewById(R.id.my_location)).isChecked();
        boolean myProfileChecked = ((RadioButton)findViewById(R.id.profile_location)).isChecked();

        DonationDTO donationDTO = new DonationDTO();

        if(myLocationChecked){
            LocationDTO locationDTO = new LocationDTO();
            locationDTO.setLongitude(String.valueOf(myLongitude));
            locationDTO.setLatitude(String.valueOf(myLatitude));
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(myLatitude, myLongitude, 1);
                if(addresses != null && addresses.size() > 0){
                    Address address = addresses.get(0);
                    ArrayList<String> addressFragments = new ArrayList<String>();

                    // Fetch the address lines using getAddressLine,
                    // join them, and send them to the thread.
                    for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        addressFragments.add(address.getAddressLine(i));
                    }
                    if(addressFragments != null && addressFragments.size() > 0){
                        if(addressFragments.size() == 3){
                            locationDTO.setCompleteAdrressFromGeocoder(addressFragments.get(1) + " Barcelona");
                        }else {
                            String fullAdress = "";
                            for(String partFromAddress : addressFragments){
                                fullAdress += partFromAddress;
                            }
                            locationDTO.setCompleteAdrressFromGeocoder(fullAdress);
                        }
                    }
                }
            } catch (IOException e) {
                ShowToast(getString(R.string.no_address_found));
            }
            long locationId = cacheDbHelper.insert(locationDTO, mDbHelper);
            donationDTO.setIdLocation(locationId);
        }else {
            donationDTO.setIdLocation(userDTO.getIdLocation());
        }

        donationDTO.setIdUser(userDTO.getIdUser());
        donationDTO.setInitialHour(initialHour);
        donationDTO.setFinalHour(finalHour);
        donationDTO.setState(1);
        donationDTO.setInsertDate(Utilities.dateToString(new Date()));
        donationDTO.setTotalWeight(totalWeight);

        Long idDonation = cacheDbHelper.insert(donationDTO, mDbHelper);
        for(FoodsDTO foodsDTO : foodsOfDonation){
            foodsDTO.setIdDonation(idDonation);
            cacheDbHelper.insert(foodsDTO, mDbHelper);

        }
        startActivity(new Intent(getApplicationContext(), MainDonateActivity.class));

    }

    @Override
    public void onConnected(Bundle bundle) {

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        myLatitude = location.getLatitude();
        myLongitude = location.getLongitude();
    }

    public void ShowToast(String message){
        Toast.makeText(this, message,
                Toast.LENGTH_LONG).show();
    }

    public void handleOnBackPress(){
        startActivity(new Intent(getApplicationContext(), MainDonateActivity.class));
    }
}
