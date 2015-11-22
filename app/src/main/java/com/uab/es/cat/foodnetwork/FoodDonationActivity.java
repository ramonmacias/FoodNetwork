package com.uab.es.cat.foodnetwork;

import android.app.ListActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.uab.es.cat.foodnetwork.database.CacheDbHelper;
import com.uab.es.cat.foodnetwork.database.FoodNetworkDbHelper;
import com.uab.es.cat.foodnetwork.dto.DonationDTO;
import com.uab.es.cat.foodnetwork.dto.FoodsDTO;
import com.uab.es.cat.foodnetwork.dto.UserDTO;
import com.uab.es.cat.foodnetwork.util.UserSession;
import com.uab.es.cat.foodnetwork.util.Utilities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FoodDonationActivity extends ListActivity implements View.OnClickListener, OnMapReadyCallback {

    private TextView quantityTextView;
    private EditText productNameText;
    private int quantityCount;
    private List<FoodsDTO> foodsOfDonation;
    private CacheDbHelper cacheDbHelper;
    private FoodNetworkDbHelper mDbHelper;
    private MapFragment mapFragment;
    private GoogleMap map;

    /** Items entered by the user is stored in this ArrayList variable */
    ArrayList list = new ArrayList();

    /** Declaring an ArrayAdapter to set items to ListView */
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_donation);

        cacheDbHelper = new CacheDbHelper();
        mDbHelper = new FoodNetworkDbHelper(getApplicationContext());

        productNameText = (EditText) findViewById(R.id.productName);
        quantityTextView = (TextView) findViewById(R.id.quantity);
        quantityCount = 1;

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

        setListAdapter(adapter);

        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
    }

    public void addToDonation(View view){
        String productName = productNameText.getText().toString();

        FoodsDTO foodsDTO = new FoodsDTO();
        foodsDTO.setFoodName(productName);
        foodsDTO.setQuantity(quantityCount);

        foodsOfDonation.add(foodsDTO);

        list.add(String.valueOf(quantityCount) + " Kg/l " + productName);
        adapter.notifyDataSetChanged();

        quantityCount = 1;
        quantityTextView.setText("1");
        productNameText.setText("");
    }

    public void removeFromDonation(View view){

        /** Getting the checked items from the listview */
        SparseBooleanArray checkedItemPositions = getListView().getCheckedItemPositions();
        int itemCount = getListView().getCount();

        for(int i=itemCount-1; i >= 0; i--){
            if(checkedItemPositions.get(i)){
                adapter.remove(list.get(i));
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

        DonationDTO donationDTO = new DonationDTO();

        donationDTO.setIdLocation(userDTO.getIdLocation());
        donationDTO.setIdUser(userDTO.getIdUser());
        donationDTO.setInitialHour(initialHour);
        donationDTO.setFinalHour(finalHour);
        donationDTO.setState(1);
        donationDTO.setInsertDate(Utilities.dateToString(new Date()));

        Long idDonation = cacheDbHelper.insert(donationDTO, mDbHelper);
        for(FoodsDTO foodsDTO : foodsOfDonation){
            foodsDTO.setIdDonation(idDonation);
            cacheDbHelper.insert(foodsDTO, mDbHelper);

        }
        startActivity(new Intent(getApplicationContext(), MainDonateActivity.class));

    }
}
