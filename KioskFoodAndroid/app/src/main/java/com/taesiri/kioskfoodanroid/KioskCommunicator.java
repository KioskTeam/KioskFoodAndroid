package com.taesiri.kioskfoodanroid;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by MohammadReza on 9/1/2014.
 */
public class KioskCommunicator {

    private static final String TAG_RESTAURANT_NAME = "Name";
    private static final String TAG_RESTAURANT_ADDRESS = "Address";
    private static final String TAG_CATEGORIES = "Categories";
    private static final String TAG_NAME = "Name";
    private static final String TAG_IMAGE = "Image";
    private static final String TAG_FOODS = "Foods";
    private static final String TAG_PRICE = "Price";
    private static final String TAG_THUMBNAIL = "Thumbnail";
    private static final String TAG_PICTURES = "Pictures";

    public void fetchData () {
        KioskAsyncFetcher asyncDownloader = new KioskAsyncFetcher();
        asyncDownloader.execute(new String [] {"http://secure-scrubland-8071.herokuapp.com/api/latest"});
    }

    private class KioskAsyncFetcher extends AsyncTask<String, Void, RestaurantData> {
        private ProgressDialog dialog;

        public KioskAsyncFetcher(){

        }

        private KioskAsyncFetcher(ProgressDialog dialog) {
            this.dialog = dialog;
        }

        @Override
        protected void onPreExecute() {
            // TODO : Show Progress Dialog
        }

        @Override
        protected RestaurantData doInBackground(String... urls) {
            // Downloading json file
            String response = "";
            for (String url : urls) {
                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                try {
                    HttpResponse execute = client.execute(httpGet);
                    InputStream content = execute.getEntity().getContent();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }

            // Parsing Data
            RestaurantData restaurantData = new RestaurantData();

            if (response != null) {
                try {
                    JSONObject jsonObj = new JSONObject(response);

                    restaurantData.set_name(jsonObj.getString(TAG_RESTAURANT_NAME));
                    restaurantData.set_address(jsonObj.getString(TAG_RESTAURANT_ADDRESS));
                    JSONArray categoriesArray = jsonObj.getJSONArray(TAG_CATEGORIES);

                    for (int i = 0, n = categoriesArray.length(); i < n ; i++) {
                        CategoryData newCategory = new CategoryData();

                        JSONObject categoryObject = categoriesArray.getJSONObject(i);

                        newCategory.set_name(categoryObject.getString(TAG_NAME));
                        newCategory.set_imageUrl(categoryObject.getString(TAG_IMAGE));
                        JSONArray foodsArray = categoryObject.getJSONArray(TAG_FOODS);

                        FoodData[] foodsInNewCategory = new FoodData[foodsArray.length()];

                        for (int j = 0, m = foodsArray.length(); j < m ; j++) {
                            JSONObject foodObject = foodsArray.getJSONObject(j);
                            FoodData newFood = new FoodData();

                            newFood.set_name(foodObject.getString(TAG_NAME));
                            newFood.set_price(foodObject.getInt(TAG_PRICE));
                            newFood.set_thumbnailImageUrl(foodObject.getString(TAG_THUMBNAIL));
                            JSONArray imageUrlsArray = foodObject.getJSONArray(TAG_PICTURES);

                            String[] imageUrls = new String[imageUrlsArray.length()];

                            for (int k = 0, p = imageUrlsArray.length();k<p ;k++) {
                                imageUrls[k] = imageUrlsArray.getString(k);
                            }

                            newFood.set_imagesUrls(imageUrls);
                            foodsInNewCategory[j] = newFood;
                        }
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return  restaurantData;
        }


        @Override
        protected void onPostExecute(RestaurantData result) {
            // TODO : Hide Progress Dialog
            MyActivity.instance.tvContent.setText(result.get_name());
            //MyActivity.instance.dataReceived(ParseData(result));
        }
    }
}
