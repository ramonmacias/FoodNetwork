package com.uab.es.cat.foodnetwork;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class DonationsForTransportActivity extends ListActivity {

    DonationForTransportArrayAdapter adapter;
    Button startCollectingButton;
    List<DonationDTO> donations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donations_for_transport);

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
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        adapter.toggleSelected(new Integer(position));
        updateStartCollectingButtonStatus(adapter.selectedIds);
        setListAdapter(adapter);
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
}
