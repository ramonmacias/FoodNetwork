package com.uab.es.cat.foodnetwork;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
import com.uab.es.cat.foodnetwork.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class DonationDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private MapFragment mapFragment;
    CacheDbHelper cacheDbHelper;
    FoodNetworkDbHelper mDbHelper;
    private TextView dateOfDonationTextView;
    private TextView stateOfDonationTextView;
    private TextView initialHourTextView;
    private TextView finalHourTextView;
    private Toolbar mToolbar;
    private ListView ItemsLst;

    double latitude;
    double longitude;

    /** Items entered by the user is stored in this ArrayList variable */
    ArrayList list = new ArrayList();

    /** Declaring an ArrayAdapter to set items to ListView */
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_detail);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ItemsLst = (ListView) findViewById(R.id.listview);

        Intent intent = getIntent();
        String idDonation = intent.getStringExtra(DonationsActivity.DONATION_ID);

        DonationDTO donationDTO = new DonationDTO();
        donationDTO.setIdDonation(Long.valueOf(idDonation));

        LocationDTO locationDTO = new LocationDTO();

        cacheDbHelper = new CacheDbHelper();
        mDbHelper = new FoodNetworkDbHelper(getApplicationContext());

        donationDTO = (DonationDTO) cacheDbHelper.getById(donationDTO, mDbHelper);
        locationDTO.setIdLocation(donationDTO.getIdLocation());
        locationDTO = (LocationDTO) cacheDbHelper.getById(locationDTO, mDbHelper);
        longitude = Double.valueOf(locationDTO.getLongitude());
        latitude = Double.valueOf(locationDTO.getLatitude());

        dateOfDonationTextView = (TextView) findViewById(R.id.dateOfDonation);
        stateOfDonationTextView = (TextView) findViewById(R.id.stateOfDonation);
        initialHourTextView = (TextView) findViewById(R.id.initial_hour);
        finalHourTextView = (TextView) findViewById(R.id.final_hour);

        dateOfDonationTextView.setText(donationDTO.getInsertDate());
        stateOfDonationTextView.setText(Constants.STATE.get(donationDTO.getState()));
        initialHourTextView.setText(donationDTO.getInitialHour());
        finalHourTextView.setText(donationDTO.getFinalHour());

        List<FoodsDTO> foods = cacheDbHelper.getFoodsOfDonation(donationDTO.getIdDonation(), mDbHelper);

        for(FoodsDTO foodsDTO : foods){
            list.add(foodsDTO.getQuantity() + " Kg/l " + foodsDTO.getFoodName());
        }

        /** Defining the ArrayAdapter to set items to ListView */
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);

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
    public void onMapReady(GoogleMap map) {
        this.map = map;

        LatLng latLng = new LatLng(latitude, longitude);

        MarkerOptions options = new MarkerOptions()
                .position(latLng);
        map.addMarker(options);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));
    }

    public void handleOnBackPress(){
        startActivity(new Intent(getApplicationContext(), DonationsActivity.class));
    }

}
