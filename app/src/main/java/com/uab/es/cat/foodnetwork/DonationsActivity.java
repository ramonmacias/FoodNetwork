package com.uab.es.cat.foodnetwork;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.uab.es.cat.foodnetwork.database.CacheDbHelper;
import com.uab.es.cat.foodnetwork.database.FoodNetworkDbHelper;
import com.uab.es.cat.foodnetwork.dto.DonationDTO;
import com.uab.es.cat.foodnetwork.util.DonationArrayAdapter;
import com.uab.es.cat.foodnetwork.util.UserSession;

import java.util.ArrayList;
import java.util.List;

import static com.uab.es.cat.foodnetwork.R.*;

public class DonationsActivity extends ListActivity {

    public static final String DONATION_ID = "com.uab.es.cat.foodnetwork.DONATION_ID";

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        long userId = UserSession.getInstance(getApplicationContext()).getUserId();

        FoodNetworkDbHelper mDbHelper = new FoodNetworkDbHelper(getApplicationContext());

        CacheDbHelper cacheDbHelper = new CacheDbHelper();
        List<DonationDTO> donations = cacheDbHelper.getDonationsByUserId(userId, mDbHelper);
        List<String> strings = new ArrayList<String>();

        for(DonationDTO donationDTO : donations){
            strings.add(String.valueOf(donationDTO.getIdDonation()));
        }

        DonationArrayAdapter adapter = new DonationArrayAdapter(this, strings, donations);

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        //        layout.activity_donations, id.label, strings);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String) getListAdapter().getItem(position);

        Intent intent = new Intent(this, DonationDetailActivity.class);
        intent.putExtra(DONATION_ID, item);
        startActivity(intent);
        //Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
    }
}
