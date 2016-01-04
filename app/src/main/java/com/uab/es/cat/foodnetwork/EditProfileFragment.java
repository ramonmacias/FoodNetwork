package com.uab.es.cat.foodnetwork;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.uab.es.cat.foodnetwork.database.CacheDbHelper;
import com.uab.es.cat.foodnetwork.database.FoodNetworkDbHelper;
import com.uab.es.cat.foodnetwork.dto.LocationDTO;
import com.uab.es.cat.foodnetwork.dto.UserDTO;
import com.uab.es.cat.foodnetwork.util.UserSession;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by ramonmacias on 19/12/15.
 */
public class EditProfileFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, SeekBar.OnSeekBarChangeListener {

    private EditText streetNameText;
    private EditText buildingNumberText;
    private EditText floorText;
    private EditText doorText;
    private EditText cityText;
    private EditText phoneText;
    private TextView nameText;
    private TextView lastNameText;
    private TextView textActionRadio;
    private TextView textTimeZone;
    private Spinner spinner;
    private Spinner spinner_districts;
    private String addresToFind;
    private FoodNetworkDbHelper mDbHelper;
    private CacheDbHelper cacheDbHelper;
    private UserDTO userDTO;
    private Spinner spinnerInitialHour;
    private Spinner spinnerFinalHour;
    private Spinner spinnerTypeVehicles;
    private SeekBar actionRadio;
    private int initialActionRadio;

    GoogleApiClient mGoogleApiClient;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        mDbHelper = new FoodNetworkDbHelper(getContext());
        cacheDbHelper = new CacheDbHelper();
        userDTO = new UserDTO();
        long userId = UserSession.getInstance(getContext()).getUserId();
        String userType = UserSession.getInstance(getContext()).getUserType();

        userDTO.setIdUser(userId);

        userDTO = (UserDTO) cacheDbHelper.getById(userDTO, mDbHelper);
        initialActionRadio = userDTO.getActionRadio();
        LocationDTO location = new LocationDTO();
        if(userDTO.getIdLocation() != 0){
            location.setIdLocation(userDTO.getIdLocation());
            location = (LocationDTO) cacheDbHelper.getById(location, mDbHelper);
        }

        streetNameText = (EditText) rootView.findViewById(R.id.streetName);
        buildingNumberText = (EditText) rootView.findViewById(R.id.buildingNumber);
        floorText = (EditText) rootView.findViewById(R.id.floor);
        doorText = (EditText) rootView.findViewById(R.id.door);
        cityText = (EditText) rootView.findViewById(R.id.city);
        phoneText = (EditText) rootView.findViewById(R.id.phone);
        nameText = (TextView) rootView.findViewById(R.id.name);
        textActionRadio = (TextView) rootView.findViewById(R.id.textActionRadio);
        lastNameText = (TextView) rootView.findViewById(R.id.lastName);
        spinner = (Spinner) rootView.findViewById(R.id.spinner_neighborhood);
        spinner_districts = (Spinner) rootView.findViewById(R.id.spinner_district);
        spinnerInitialHour = (Spinner) rootView.findViewById(R.id.initial_hour);
        spinnerFinalHour = (Spinner) rootView.findViewById(R.id.final_hour);
        spinnerTypeVehicles = (Spinner) rootView.findViewById(R.id.type_vehicles);
        actionRadio = (SeekBar) rootView.findViewById(R.id.actionRadio);
        textTimeZone = (TextView) rootView.findViewById(R.id.timeZone);

        actionRadio.setOnSeekBarChangeListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.neighborhoods, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterDistricts = ArrayAdapter.createFromResource(getContext(),
                R.array.districts, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterInitialHour = ArrayAdapter.createFromResource(getContext(),
                R.array.hours, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterFinalHour = ArrayAdapter.createFromResource(getContext(),
                R.array.hours, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterTypeOfVehicles = ArrayAdapter.createFromResource(getContext(),
                R.array.typeOfVehicles, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterDistricts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterInitialHour.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterFinalHour.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterTypeOfVehicles.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerInitialHour.setAdapter(adapterInitialHour);
        spinnerFinalHour.setAdapter(adapterFinalHour);
        spinnerTypeVehicles.setAdapter(adapterTypeOfVehicles);
        spinner.setAdapter(adapter);
        spinner_districts.setAdapter(adapterDistricts);
        nameText.setText(userDTO.getName());
        lastNameText.setText(userDTO.getLastName());
        if(userDTO.getPhoneNumber() != 0){
            phoneText.setText(String.valueOf(userDTO.getPhoneNumber()));
        }else {
            phoneText.setText("");
        }

        if("D".equals(userType)){
            spinnerFinalHour.setVisibility(View.GONE);
            spinnerInitialHour.setVisibility(View.GONE);
            actionRadio.setVisibility(View.GONE);
            textActionRadio.setVisibility(View.GONE);
            spinnerTypeVehicles.setVisibility(View.GONE);
            textTimeZone.setVisibility(View.GONE);
        }else {
            spinnerInitialHour.setSelection(adapterInitialHour.getPosition(userDTO.getInitialHour()));
            spinnerFinalHour.setSelection(adapterFinalHour.getPosition(userDTO.getFinalHour()));
            actionRadio.setProgress(userDTO.getActionRadio());
            spinnerTypeVehicles.setSelection(adapterTypeOfVehicles.getPosition(userDTO.getTypeOfVehicle()));
        }

        if(location != null){
            streetNameText.setText(location.getStreetName());
            buildingNumberText.setText(location.getBuildingNumber());
            floorText.setText(location.getFloor());
            doorText.setText(location.getDoor());
            cityText.setText(location.getCity());
            spinner.setSelection(adapter.getPosition(location.getNeighborhood()));
            spinner_districts.setSelection(adapterDistricts.getPosition(location.getDistrict()));
        }


        // Inflate the layout for this fragment
        return rootView;
    }

    public void updateUserInfo(LocationDTO locationDTO){

        /*long idLocation = userDTO.getIdLocation();
        if(idLocation != 0){
            locationDTO.setIdLocation(idLocation);
            cacheDbHelper.update(locationDTO, mDbHelper);
        }else {*/
        long idLocationNew = cacheDbHelper.insert(locationDTO, mDbHelper);
        userDTO.setIdLocation(idLocationNew);
        String phoneNumber = phoneText.getText().toString();
        userDTO.setPhoneNumber(Long.valueOf(phoneNumber));
        cacheDbHelper.update(userDTO, mDbHelper);
        //}

        int actionRadioValue = 0;
        String initialHour = null;
        String finalHour = null;
        String typeOfVehiclesValue = null;

        String userType = UserSession.getInstance(getContext()).getUserType();
        if(!"D".equals(userType)){
            actionRadioValue = actionRadio.getProgress();
            initialHour = spinnerInitialHour.getSelectedItem().toString();
            finalHour = spinnerFinalHour.getSelectedItem().toString();
            typeOfVehiclesValue = spinnerTypeVehicles.getSelectedItem().toString();

            userDTO.setActionRadio(actionRadioValue);
            userDTO.setInitialHour(initialHour);
            userDTO.setFinalHour(finalHour);
            userDTO.setTypeOfVehicle(typeOfVehiclesValue);

            cacheDbHelper.update(userDTO, mDbHelper);

            startActivity(new Intent(getContext(), MainReceptorActivity.class));
        }else {
            startActivity(new Intent(getContext(), MainDonateActivity.class));
        }
    }

    public void beginUpdateUserInfo(View view){

        addresToFind = streetNameText.getText().toString() + ", " + buildingNumberText.getText().toString() + ", " + cityText.getText().toString();
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
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
        Toast.makeText(getContext(), message,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        textActionRadio.setText(getString(R.string.radio_action) + ": " + progress + " km");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        seekBar.setProgress(initialActionRadio);
        textActionRadio.setText(getString(R.string.radio_action) + ": " + seekBar.getProgress() + " km");
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
