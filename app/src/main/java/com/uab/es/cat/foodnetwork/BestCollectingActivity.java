package com.uab.es.cat.foodnetwork;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.uab.es.cat.foodnetwork.database.CacheDbHelper;
import com.uab.es.cat.foodnetwork.database.FoodNetworkDbHelper;
import com.uab.es.cat.foodnetwork.dto.DonationDTO;
import com.uab.es.cat.foodnetwork.dto.LocationDTO;
import com.uab.es.cat.foodnetwork.dto.UserDTO;
import com.uab.es.cat.foodnetwork.util.Constants;
import com.uab.es.cat.foodnetwork.util.Element;
import com.uab.es.cat.foodnetwork.util.HttpConnection;
import com.uab.es.cat.foodnetwork.util.PathJSONParser;
import com.uab.es.cat.foodnetwork.util.UserSession;
import com.uab.es.cat.foodnetwork.util.Utilities;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class BestCollectingActivity extends AppCompatActivity implements OnMapReadyCallback {

    private CacheDbHelper cacheDbHelper;
    private FoodNetworkDbHelper mDbHelper;
    private List<DonationDTO> donations;
    private List<DonationDTO> donationsAux;
    private List<Element> elements;
    private UserDTO userDTO;
    private String finalHour;
    private String initialHour;
    private int actionRadio;
    private double latitudeProfile;
    private double longitudeProfile;
    private double maximumWeight;

    private double bancAlimentsLatitude = 41.35062245;
    private double bancAlimentsLongitude = 2.14470506;
    private double homeLatitude;
    private double homeLongitude;
    private int totalSelectedDonations;
    private int totalWeightOfDonations;
    List<MarkerOptions> markers = new ArrayList<MarkerOptions>();
    private MapFragment mapFragment;
    private GoogleMap map;
    private LatLng latLng;
    private List<LatLng> coordenatesDonations = new ArrayList<LatLng>();
    private Toolbar mToolbar;

    TextView totalWeightOfDonationsTextView;
    TextView totalNumOfDonationsTextView;
    TextView dateOfCollectingTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_collecting);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        cacheDbHelper = new CacheDbHelper();
        mDbHelper = new FoodNetworkDbHelper(getApplicationContext());
        donationsAux = new ArrayList<DonationDTO>();
        elements = new ArrayList<Element>();

        userDTO = obtainUserInformation();
        getLocationProfileInfo();
        maximumWeight = Constants.WEIGHT_VEHICLES.get(userDTO.getTypeOfVehicle());
        donations = cacheDbHelper.getReadyAndCurrentDonations(mDbHelper);

        filterDonationsByUserInformation(userDTO);
        algorithmToFindBestWay();

        totalWeightOfDonationsTextView = (TextView) findViewById(R.id.totalWeight);
        totalNumOfDonationsTextView = (TextView) findViewById(R.id.totalNumOfDonations);
        dateOfCollectingTextView = (TextView) findViewById(R.id.collectingDate);

        totalSelectedDonations = donations.size();
        generateStaticMarkers();
        for(DonationDTO donationDTO : donations){
            generateNewMarker(donationDTO);
            increaseTotalWeight(donationDTO);
            updateDonationStatus(donationDTO);
        }

        totalWeightOfDonationsTextView.setText(getString(R.string.total_weight) + ": " + String.valueOf(totalWeightOfDonations) + " kg/l");
        totalNumOfDonationsTextView.setText(getString(R.string.number_of_donations) + ": " + String.valueOf(totalSelectedDonations));
        dateOfCollectingTextView.setText(getString(R.string.collecting_date) + " " + Utilities.dateToString(new Date()));

        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOnBackPress();
            }
        });

        String url = getMapsApiDirectionsUrl();
        ReadTask downloadTask = new ReadTask();
        downloadTask.execute(url);

    }


    private UserDTO obtainUserInformation(){
        UserDTO userDTO = new UserDTO();
        userDTO.setIdUser(UserSession.getInstance(getApplicationContext()).getUserId());
        return (UserDTO) cacheDbHelper.getById(userDTO, mDbHelper);
    }

    private void filterDonationsByUserInformation(UserDTO userDTO){
        finalHour = userDTO.getFinalHour();
        initialHour = userDTO.getInitialHour();
        actionRadio = userDTO.getActionRadio();

        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setIdLocation(userDTO.getIdLocation());
        locationDTO = (LocationDTO) cacheDbHelper.getById(locationDTO, mDbHelper);

        latitudeProfile = Double.valueOf(locationDTO.getLatitude());
        longitudeProfile = Double.valueOf(locationDTO.getLongitude());

        boolean longitudeCriteria = false;
        boolean rangeOfHoursCriteria = false;

        for(int i = 0; i < donations.size(); i++){
            double distance = validateLongitudeCriteria(donations.get(i));

            if(distance > actionRadio){
                longitudeCriteria = false;
            }else {
                longitudeCriteria = true;
            }

            rangeOfHoursCriteria = validateRangeOfHoursCriteria(donations.get(i));

            if(longitudeCriteria && rangeOfHoursCriteria){
                donationsAux.add(donations.get(i));
                Element element = new Element(distance, donations.get(i).getTotalWeight(), donations.get(i).getIdDonation());
                elements.add(element);
            }
        }
        donations.clear();
        donations.addAll(donationsAux);
        donationsAux.clear();
    }

    private double validateLongitudeCriteria(DonationDTO donationDTO){
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setIdLocation(donationDTO.getIdLocation());
        locationDTO = (LocationDTO) cacheDbHelper.getById(locationDTO, mDbHelper);

        double distance = Utilities.calculateDistanceBetweenTwoPoints(longitudeProfile, latitudeProfile, Double.valueOf(locationDTO.getLongitude()), Double.valueOf(locationDTO.getLatitude()));

        return  distance;
    }

    private boolean validateRangeOfHoursCriteria(DonationDTO donationDTO){

        int finalHourDonationValue = Constants.HOURS.get(donationDTO.getFinalHour());
        int initialHourDonationValue = Constants.HOURS.get(donationDTO.getInitialHour());

        int finalHourProfileValue = Constants.HOURS.get(finalHour);
        int initialHourProfileValue = Constants.HOURS.get(initialHour);

        if((initialHourProfileValue > finalHourDonationValue && initialHourProfileValue > initialHourDonationValue) ||
                (finalHourProfileValue < initialHourDonationValue && finalHourProfileValue < finalHourDonationValue)){
            return false;
        }else {
            return true;
        }
    }

    private void algorithmToFindBestWay(){

        Comparator cmp = new Comparator<Element>() {
            public int compare(Element x, Element y) {
                return (int) (x.getLongitude() - y.getLongitude());
            }
        };
        Collections.sort(elements, cmp);
        Collections.reverse(elements);

        double pesoMochila=0;
        int    posicion=0;
        while(pesoMochila<maximumWeight && posicion < elements.size()) {
            Element tmp = elements.get(posicion);
            if(pesoMochila + tmp.getWeight() <= maximumWeight) {
                donationsAux.add(donations.get(posicion));
                pesoMochila+=tmp.getWeight();
            }
            posicion++;
        }

        donations.clear();
        donations.addAll(donationsAux);
        donationsAux.clear();

    }

    public void generateStaticMarkers(){
        latLng = new LatLng(bancAlimentsLatitude, bancAlimentsLongitude);
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(getString(R.string.food_bank))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));

        latLng = new LatLng(homeLatitude, homeLongitude);
        MarkerOptions optionsHome = new MarkerOptions()
                .position(latLng)
                .title(getString(R.string.home))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));

        markers.add(options);
        markers.add(optionsHome);
    }

    public void generateNewMarker(DonationDTO donationDTO){

        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setIdLocation(donationDTO.getIdLocation());
        locationDTO = (LocationDTO) cacheDbHelper.getById(locationDTO, mDbHelper);

        latLng = new LatLng(Double.valueOf(locationDTO.getLatitude()), Double.valueOf(locationDTO.getLongitude()));
        coordenatesDonations.add(latLng);
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

    public void handleOnBackPress(){
        startActivity(new Intent(getApplicationContext(), MainReceptorActivity.class));
    }

    private void getLocationProfileInfo(){
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setIdLocation(userDTO.getIdLocation());
        locationDTO = (LocationDTO) cacheDbHelper.getById(locationDTO, mDbHelper);
        homeLatitude = Double.valueOf(locationDTO.getLatitude());
        homeLongitude = Double.valueOf(locationDTO.getLongitude());
    }

    private String getMapsApiDirectionsUrl(){
        String waypoints = getWaypoints();
        String OriDest = "origin="+homeLatitude+","+homeLongitude+"&destination="+bancAlimentsLatitude+","+bancAlimentsLongitude;

        String sensor = "sensor=false";
        String params = OriDest+"&"+waypoints + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + params;
        return url;
    }

    private String getWaypoints(){
        String waypoints = "";
        if(coordenatesDonations != null && coordenatesDonations.size() > 0){
            waypoints += "waypoints=optimize:true|";
            waypoints += coordenatesDonations.get(0).latitude + "," + coordenatesDonations.get(0).longitude;
            for(int i = 1; i < coordenatesDonations.size(); i++){
                waypoints += "|" + coordenatesDonations.get(i).latitude + "," + coordenatesDonations.get(i).longitude;
            }
        }
        return waypoints;
    }

    private class ReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }

    private class ParserTask extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;

            if(routes != null && routes.size() > 0){
                // traversing through routes
                for (int i = 0; i < routes.size(); i++) {
                    points = new ArrayList<LatLng>();
                    polyLineOptions = new PolylineOptions();
                    List<HashMap<String, String>> path = routes.get(i);

                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }

                    polyLineOptions.addAll(points);
                    polyLineOptions.width(6);
                    polyLineOptions.color(Color.BLUE);
                }

                map.addPolyline(polyLineOptions);
            }

        }
    }
}
