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
import android.widget.EditText;
import android.widget.RadioButton;

import com.uab.es.cat.foodnetwork.database.CacheDbHelper;
import com.uab.es.cat.foodnetwork.database.FoodNetworkDbHelper;
import com.uab.es.cat.foodnetwork.database.UserContract;
import com.uab.es.cat.foodnetwork.dto.UserDTO;

public class RegisterActivity extends AppCompatActivity {

    FoodNetworkDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mDbHelper = new FoodNetworkDbHelper(getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_register, menu);
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

    public void register(View view){
        EditText nameText = (EditText) findViewById(R.id.name);
        EditText lastNameText = (EditText) findViewById(R.id.lastName);
        EditText nickNameText = (EditText) findViewById(R.id.nickName);
        EditText mailText = (EditText) findViewById(R.id.mail);
        EditText passwordText = (EditText) findViewById(R.id.password);

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
        }


        SQLiteDatabase dbRead = mDbHelper.getWritableDatabase();

        /*Cursor mCount= dbRead.rawQuery("select count(*) from users", null);
        mCount.moveToFirst();
        int count= mCount.getInt(0);
        mCount.close();

        dbRead.close();*/

        UserDTO userDTO = new UserDTO();

        //userDTO.setIdUser(count + 1);
        userDTO.setName(name);
        userDTO.setLastName(lastName);
        userDTO.setUserName(nickName);
        userDTO.setMail(mail);
        userDTO.setPassword(password);
        userDTO.setIdTypeUser(userType);

        CacheDbHelper cacheDbHelper = new CacheDbHelper();

        cacheDbHelper.insert(userDTO, mDbHelper);


        startActivity(new Intent(getApplicationContext(), LoginActivity.class));

    }

}
