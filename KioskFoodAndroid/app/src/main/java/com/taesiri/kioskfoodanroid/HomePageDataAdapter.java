package com.taesiri.kioskfoodanroid;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.etsy.android.grid.util.DynamicHeightImageView;
import com.etsy.android.grid.util.DynamicHeightTextView;
import java.util.Random;
import java.util.concurrent.Callable;

/**
 * Created by MohammadReza on 9/2/2014.
 */
public class HomePageDataAdapter extends ArrayAdapter<CategoryData> {

    private static final String TAG = "HomePageDataAdapter";

    static class ViewHolder {
        DynamicHeightTextView txtLineOne;
        DynamicHeightImageView ivCategoryImage;
    }

    private final LayoutInflater mLayoutInflater;
    private final Random mRandom;
    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();

    public HomePageDataAdapter(final Context context, final int textViewResourceId) {
        super(context, textViewResourceId);
        mLayoutInflater = LayoutInflater.from(context);
        mRandom = new Random();
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final ViewHolder vh;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_item_category, parent, false);
            vh = new ViewHolder();
            vh.txtLineOne = (DynamicHeightTextView) convertView.findViewById(R.id.txt_line1);
            vh.ivCategoryImage = (DynamicHeightImageView) convertView.findViewById(R.id.dynamic_image_view);

            convertView.setTag(vh);
        }
        else {
            vh = (ViewHolder) convertView.getTag();
        }

        double positionHeight = getPositionRatio(position);

        vh.txtLineOne.setHeightRatio(positionHeight);
        vh.txtLineOne.setText(getItem(position).get_name());


        try {
            int index = getItem(position).get_imageUrl().lastIndexOf("/");
            final String imageKey = getItem(position).get_imageUrl().substring(index + 1);
            HomeActivity.instance.kCommunicator.getImage(  imageKey , new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    Bitmap catImage = HomeActivity.instance.kCommunicator.ImagePool.get(imageKey);
                    vh.ivCategoryImage.setImageBitmap(catImage);
                    return null;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

    private double getPositionRatio(final int position) {
        double ratio = sPositionHeightRatios.get(position, 0.0);
        // if not yet done generate and stash the columns height
        // in our real world scenario this will be determined by
        // some match based on the known height and width of the image
        // and maybe a helpful way to get the column height!
        if (ratio == 0) {
            ratio = getRandomHeightRatio();
            sPositionHeightRatios.append(position, ratio);
            Log.d(TAG, "getPositionRatio:" + position + " ratio:" + ratio);
        }
        return ratio;
    }

    private double getRandomHeightRatio() {
        return (mRandom.nextDouble() / 2.0) + 1.0; // height will be 1.0 - 1.5 the width
    }

}