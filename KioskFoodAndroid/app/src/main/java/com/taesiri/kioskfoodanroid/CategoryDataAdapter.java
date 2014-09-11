package com.taesiri.kioskfoodanroid;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

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

        ImageView thumbNail = (ImageView) convertView.findViewById(R.id.thumbnail);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView rating = (TextView) convertView.findViewById(R.id.rating);
        TextView description = (TextView) convertView.findViewById(R.id.description);

        FoodData f = foodItems.get(position);

        // thumbnail image
        // thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);

        title.setText(f.get_name());

        // rating
        //rating.setText("Rating: " + String.valueOf(m.getRating()));

        // description
        description.setText("Description");

        return convertView;
    }

}