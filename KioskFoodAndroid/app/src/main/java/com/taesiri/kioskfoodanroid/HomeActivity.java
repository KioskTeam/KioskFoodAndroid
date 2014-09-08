package com.taesiri.kioskfoodanroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.etsy.android.grid.StaggeredGridView;


public class HomeActivity extends Activity implements AbsListView.OnScrollListener, AbsListView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private static final String TAG = "HomeActivity";
    public static HomeActivity instance;
    private StaggeredGridView mGridView;
    private boolean mHasRequestedMore;
    private HomePageDataAdapter mAdapter;
    public static KioskCommunicator kCommunicator;
    private RestaurantData _currentData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setTitle("Browse Categories");
        mGridView = (StaggeredGridView) findViewById(R.id.grid_view);

        LayoutInflater layoutInflater = getLayoutInflater();

        View header = layoutInflater.inflate(R.layout.list_item_header_footer, null);
        View footer = layoutInflater.inflate(R.layout.list_item_header_footer, null);
        TextView txtHeaderTitle = (TextView) header.findViewById(R.id.txt_title);
        TextView txtFooterTitle = (TextView) footer.findViewById(R.id.txt_title);
        txtHeaderTitle.setText("HOME");
        txtFooterTitle.setText("THE FOOTER!");

        mGridView.addHeaderView(header);
        mGridView.addFooterView(footer);
        mAdapter = new HomePageDataAdapter(this, R.id.txt_line1);

        instance = this;

        kCommunicator = new KioskCommunicator();
        kCommunicator.fetchData();

        mGridView.setAdapter(mAdapter);
        mGridView.setOnScrollListener(this);
        mGridView.setOnItemClickListener(this);
        mGridView.setOnItemLongClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sgv_dynamic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.col1:
                mGridView.setColumnCount(1);
                break;
            case R.id.col2:
                mGridView.setColumnCount(2);
                break;
            case R.id.col3:
                mGridView.setColumnCount(3);
                break;
            case R.id.col4:
                kCommunicator.forceUpdate = true;
                kCommunicator.fetchData();
                // TODO : Refresh the grid
                break;
        }
        return true;
    }

    @Override
    public void onScrollStateChanged(final AbsListView view, final int scrollState) {
        Log.d(TAG, "onScrollStateChanged:" + scrollState);
    }

    @Override
    public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {
        Log.d(TAG, "onScroll firstVisibleItem:" + firstVisibleItem +
                " visibleItemCount:" + visibleItemCount +
                " totalItemCount:" + totalItemCount);
        // our handling
        if (!mHasRequestedMore) {
            int lastInScreen = firstVisibleItem + visibleItemCount;
            if (lastInScreen >= totalItemCount) {
                Log.d(TAG, "onScroll lastInScreen - so load more");
                mHasRequestedMore = true;
                onLoadMoreItems();
            }
        }
    }

    private void onLoadMoreItems() {
//        final ArrayList<String> sampleData = SampleData.generateSampleData();
//        for (String data : sampleData) {
//            mAdapter.add(data);
//        }
//        // stash all the data in our backing store
//        mData.addAll(sampleData);
//        // notify the adapter that we can update now
//        mAdapter.notifyDataSetChanged();
//        mHasRequestedMore = false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Toast.makeText(this, "Item Clicked: " + position, Toast.LENGTH_SHORT).show();

        if (_currentData != null) {
            if (position <= _currentData.get_categories().length) {
                Intent catIntent = new Intent(this, CategoryActivity.class);
                CategoryActivity.CurrentData = _currentData.get_categories()[position - 1];
                this.startActivity(catIntent);
            }
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "Item Long Clicked: " + position, Toast.LENGTH_SHORT).show();
        return true;
    }

    public void dataReceived(RestaurantData rData) {
        kCommunicator.forceUpdate = false;
        mAdapter.clear();

        for (CategoryData cat : rData.get_categories()) {
            mAdapter.add(cat);
        }
        _currentData = rData;
    }
}
