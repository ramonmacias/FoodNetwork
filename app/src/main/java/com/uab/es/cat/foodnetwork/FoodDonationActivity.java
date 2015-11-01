package com.uab.es.cat.foodnetwork;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.uab.es.cat.foodnetwork.database.CacheDbHelper;
import com.uab.es.cat.foodnetwork.database.FoodNetworkDbHelper;
import com.uab.es.cat.foodnetwork.dto.DonationDTO;
import com.uab.es.cat.foodnetwork.dto.FoodsDTO;
import com.uab.es.cat.foodnetwork.dto.UserDTO;
import com.uab.es.cat.foodnetwork.util.UserSession;

public class FoodDonationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_donation);
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

    public void donate(View view){

        EditText productNameText = (EditText) findViewById(R.id.productName);

        String productName = productNameText.getText().toString();

        FoodsDTO foodsDTO = new FoodsDTO();
        foodsDTO.setFoodName(productName);
        foodsDTO.setExpirationDate("07/08/2016");
        foodsDTO.setFoodType("Lactico");
        foodsDTO.setQuantity(2);

        CacheDbHelper cacheDbHelper = new CacheDbHelper();
        FoodNetworkDbHelper mDbHelper = new FoodNetworkDbHelper(getApplicationContext());

        long idFood = cacheDbHelper.insert(foodsDTO, mDbHelper);

        long userId = UserSession.getInstance(getApplicationContext()).getUserId();

        UserDTO userDTO = new UserDTO();
        userDTO.setIdUser(userId);

        userDTO = (UserDTO) cacheDbHelper.getById(userDTO, mDbHelper);

        DonationDTO donationDTO = new DonationDTO();

        donationDTO.setIdFood(idFood);
        donationDTO.setIdLocation(userDTO.getIdLocation());
        donationDTO.setIdUser(userDTO.getIdUser());
        donationDTO.setState(1);

        cacheDbHelper.insert(donationDTO, mDbHelper);

        startActivity(new Intent(getApplicationContext(), MainDonateActivity.class));

    }
}
