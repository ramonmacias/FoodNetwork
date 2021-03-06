package com.uab.es.cat.foodnetwork;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.uab.es.cat.foodnetwork.database.CacheDbHelper;
import com.uab.es.cat.foodnetwork.database.FoodNetworkDbHelper;
import com.uab.es.cat.foodnetwork.dto.DonationDTO;
import com.uab.es.cat.foodnetwork.util.DonationArrayAdapter;
import com.uab.es.cat.foodnetwork.util.UserSession;

import java.util.ArrayList;
import java.util.List;

import static com.uab.es.cat.foodnetwork.R.*;

public class DonationsActivity extends AppCompatActivity {

    public static final String DONATION_ID = "com.uab.es.cat.foodnetwork.DONATION_ID";
    private ListView ItemsLst;
    private Toolbar mToolbar;
    private FloatingActionButton floatingActionButton;

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(layout.activity_donations_list);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ItemsLst = (ListView) findViewById(R.id.listview);

        long userId = UserSession.getInstance(getApplicationContext()).getUserId();

        FoodNetworkDbHelper mDbHelper = new FoodNetworkDbHelper(getApplicationContext());

        CacheDbHelper cacheDbHelper = new CacheDbHelper();
        List<DonationDTO> donations = cacheDbHelper.getDonationsByUserId(userId, mDbHelper);
        List<String> strings = new ArrayList<String>();

        for(DonationDTO donationDTO : donations){
            strings.add(String.valueOf(donationDTO.getIdDonation()));
        }

        DonationArrayAdapter adapter = new DonationArrayAdapter(this, strings, donations);
        ItemsLst.setAdapter(adapter);

        ItemsLst.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> l, View v, int position,
                                    long id) {
                String item = (String) ItemsLst.getItemAtPosition(position);
                //String item = (String) getListAdapter().getItem(position);

                Intent intent = new Intent(getApplicationContext(), DonationDetailActivity.class);
                intent.putExtra(DONATION_ID, item);
                startActivity(intent);
                //Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();

            }
        });

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FoodDonationActivity.class));
            }
        });

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOnBackPress();
            }
        });
    }

    //@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String) ItemsLst.getItemAtPosition(position);
        //String item = (String) getListAdapter().getItem(position);

        Intent intent = new Intent(this, DonationDetailActivity.class);
        intent.putExtra(DONATION_ID, item);
        startActivity(intent);
        //Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
    }

    public void handleOnBackPress(){
        startActivity(new Intent(getApplicationContext(), MainDonateActivity.class));
    }
}
