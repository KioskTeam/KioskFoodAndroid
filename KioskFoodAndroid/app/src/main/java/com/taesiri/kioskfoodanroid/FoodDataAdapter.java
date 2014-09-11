package com.taesiri.kioskfoodanroid;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.Callable;

/**
 * Created by MohammadReza on 9/11/2014.
 */
public class FoodDataAdapter extends PagerAdapter{
    private Activity _activity;
    private ArrayList<String> _imagePaths;
    private LayoutInflater inflater;

    // constructor
    public FoodDataAdapter(Activity activity, ArrayList<String> imagePaths) {
        this._activity = activity;
        this._imagePaths = imagePaths;
    }

    @Override
    public int getCount() {
        return this._imagePaths.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final ImageView imgDisplay;

        inflater = (LayoutInflater) _activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.layout_fullscreen_food_image, container,false);
        imgDisplay = (ImageView) viewLayout.findViewById(R.id.foodImage);

        try {
            int index = _imagePaths.get(position).lastIndexOf("/");
            final String imageKey = _imagePaths.get(position).substring(index + 1);
            HomeActivity.instance.kCommunicator.getImage(  imageKey , new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    Bitmap foodThumbnail = HomeActivity.instance.kCommunicator.ImagePool.get(imageKey);
                    imgDisplay.setImageBitmap(foodThumbnail);
                    return null;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }
}
