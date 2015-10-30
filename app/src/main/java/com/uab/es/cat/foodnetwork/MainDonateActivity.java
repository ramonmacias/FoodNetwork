package com.uab.es.cat.foodnetwork;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainDonateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_donate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_donate, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_view_profile) {
            startActivity(new Intent(getApplicationContext(), ViewProfileActivity.class));
            return true;
        }
        if (id == R.id.action_edit_profile) {
            startActivity(new Intent(getApplicationContext(), EditProfileActivity.class));
            return true;
        }
        if (id == R.id.action_disconnect) {

            // After logout redirect user to Loing Activity
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            getApplicationContext().startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void donate(View view){

    }

    public void listDonations(View view){

    }

    public void rankings(View view){

    }
}
