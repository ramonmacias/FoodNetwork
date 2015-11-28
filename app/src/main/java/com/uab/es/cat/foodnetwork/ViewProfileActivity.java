package com.uab.es.cat.foodnetwork;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.uab.es.cat.foodnetwork.database.CacheDbHelper;
import com.uab.es.cat.foodnetwork.database.FoodNetworkDbHelper;
import com.uab.es.cat.foodnetwork.dto.LocationDTO;
import com.uab.es.cat.foodnetwork.dto.UserDTO;
import com.uab.es.cat.foodnetwork.util.UserSession;
import com.uab.es.cat.foodnetwork.util.Utilities;

public class ViewProfileActivity extends AppCompatActivity {

    TextView textViewName;
    TextView textViewLastName;
    TextView textViewInitialHour;
    TextView textViewFinalHour;
    TextView textViewActionRadio;
    TextView textViewTypeVehicle;
    TextView textViewTmeZone;
    TextView textViewActionRadioTitle;
    TextView textViewTypeVehicleTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        textViewName = (TextView) findViewById(R.id.name);
        textViewLastName = (TextView) findViewById(R.id.lastName);
        textViewInitialHour = (TextView) findViewById(R.id.initial_hour);
        textViewFinalHour = (TextView) findViewById(R.id.final_hour);
        textViewActionRadio = (TextView) findViewById(R.id.actionRadio);
        textViewTypeVehicle = (TextView) findViewById(R.id.type_vehicles);
        textViewTmeZone = (TextView) findViewById(R.id.timeZone);
        textViewActionRadioTitle = (TextView) findViewById(R.id.actionRadioTitle);
        textViewTypeVehicleTitle = (TextView) findViewById(R.id.type_vehicles_title);

        UserDTO userDTO = new UserDTO();

        long userId = UserSession.getInstance(getApplicationContext()).getUserId();
        String userType = UserSession.getInstance(getApplicationContext()).getUserType();

        userDTO.setIdUser(userId);

        FoodNetworkDbHelper mDbHelper = new FoodNetworkDbHelper(getApplicationContext());

        CacheDbHelper cacheDbHelper = new CacheDbHelper();
        userDTO = (UserDTO) cacheDbHelper.getById(userDTO, mDbHelper);


        long idLocation = userDTO.getIdLocation();
        if(idLocation != 0){
            LocationDTO locationDTO = new LocationDTO();
            locationDTO.setIdLocation(idLocation);
            locationDTO = (LocationDTO) cacheDbHelper.getById(locationDTO, mDbHelper);
            TextView textViewNeighborhood = (TextView) findViewById(R.id.neighborhood);
            TextView textViewDistrict = (TextView) findViewById(R.id.district);
            TextView textViewAddress = (TextView) findViewById(R.id.address);
            textViewNeighborhood.setText(locationDTO.getNeighborhood());
            textViewDistrict.setText(locationDTO.getDistrict());
            textViewAddress.setText(Utilities.getFullAddress(locationDTO.getStreetName(), locationDTO.getBuildingNumber(), locationDTO.getFloor(), locationDTO.getDoor()));
        }

        textViewName.setText(userDTO.getName());
        textViewLastName.setText(userDTO.getLastName());

        if (!"D".equals(userType)){
            textViewActionRadio.setText(String.valueOf(userDTO.getActionRadio()));
            textViewInitialHour.setText(userDTO.getInitialHour());
            textViewFinalHour.setText(userDTO.getFinalHour());
            textViewTypeVehicle.setText(userDTO.getTypeOfVehicle());
        }else {
            textViewActionRadio.setVisibility(View.GONE);
            textViewInitialHour.setVisibility(View.GONE);
            textViewFinalHour.setVisibility(View.GONE);
            textViewTypeVehicle.setVisibility(View.GONE);
            textViewTmeZone.setVisibility(View.GONE);
            textViewTypeVehicleTitle.setVisibility(View.GONE);
            textViewActionRadioTitle.setVisibility(View.GONE);
        }

    }
}
