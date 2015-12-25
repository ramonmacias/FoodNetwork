package com.uab.es.cat.foodnetwork;

import android.app.ListActivity;
import android.content.Intent;
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
import com.uab.es.cat.foodnetwork.util.DonationForTransportArrayAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DonationsForTransportActivity extends AppCompatActivity {

    DonationForTransportArrayAdapter adapter;
    Button startCollectingButton;
    List<DonationDTO> donations;
    private ListView ItemsLst;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donations_for_transport);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ItemsLst = (ListView) findViewById(R.id.listview);

        startCollectingButton = (Button) findViewById(R.id.startCollectingId);
        FoodNetworkDbHelper mDbHelper = new FoodNetworkDbHelper(getApplicationContext());

        CacheDbHelper cacheDbHelper = new CacheDbHelper();
        donations = cacheDbHelper.getReadyAndCurrentDonations(mDbHelper);
        List<String> strings = new ArrayList<String>();

        for(DonationDTO donationDTO : donations){
            strings.add(String.valueOf(donationDTO.getIdDonation()));
        }
        startCollectingButton.setVisibility(View.GONE);
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

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOnBackPress();
            }
        });
    }

    public void updateStartCollectingButtonStatus(ArrayList selectedIds){
        if(selectedIds != null && selectedIds.size() > 0){
            startCollectingButton.setVisibility(View.VISIBLE);
        }else {
            startCollectingButton.setVisibility(View.GONE);
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
}
