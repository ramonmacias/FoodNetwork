package com.uab.es.cat.foodnetwork;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.uab.es.cat.foodnetwork.database.CacheDbHelper;
import com.uab.es.cat.foodnetwork.database.FoodNetworkDbHelper;
import com.uab.es.cat.foodnetwork.database.UserContract;
import com.uab.es.cat.foodnetwork.dto.UserDTO;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private FoodNetworkDbHelper mDbHelper;
    private Spinner spinnerInitialHour;
    private Spinner spinnerFinalHour;
    private Spinner spinnerTypeVehicles;
    private SeekBar actionRadio;
    private TextView textActionRadio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findViewById(R.id.donor_user).setOnClickListener(this);
        findViewById(R.id.receptor_user).setOnClickListener(this);

        spinnerInitialHour = (Spinner) findViewById(R.id.initial_hour);
        spinnerFinalHour = (Spinner) findViewById(R.id.final_hour);
        spinnerTypeVehicles = (Spinner) findViewById(R.id.type_vehicles);
        actionRadio = (SeekBar) findViewById(R.id.actionRadio);
        textActionRadio = (TextView) findViewById(R.id.textActionRadio);

        actionRadio.setOnSeekBarChangeListener(this);

        ArrayAdapter<CharSequence> adapterInitialHour = ArrayAdapter.createFromResource(this,
                R.array.hours, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterFinalHour = ArrayAdapter.createFromResource(this,
                R.array.hours, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterTypeOfVehicles = ArrayAdapter.createFromResource(this,
                R.array.typeOfVehicles, android.R.layout.simple_spinner_item);

        adapterInitialHour.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterFinalHour.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterTypeOfVehicles.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerInitialHour.setAdapter(adapterInitialHour);
        spinnerFinalHour.setAdapter(adapterFinalHour);
        spinnerTypeVehicles.setAdapter(adapterTypeOfVehicles);

        spinnerFinalHour.setVisibility(View.GONE);
        spinnerInitialHour.setVisibility(View.GONE);
        actionRadio.setVisibility(View.GONE);
        textActionRadio.setVisibility(View.GONE);
        spinnerTypeVehicles.setVisibility(View.GONE);

        mDbHelper = new FoodNetworkDbHelper(getApplicationContext());
    }

    public void register(View view){
        EditText nameText = (EditText) findViewById(R.id.name);
        EditText lastNameText = (EditText) findViewById(R.id.lastName);
        EditText nickNameText = (EditText) findViewById(R.id.nickName);
        EditText mailText = (EditText) findViewById(R.id.mail);
        EditText passwordText = (EditText) findViewById(R.id.password);

        int actionRadioValue = 0;
        String initialHour = null;
        String finalHour = null;
        String typeOfVehiclesValue = null;
        String name = nameText.getText().toString();
        String lastName = lastNameText.getText().toString();
        String nickName = nickNameText.getText().toString();
        String mail = mailText.getText().toString();
        String password = passwordText.getText().toString();

        boolean donoUserChecked = ((RadioButton)findViewById(R.id.donor_user)).isChecked();
        boolean receptorUserChecked = ((RadioButton)findViewById(R.id.receptor_user)).isChecked();
        String userType = "D";

        if(receptorUserChecked){
            userType = "R";
            actionRadioValue = actionRadio.getProgress();
            initialHour = spinnerInitialHour.getSelectedItem().toString();
            finalHour = spinnerFinalHour.getSelectedItem().toString();
            typeOfVehiclesValue = spinnerTypeVehicles.getSelectedItem().toString();
        }


        UserDTO userDTO = new UserDTO();

        userDTO.setName(name);
        userDTO.setLastName(lastName);
        userDTO.setUserName(nickName);
        userDTO.setMail(mail);
        userDTO.setPassword(password);
        userDTO.setIdTypeUser(userType);
        userDTO.setTypeOfVehicle(typeOfVehiclesValue);
        userDTO.setInitialHour(initialHour);
        userDTO.setFinalHour(finalHour);
        userDTO.setActionRadio(actionRadioValue);

        CacheDbHelper cacheDbHelper = new CacheDbHelper();

        cacheDbHelper.insert(userDTO, mDbHelper);


        startActivity(new Intent(getApplicationContext(), LoginActivity.class));

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.donor_user:
                spinnerFinalHour.setVisibility(View.GONE);
                spinnerInitialHour.setVisibility(View.GONE);
                actionRadio.setVisibility(View.GONE);
                textActionRadio.setVisibility(View.GONE);
                spinnerTypeVehicles.setVisibility(View.GONE);
                break;
            case R.id.receptor_user:
                spinnerFinalHour.setVisibility(View.VISIBLE);
                spinnerInitialHour.setVisibility(View.VISIBLE);
                actionRadio.setVisibility(View.VISIBLE);
                textActionRadio.setVisibility(View.VISIBLE);
                spinnerTypeVehicles.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        textActionRadio.setText(getString(R.string.radio_action) + ": " + progress + " km");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
