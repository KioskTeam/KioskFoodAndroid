package com.taesiri.kioskfoodanroid;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends Activity {
    // Log tag
    private static final String TAG = CategoryActivity.class.getSimpleName();

    public static CategoryData CurrentData;
    private ProgressDialog pDialog;
    private List<FoodData> foodList = new ArrayList<FoodData>();
    private ListView listView;
    private CategoryDataAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        listView = (ListView) findViewById(R.id.list);
        adapter = new CategoryDataAdapter(this, foodList);
        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        for(FoodData foodData:CurrentData.get_foods()) {

            foodList.add(foodData);
        }
        setTitle(CurrentData.get_name());

        hidePDialog();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

}