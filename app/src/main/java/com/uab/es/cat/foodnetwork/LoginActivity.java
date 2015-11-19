package com.uab.es.cat.foodnetwork;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.uab.es.cat.foodnetwork.database.CacheDbHelper;
import com.uab.es.cat.foodnetwork.database.FoodNetworkDbHelper;
import com.uab.es.cat.foodnetwork.dto.UserDTO;
import com.uab.es.cat.foodnetwork.util.UserSession;

public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener{

    private static final String TAG = "LoginActivity";
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;

    private FoodNetworkDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mDbHelper = new FoodNetworkDbHelper(getApplicationContext());


        findViewById(R.id.button_sign_in).setOnClickListener(this);
        findViewById(R.id.email_sign_in_button).setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton signInButton = (SignInButton) findViewById(R.id.button_sign_in);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_sign_in:
                signIn();
                break;
            case R.id.email_sign_in_button:
                signInByEmail();
                break;
        }
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }

    private void updateUI(boolean signedIn) {
        if (signedIn) {
            Toast.makeText(this, "El login ha sido correcto", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Se ha producido un error en el login", Toast.LENGTH_LONG).show();
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signInByEmail(){

        TextView mEmailView = (TextView)findViewById(R.id.email);
        TextView mPasswordView = (TextView)findViewById(R.id.password);

        String mEmail = mEmailView.getText().toString();
        String mPassword = mPasswordView.getText().toString();

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor mCount= db.rawQuery("select count(*), usertype, userid from users where mail='" + mEmail + "' and password='" + mPassword + "'", null);
        mCount.moveToFirst();
        int count= mCount.getInt(0);
        String userType = mCount.getString(1);
        int userId = mCount.getInt(2);
        mCount.close();

        if(count > 0){
            UserSession.getInstance(getApplicationContext()).logIn(userId, userType, "Mail");
            if("D".equals(userType)){
                startActivity(new Intent(getApplicationContext(), MainDonateActivity.class));
            }else {
                startActivity(new Intent(getApplicationContext(), MainReceptorActivity.class));
            }
        }else {
            updateUI(false);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            String email = acct.getEmail();
            String name = acct.getDisplayName();


            SQLiteDatabase db = mDbHelper.getReadableDatabase();

            Cursor mCount= db.rawQuery("select count(*), usertype, userid from users where mail='" + email + "'", null);
            mCount.moveToFirst();
            int count= mCount.getInt(0);
            String userType = mCount.getString(1);
            int userId = mCount.getInt(2);
            mCount.close();

            if(count > 0){
                UserSession.getInstance(getApplicationContext()).logIn(userId, userType, "Google");
                if("D".equals(userType)){
                    startActivity(new Intent(getApplicationContext(), MainDonateActivity.class));
                }else {
                    startActivity(new Intent(getApplicationContext(), MainReceptorActivity.class));
                }
            }else {
                UserDTO userDTO = new UserDTO();

                String[] longName = name.split(" ");

                userDTO.setName(longName[0]);
                userDTO.setLastName(longName[1]);
                userDTO.setMail(email);

                CacheDbHelper cacheDbHelper = new CacheDbHelper();
                long newUserId = cacheDbHelper.insert(userDTO, mDbHelper);

                UserSession.getInstance(getApplicationContext()).logIn(newUserId, "", "Google");

                setContentView(R.layout.type_user);
            }
        } else {
            updateUI(false);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    public void register(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void donador(View view){

        UserDTO userDTO = new UserDTO();
        userDTO.setIdUser(UserSession.getInstance(getApplicationContext()).getUserId());

        CacheDbHelper cacheDbHelper = new CacheDbHelper();
        userDTO = (UserDTO) cacheDbHelper.getById(userDTO, mDbHelper);
        userDTO.setIdTypeUser("D");

        cacheDbHelper.update(userDTO, mDbHelper);


        UserSession.getInstance(getApplicationContext()).logIn(userDTO.getIdUser(), userDTO.getIdTypeUser(), "Google");

        startActivity(new Intent(getApplicationContext(), MainDonateActivity.class));
    }

    public void transporter(View view){
        UserDTO userDTO = new UserDTO();
        userDTO.setIdUser(UserSession.getInstance(getApplicationContext()).getUserId());

        CacheDbHelper cacheDbHelper = new CacheDbHelper();
        userDTO = (UserDTO) cacheDbHelper.getById(userDTO, mDbHelper);
        userDTO.setIdTypeUser("R");

        cacheDbHelper.update(userDTO, mDbHelper);


        UserSession.getInstance(getApplicationContext()).logIn(userDTO.getIdUser(), userDTO.getIdTypeUser(), "Google");

        startActivity(new Intent(getApplicationContext(), MainReceptorActivity.class));
    }

    @Override
    public void onBackPressed()
    {

    }
}
