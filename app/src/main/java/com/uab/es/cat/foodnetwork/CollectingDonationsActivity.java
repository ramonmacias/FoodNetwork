package com.uab.es.cat.foodnetwork;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.uab.es.cat.foodnetwork.database.CacheDbHelper;
import com.uab.es.cat.foodnetwork.database.FoodNetworkDbHelper;
import com.uab.es.cat.foodnetwork.dto.DonationDTO;
import com.uab.es.cat.foodnetwork.dto.LocationDTO;
import com.uab.es.cat.foodnetwork.util.Utilities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CollectingDonationsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private int totalSelectedDonations;
    private int totalWeightOfDonations;
    List<MarkerOptions> markers = new ArrayList<MarkerOptions>();
    private CacheDbHelper cacheDbHelper;
    private FoodNetworkDbHelper mDbHelper;
    private MapFragment mapFragment;
    private GoogleMap map;
    private LatLng latLng;
    private double bancAlimentsLatitude = 41.35062245;
    private double bancAlimentsLongitude = 2.14470506;
    private List<DonationDTO> donations;

    TextView totalWeightOfDonationsTextView;
    TextView totalNumOfDonationsTextView;
    TextView dateOfCollectingTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collecting_donations);

        cacheDbHelper = new CacheDbHelper();
        mDbHelper = new FoodNetworkDbHelper(getApplicationContext());

        totalWeightOfDonationsTextView = (TextView) findViewById(R.id.totalWeight);
        totalNumOfDonationsTextView = (TextView) findViewById(R.id.totalNumOfDonations);
        dateOfCollectingTextView = (TextView) findViewById(R.id.collectingDate);


        Intent intent = getIntent();
        donations = (List<DonationDTO>) intent.getSerializableExtra("ListOfDonationsSelected");
        totalSelectedDonations = donations.size();
        generateStaticMarkers();
        for(DonationDTO donationDTO : donations){
            generateNewMarker(donationDTO);
            increaseTotalWeight(donationDTO);
            updateDonationStatus(donationDTO);
        }

        totalWeightOfDonationsTextView.setText(getString(R.string.total_weight) + ": " + String.valueOf(totalWeightOfDonations));
        totalNumOfDonationsTextView.setText(getString(R.string.number_of_donations) + ": " + String.valueOf(totalSelectedDonations));
        dateOfCollectingTextView.setText(getString(R.string.collecting_date) + " " + Utilities.dateToString(new Date()));

        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void generateStaticMarkers(){
        latLng = new LatLng(bancAlimentsLatitude, bancAlimentsLongitude);
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(getString(R.string.food_bank))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));

        markers.add(options);
    }

    public void generateNewMarker(DonationDTO donationDTO){

        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setIdLocation(donationDTO.getIdLocation());
        locationDTO = (LocationDTO) cacheDbHelper.getById(locationDTO, mDbHelper);

        latLng = new LatLng(Double.valueOf(locationDTO.getLatitude()), Double.valueOf(locationDTO.getLongitude()));
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(donationDTO.getInitialHour() + " " + getString(R.string.to) + " " + donationDTO.getFinalHour());

        markers.add(options);
    }

    public void increaseTotalWeight(DonationDTO donationDTO){
        totalWeightOfDonations += donationDTO.getTotalWeight();
    }

    public void updateDonationStatus(DonationDTO donationDTO){
        donationDTO.setState(2);
        cacheDbHelper.update(donationDTO, mDbHelper);
    }

    public void finalizeCollect(View view){
        for(DonationDTO donationDTO : donations){
            donationDTO.setState(3);
            cacheDbHelper.update(donationDTO, mDbHelper);
        }
        startActivity(new Intent(this, MainReceptorActivity.class));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;

        for (MarkerOptions options : markers){
            map.addMarker(options);
        }
        map.setMyLocationEnabled(true);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
    }
}
