package com.uab.es.cat.foodnetwork;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.uab.es.cat.foodnetwork.database.CacheDbHelper;
import com.uab.es.cat.foodnetwork.database.FoodNetworkDbHelper;
import com.uab.es.cat.foodnetwork.dto.DonationDTO;
import com.uab.es.cat.foodnetwork.dto.LocationDTO;
import com.uab.es.cat.foodnetwork.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class AllDonationsMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private CacheDbHelper cacheDbHelper;
    private FoodNetworkDbHelper mDbHelper;
    private List<DonationDTO> listOfCurrentDonations;
    private List<DonationDTO> listOfCompletedDonations;
    private List<DonationDTO> listOfActiveDonations;
    private List<MarkerOptions> markersCurrentDonations = new ArrayList<MarkerOptions>();
    private List<MarkerOptions> markersCompletedDonations = new ArrayList<MarkerOptions>();
    private List<MarkerOptions> markersActiveDonations = new ArrayList<MarkerOptions>();
    private LatLng latLng;
    private GoogleMap map;
    private MapFragment mapFragment;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_donations_map);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        cacheDbHelper = new CacheDbHelper();
        mDbHelper = new FoodNetworkDbHelper(getApplicationContext());

        listOfCompletedDonations = getCompletedDonations(Constants.COMPLETED_STATE);
        listOfCurrentDonations = getCurrentDonations(Constants.CURRENT_STATE);
        listOfActiveDonations = getActiveDonations(Constants.ACTIVE_STATE);

        generateCurrentDonationsMarkers();
        generateCompletedDonationsMarkers();
        generateActiveDonationsMarkers();


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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_all_donations_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.all_state) {
            map.clear();
            for (MarkerOptions options : markersActiveDonations){
                map.addMarker(options);
            }
            for (MarkerOptions options : markersCurrentDonations){
                map.addMarker(options);
            }
            for (MarkerOptions options : markersCompletedDonations){
                map.addMarker(options);
            }
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));

            return true;
        }
        if (id == R.id.active_state){
            map.clear();
            for (MarkerOptions options : markersActiveDonations){
                map.addMarker(options);
            }
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
            return true;
        }
        if (id == R.id.completed_state) {
            map.clear();
            for (MarkerOptions options : markersCompletedDonations){
                map.addMarker(options);
            }
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
            return true;
        }
        if (id == R.id.current_state){
            map.clear();
            for (MarkerOptions options : markersCurrentDonations){
                map.addMarker(options);
            }
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;

        for (MarkerOptions options : markersActiveDonations){
            map.addMarker(options);
        }
        for (MarkerOptions options : markersCurrentDonations){
            map.addMarker(options);
        }
        for (MarkerOptions options : markersCompletedDonations){
            map.addMarker(options);
        }
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
    }

    public List<DonationDTO> getCompletedDonations(int state){
        return cacheDbHelper.getDonationsByState(mDbHelper, state);
    }

    public List<DonationDTO> getCurrentDonations(int state){
        return cacheDbHelper.getDonationsByState(mDbHelper, state);
    }

    public List<DonationDTO> getActiveDonations(int state){
        return cacheDbHelper.getDonationsByState(mDbHelper, state);
    }

    private void generateCurrentDonationsMarkers(){
        for(DonationDTO donationDTO : listOfCurrentDonations) {
            LocationDTO locationDTO = new LocationDTO();
            locationDTO.setIdLocation(donationDTO.getIdLocation());
            locationDTO = (LocationDTO) cacheDbHelper.getById(locationDTO, mDbHelper);

            latLng = new LatLng(Double.valueOf(locationDTO.getLatitude()), Double.valueOf(locationDTO.getLongitude()));
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(donationDTO.getInitialHour() + " " + getString(R.string.to) + " " + donationDTO.getFinalHour())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));

            markersCurrentDonations.add(options);
        }
    }

    private void generateCompletedDonationsMarkers(){
        for(DonationDTO donationDTO : listOfCompletedDonations) {
            LocationDTO locationDTO = new LocationDTO();
            locationDTO.setIdLocation(donationDTO.getIdLocation());
            locationDTO = (LocationDTO) cacheDbHelper.getById(locationDTO, mDbHelper);

            latLng = new LatLng(Double.valueOf(locationDTO.getLatitude()), Double.valueOf(locationDTO.getLongitude()));
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(donationDTO.getInitialHour() + " " + getString(R.string.to) + " " + donationDTO.getFinalHour())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

            markersCompletedDonations.add(options);
        }
    }

    private void generateActiveDonationsMarkers(){
        for(DonationDTO donationDTO : listOfActiveDonations) {
            LocationDTO locationDTO = new LocationDTO();
            locationDTO.setIdLocation(donationDTO.getIdLocation());
            locationDTO = (LocationDTO) cacheDbHelper.getById(locationDTO, mDbHelper);

            latLng = new LatLng(Double.valueOf(locationDTO.getLatitude()), Double.valueOf(locationDTO.getLongitude()));
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(donationDTO.getInitialHour() + " " + getString(R.string.to) + " " + donationDTO.getFinalHour())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

            markersActiveDonations.add(options);
        }
    }

    public void handleOnBackPress(){
        startActivity(new Intent(getApplicationContext(), MainReceptorActivity.class));
    }
}
