package com.uab.es.cat.foodnetwork;

import android.app.ListActivity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.uab.es.cat.foodnetwork.database.CacheDbHelper;
import com.uab.es.cat.foodnetwork.database.FoodNetworkDbHelper;
import com.uab.es.cat.foodnetwork.dto.DonationDTO;
import com.uab.es.cat.foodnetwork.dto.UserDTO;
import com.uab.es.cat.foodnetwork.util.DonationForTransportArrayAdapter;
import com.uab.es.cat.foodnetwork.util.UserSession;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DonationsForTransportActivity extends AppCompatActivity {

    DonationForTransportArrayAdapter adapter;
    List<DonationDTO> donations;
    private ListView ItemsLst;
    private Toolbar mToolbar;
    private FloatingActionButton floatingActionButton;
    private FoodNetworkDbHelper mDbHelper;
    private CacheDbHelper cacheDbHelper;
    private UserDTO userDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donations_for_transport);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ItemsLst = (ListView) findViewById(R.id.listview);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);


        mDbHelper = new FoodNetworkDbHelper(getApplicationContext());
        cacheDbHelper = new CacheDbHelper();

        userDTO = getUserInfo();
        selectIconForFloatButton(userDTO);

        donations = cacheDbHelper.getReadyAndCurrentDonations(mDbHelper);
        List<String> strings = new ArrayList<String>();

        for(DonationDTO donationDTO : donations){
            strings.add(String.valueOf(donationDTO.getIdDonation()));
        }
        floatingActionButton.setVisibility(View.GONE);
        adapter = new DonationForTransportArrayAdapter(this, strings, donations);
        ItemsLst.setAdapter(adapter);

        ItemsLst.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> l, View v, int position,
                                    long id) {
                adapter.toggleSelected(new Integer(position));
                updateStartCollectingButtonStatus(adapter.selectedIds);
                ItemsLst.setAdapter(adapter);

            }
        });


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCollecting(v);
            }
        });

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOnBackPress();
            }
        });
    }

    public void updateStartCollectingButtonStatus(ArrayList selectedIds){
        if(selectedIds != null && selectedIds.size() > 0){
            floatingActionButton.setVisibility(View.VISIBLE);
        }else {
            floatingActionButton.setVisibility(View.GONE);
        }
    }

    public void startCollecting(View view){
        Intent intent = new Intent(this, CollectingDonationsActivity.class);
        intent.putExtra("ListOfDonationsSelected", (Serializable) adapter.selectedDonationDTO);
        startActivity(intent);
    }

    public void handleOnBackPress(){
        startActivity(new Intent(getApplicationContext(), MainReceptorActivity.class));
    }

    private UserDTO getUserInfo(){
        UserDTO userDTO = new UserDTO();
        userDTO.setIdUser(UserSession.getInstance(getApplicationContext()).getUserId());
        return (UserDTO) cacheDbHelper.getById(userDTO, mDbHelper);
    }

    private void selectIconForFloatButton(UserDTO userDTO){

        switch (userDTO.getTypeOfVehicle()){
            case "Moto":
                floatingActionButton.setImageResource(R.drawable.ic_motorcycle_black_24dp);
                break;
            case "Coche":
                floatingActionButton.setImageResource(R.drawable.ic_directions_car_black_24dp);
                break;
            case "Furgoneta":
                floatingActionButton.setImageResource(R.drawable.ic_airport_shuttle_black_24dp);
                break;
            case "Camion":
                floatingActionButton.setImageResource(R.drawable.ic_directions_bus_black_24dp);
                break;
        }
    }
}
