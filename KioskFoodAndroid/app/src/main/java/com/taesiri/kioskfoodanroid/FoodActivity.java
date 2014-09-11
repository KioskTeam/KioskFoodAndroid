package com.taesiri.kioskfoodanroid;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class FoodActivity extends Activity {
    public static FoodData CurrentData;
    public static ArrayList<String> listOfImages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.food_pager);
        mViewPager.setAdapter(new FoodDataAdapter(this, listOfImages));

        TextView tvFoodTitle = (TextView) findViewById(R.id.food_name);

        tvFoodTitle.setText(CurrentData.get_name());
        setTitle(CurrentData.get_name());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.food, menu);
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
}
