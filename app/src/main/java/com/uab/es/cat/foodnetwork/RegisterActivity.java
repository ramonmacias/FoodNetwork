package com.uab.es.cat.foodnetwork;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.uab.es.cat.foodnetwork.database.FoodNetworkDbHelper;
import com.uab.es.cat.foodnetwork.database.UserContract;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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

        FoodNetworkDbHelper mDbHelper = new FoodNetworkDbHelper(getApplicationContext());

        SQLiteDatabase dbRead = mDbHelper.getWritableDatabase();

        Cursor mCount= dbRead.rawQuery("select count(*) from users", null);
        mCount.moveToFirst();
        int count= mCount.getInt(0);
        mCount.close();

        dbRead.close();

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserContract.UserEntry.COLUMN_NAME_USER_ID, count + 1);
        values.put(UserContract.UserEntry.COLUMN_NAME_NAME, name);
        values.put(UserContract.UserEntry.COLUMN_NAME_LAST_NAME, lastName);
        values.put(UserContract.UserEntry.COLUMN_NAME_USER_NAME, nickName);
        values.put(UserContract.UserEntry.COLUMN_NAME_MAIL, mail);
        values.put(UserContract.UserEntry.COLUMN_NAME_PASSWORD, password);
        values.put(UserContract.UserEntry.COLUMN_NAME_USER_TYPE, "D");

        long newRowId;
        newRowId = db.insert(
                UserContract.UserEntry.TABLE_NAME,
                null,
                values);


        /*FoodNetworkDbHelper mDbHelper = new FoodNetworkDbHelper(getApplicationContext());

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                UserContract.UserEntry.COLUMN_NAME_USER_ID
        };

        String sortOrder = UserContract.UserEntry.COLUMN_NAME_USER_ID + " DESC";

        Cursor c = db.query(
                UserContract.UserEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        c.moveToFirst();
        long itemId = c.getLong(
                c.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_NAME_USER_ID)
        );*/

    }

}
