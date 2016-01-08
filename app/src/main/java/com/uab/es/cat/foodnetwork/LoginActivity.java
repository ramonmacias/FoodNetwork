package com.uab.es.cat.foodnetwork;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener{

    private static final String TAG = "LoginActivity";
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private static int FB_SIGN_IN = 0;
    private Toolbar mToolbar;
    private LoginButton loginButton;
    private ProfileTracker mProfileTracker;

    private FoodNetworkDbHelper mDbHelper;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);


        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday, user_friends"));
        FB_SIGN_IN = loginButton.getRequestCode();

        callbackManager = CallbackManager.Factory.create();

        boolean isLoggedByFacebook = isLoggedIn();


        loginButton.registerCallback(callbackManager, mFacebookCallback);

        if(isLoggedByFacebook){
            manageLoggdeUser();
        }

        manageKeyHashes();
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
            UserSession.getInstance(getApplicationContext()).logIn(userId, userType);
            UserSession.getInstance(getApplicationContext()).setUserTypeLoggin("Mail");
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
        if (requestCode == FB_SIGN_IN){
            callbackManager.onActivityResult(requestCode, resultCode, data);
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
                UserSession.getInstance(getApplicationContext()).logIn(userId, userType);
                UserSession.getInstance(getApplicationContext()).setUserTypeLoggin("Google");
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

                UserSession.getInstance(getApplicationContext()).logIn(newUserId, "");
                UserSession.getInstance(getApplicationContext()).setUserTypeLoggin("Google");

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


        UserSession.getInstance(getApplicationContext()).logIn(userDTO.getIdUser(), userDTO.getIdTypeUser());

        startActivity(new Intent(getApplicationContext(), MainDonateActivity.class));
    }

    public void transporter(View view){
        UserDTO userDTO = new UserDTO();
        userDTO.setIdUser(UserSession.getInstance(getApplicationContext()).getUserId());

        CacheDbHelper cacheDbHelper = new CacheDbHelper();
        userDTO = (UserDTO) cacheDbHelper.getById(userDTO, mDbHelper);
        userDTO.setIdTypeUser("R");

        cacheDbHelper.update(userDTO, mDbHelper);


        UserSession.getInstance(getApplicationContext()).logIn(userDTO.getIdUser(), userDTO.getIdTypeUser());

        startActivity(new Intent(getApplicationContext(), MainReceptorActivity.class));
    }

    @Override
    public void onBackPressed()
    {

    }


    private FacebookCallback<LoginResult> mFacebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            if(Profile.getCurrentProfile() == null) {
                mProfileTracker = new ProfileTracker() {
                    @Override
                    protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                        String firstName = profile2.getFirstName();
                        String lastName = profile2.getLastName();
                        String facebookUserId = profile2.getId();


                        SQLiteDatabase db = mDbHelper.getReadableDatabase();

                        Cursor mCount= db.rawQuery("select count(*), usertype, userid from users where mail='" + facebookUserId + "'", null);
                        mCount.moveToFirst();
                        int count= mCount.getInt(0);
                        String userType = mCount.getString(1);
                        int userId = mCount.getInt(2);
                        mCount.close();

                        if(count > 0){
                            UserSession.getInstance(getApplicationContext()).logIn(userId, userType);
                            UserSession.getInstance(getApplicationContext()).setUserTypeLoggin("Facebook");
                            if("D".equals(userType)){
                                startActivity(new Intent(getApplicationContext(), MainDonateActivity.class));
                            }else {
                                startActivity(new Intent(getApplicationContext(), MainReceptorActivity.class));
                            }
                        }else {
                            UserDTO userDTO = new UserDTO();


                            userDTO.setName(firstName);
                            userDTO.setLastName(lastName);
                            userDTO.setMail(facebookUserId);

                            CacheDbHelper cacheDbHelper = new CacheDbHelper();
                            long newUserId = cacheDbHelper.insert(userDTO, mDbHelper);

                            UserSession.getInstance(getApplicationContext()).logIn(newUserId, "");
                            UserSession.getInstance(getApplicationContext()).setUserTypeLoggin("Facebook");

                            setContentView(R.layout.type_user);
                        }

                        mProfileTracker.stopTracking();
                    }
                };
                mProfileTracker.startTracking();
            }
            else {
                Profile profile = Profile.getCurrentProfile();

                String firstName = profile.getFirstName();
                String lastName = profile.getLastName();
                String facebookUserId = profile.getId();


                SQLiteDatabase db = mDbHelper.getReadableDatabase();

                Cursor mCount= db.rawQuery("select count(*), usertype, userid from users where mail='" + facebookUserId + "'", null);
                mCount.moveToFirst();
                int count= mCount.getInt(0);
                String userType = mCount.getString(1);
                int userId = mCount.getInt(2);
                mCount.close();

                if(count > 0){
                    UserSession.getInstance(getApplicationContext()).logIn(userId, userType);
                    UserSession.getInstance(getApplicationContext()).setUserTypeLoggin("Facebook");
                    if("D".equals(userType)){
                        startActivity(new Intent(getApplicationContext(), MainDonateActivity.class));
                    }else {
                        startActivity(new Intent(getApplicationContext(), MainReceptorActivity.class));
                    }
                }else {
                    UserDTO userDTO = new UserDTO();


                    userDTO.setName(firstName);
                    userDTO.setLastName(lastName);
                    userDTO.setMail(facebookUserId);

                    CacheDbHelper cacheDbHelper = new CacheDbHelper();
                    long newUserId = cacheDbHelper.insert(userDTO, mDbHelper);

                    UserSession.getInstance(getApplicationContext()).logIn(newUserId, "");
                    UserSession.getInstance(getApplicationContext()).setUserTypeLoggin("Facebook");

                    setContentView(R.layout.type_user);
                }
            }
        }


        @Override
        public void onCancel() {
            updateUI(false);
        }

        @Override
        public void onError(FacebookException e) {
            updateUI(false);
        }
    };

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    public void manageLoggdeUser() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if(accessToken != null){
            String facebookUserId = accessToken.getUserId();
            SQLiteDatabase db = mDbHelper.getReadableDatabase();

            Cursor mCount= db.rawQuery("select count(*), usertype, userid from users where mail='" + facebookUserId + "'", null);
            mCount.moveToFirst();
            int count= mCount.getInt(0);
            String userType = mCount.getString(1);
            int userId = mCount.getInt(2);
            mCount.close();

            if(count > 0){
                UserSession.getInstance(getApplicationContext()).logIn(userId, userType);
                UserSession.getInstance(getApplicationContext()).setUserTypeLoggin("Facebook");
                if("D".equals(userType)){
                    startActivity(new Intent(getApplicationContext(), MainDonateActivity.class));
                }else {
                    startActivity(new Intent(getApplicationContext(), MainReceptorActivity.class));
                }
            }
        }
    }

    public void manageKeyHashes(){
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                Log.i("LoginActivity", key);

            }
        } catch (PackageManager.NameNotFoundException e1) {
            updateUI(false);
        }
        catch (NoSuchAlgorithmException e) {
            updateUI(false);
        } catch (Exception e) {
            updateUI(false);
        }
    }

}
