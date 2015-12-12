package com.uab.es.cat.foodnetwork;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.uab.es.cat.foodnetwork.util.UserSession;

public class MainDonateActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    public static final String LOGOUT = "com.uab.es.cat.foodnetwork.logout";

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_donate);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
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

            if("Google".equals(UserSession.getInstance(getApplicationContext()).getUserTypeLoggin())){
                signOut();
            }else {
                UserSession.getInstance(getApplicationContext()).logOut(getApplicationContext());
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void donate(View view){
        startActivity(new Intent(getApplicationContext(), FoodDonationActivity.class));
    }

    public void listDonations(View view){
        startActivity(new Intent(getApplicationContext(), DonationsActivity.class));
    }

    public void rankings(View view){
        startActivity(new Intent(getApplicationContext(), RankingsActivity.class));
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        // [END_EXCLUDE]
                    }
                });
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        //Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }
}
