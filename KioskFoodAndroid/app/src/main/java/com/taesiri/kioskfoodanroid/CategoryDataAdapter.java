package com.taesiri.kioskfoodanroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by MohammadReza on 9/2/2014.
 */
public class CategoryDataAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<FoodData> foodItems;

    public CategoryDataAdapter(Activity activity, List<FoodData> movieItems) {
        this.activity = activity;
        this.foodItems = movieItems;
    }

    @Override
    public int getCount() {
        return foodItems.size();
    }

    @Override
    public Object getItem(int location) {
        return foodItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);

        final ImageView thumbNail = (ImageView) convertView.findViewById(R.id.thumbnail);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        //TextView rating = (TextView) convertView.findViewById(R.id.rating);
        TextView description = (TextView) convertView.findViewById(R.id.description);

        final FoodData f = foodItems.get(position);

        title.setText(f.get_name());

        try {
            int index = foodItems.get(position).get_thumbnailImageUrl().lastIndexOf("/");
            final String imageKey = foodItems.get(position).get_thumbnailImageUrl().substring(index + 1);
            HomeActivity.instance.kCommunicator.getImage(  imageKey , new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    Bitmap foodThumbnail = HomeActivity.instance.kCommunicator.ImagePool.get(imageKey);
                    thumbNail.setImageBitmap(foodThumbnail);
                    return null;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        // rating
        //rating.setText("Rating: " + String.valueOf(m.getRating()));

        // description
        description.setText("Description");

        convertView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                Intent foodIntent = new Intent(activity, FoodActivity.class);
                FoodActivity.listOfImages = new ArrayList<String>(Arrays.asList( f.get_imagesUrls()));
                FoodActivity.CurrentData = f;
                activity.startActivity(foodIntent);

                Log.d("MREZA", "TOUCHED!");
                return false;
            }
        });

        return convertView;
    }

}