package com.uab.es.cat.foodnetwork;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class DonationsActivity extends ListActivity {

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        //TUTORIAL: http://www.vogella.com/tutorials/AndroidListView/article.html
        String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2", "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.activity_donations, R.id.label, values);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String) getListAdapter().getItem(position);
        Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
    }
}
