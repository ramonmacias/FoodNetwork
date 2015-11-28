package com.uab.es.cat.foodnetwork;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.uab.es.cat.foodnetwork.database.CacheDbHelper;
import com.uab.es.cat.foodnetwork.database.FoodNetworkDbHelper;
import com.uab.es.cat.foodnetwork.dto.DonationDTO;
import com.uab.es.cat.foodnetwork.util.DonationForTransportArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class DonationsForTransportActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donations_for_transport);
        FoodNetworkDbHelper mDbHelper = new FoodNetworkDbHelper(getApplicationContext());

        CacheDbHelper cacheDbHelper = new CacheDbHelper();
        List<DonationDTO> donations = cacheDbHelper.getReadyAndCurrentDonations(mDbHelper);
        List<String> strings = new ArrayList<String>();

        for(DonationDTO donationDTO : donations){
            strings.add(String.valueOf(donationDTO.getIdDonation()));
        }

        DonationForTransportArrayAdapter adapter = new DonationForTransportArrayAdapter(this, strings, donations);
        setListAdapter(adapter);
    }

    public void startCollecting(View view){

    }
}
