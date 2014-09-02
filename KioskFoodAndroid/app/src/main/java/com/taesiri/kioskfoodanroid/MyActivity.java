package com.taesiri.kioskfoodanroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MyActivity extends Activity {
    public static MyActivity instance;

    private TextView tvRestaurantName;
    private TextView tvRestaurantAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        instance = this;

        tvRestaurantName = (TextView) findViewById(R.id.tvRestaurantName);
        tvRestaurantName.setText("...");

        tvRestaurantAddress = (TextView) findViewById(R.id.tvRestaurantAddress);
        tvRestaurantAddress.setText("...");

        KioskCommunicator kCommunicator = new KioskCommunicator();
        kCommunicator.fetchData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void dataReceived(RestaurantData data) {
        tvRestaurantName.setText(data.get_name());
        tvRestaurantAddress.setText(data.get_address());
    }
}
