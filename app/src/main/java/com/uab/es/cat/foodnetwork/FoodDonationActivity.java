package com.uab.es.cat.foodnetwork;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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

public class FoodDonationActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView quantityTextView;
    private EditText productNameText;
    private int quantityCount;
    private List<FoodsDTO> foodsOfDonation;
    private CacheDbHelper cacheDbHelper;
    private FoodNetworkDbHelper mDbHelper;

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_food_donation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    public void addToDonation(View view){
        String productName = productNameText.getText().toString();

        FoodsDTO foodsDTO = new FoodsDTO();
        foodsDTO.setFoodName(productName);
        foodsDTO.setQuantity(quantityCount);

        foodsOfDonation.add(foodsDTO);

        quantityTextView.setText("1");
        productNameText.setText("");
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
