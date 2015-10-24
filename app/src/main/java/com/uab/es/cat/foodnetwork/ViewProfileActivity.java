package com.uab.es.cat.foodnetwork;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.uab.es.cat.foodnetwork.database.CacheDbHelper;
import com.uab.es.cat.foodnetwork.database.FoodNetworkDbHelper;
import com.uab.es.cat.foodnetwork.dto.UserDTO;

public class ViewProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserDTO userDTO = new UserDTO();

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.user_type), Context.MODE_PRIVATE);
        int defaultValue = 0;
        int userId = sharedPref.getInt(getString(R.string.user_id), defaultValue);

        userDTO.setIdUser(userId);

        FoodNetworkDbHelper mDbHelper = new FoodNetworkDbHelper(getApplicationContext());

        CacheDbHelper cacheDbHelper = new CacheDbHelper();
        userDTO = (UserDTO) cacheDbHelper.getById(userDTO, mDbHelper);

        setContentView(R.layout.activity_view_profile);
        TextView textView = (TextView) findViewById(R.id.name);
        textView.setText(userDTO.getName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_profile, menu);
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
}
