package com.taesiri.kioskfoodanroid;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

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
    private static final String REMOTE_ASSET_DIRECTORY = "http://secure-scrubland-8071.herokuapp.com/assets/";
    private static final String REMOTE_API_URL = "http://secure-scrubland-8071.herokuapp.com/api/latest";
    private static final String ASSETS_FOLDER_NAME = "assets";
    private static final String DATA_FOLDER_NAME = "data";
    private static final String LOCAL_JSON_FILE_NAME = "data.json";


    public boolean forceUpdate;
    public RestaurantData restaurantData;
    public Map<String, Bitmap> ImagePool;
    private File assetsDirectory;
    private File dataDirectory;

    public KioskCommunicator(){
        ImagePool = new HashMap<String , Bitmap>();

        assetsDirectory = HomeActivity.instance.getApplicationContext().getDir(ASSETS_FOLDER_NAME, Context.MODE_PRIVATE);
        dataDirectory = HomeActivity.instance.getApplicationContext().getDir(DATA_FOLDER_NAME, Context.MODE_PRIVATE);

        forceUpdate = false;

        String[] localImages = assetsDirectory.list();
        for (int i = 0, n = localImages.length; i < n; i++){
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap lImage = BitmapFactory.decodeFile(assetsDirectory.getPath() + '/' + localImages[i], options);

                ImagePool.put(localImages[i],lImage );
            }
            catch (Exception exp) {
                exp.printStackTrace();
            }
        }
    }

    public void fetchData () {

        KioskAsyncFetcher asyncDownloader = new KioskAsyncFetcher();
        asyncDownloader.execute(new String[]{REMOTE_API_URL});
    }

    public void getImage(String imageKey, Callable<Void> callBackFunction) throws Exception {

        Log.i("MREZA", "GETTING the IMAGE!");

        if(ImagePool.get(imageKey)!= null)
        {
            callBackFunction.call();
            return;
        }

        KioskAsyncImageDownloader asyncImageDownloader = new KioskAsyncImageDownloader();
        asyncImageDownloader.CallBack = callBackFunction;
        asyncImageDownloader.execute(new String[] {REMOTE_ASSET_DIRECTORY + imageKey});
    }

    private class KioskAsyncFetcher extends AsyncTask<String, Void, RestaurantData> {
        private ProgressDialog pDialog;

        public KioskAsyncFetcher(){

        }

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(HomeActivity.instance);
            pDialog.setMessage("Refreshing ...");
            pDialog.show();
        }

        @Override
        protected RestaurantData doInBackground(String... urls) {
            String response = "";
            File localJsonFile = new File(dataDirectory + File.separator + "/" + LOCAL_JSON_FILE_NAME);
            RestaurantData restaurantData = new RestaurantData();

            if(localJsonFile.exists() && !forceUpdate) {
                StringBuilder text = new StringBuilder();
                try {
                    BufferedReader br = new BufferedReader(new FileReader(localJsonFile));
                    String line;

                    while ((line = br.readLine()) != null) {
                        text.append(line);
                        text.append('\n');
                    }
                }
                catch (IOException e) {
                    //You'll need to add proper error handling here
                }

                response = text.toString();
            }
            else {
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
                        return null;
                    }
                }

                if(response != null && response != "") {
                    FileOutputStream fOut = null;
                    try {
                        fOut = new FileOutputStream(localJsonFile, false);
                        OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                        myOutWriter.append(response);
                        myOutWriter.close();
                        fOut.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


            if (response != null) {

                try {
                    JSONObject jsonObj = new JSONObject(response);

                    restaurantData.set_name(jsonObj.getString(TAG_RESTAURANT_NAME));
                    restaurantData.set_address(jsonObj.getString(TAG_RESTAURANT_ADDRESS));
                    JSONArray categoriesArray = jsonObj.getJSONArray(TAG_CATEGORIES);

                    CategoryData[] restaurantCategories  = new CategoryData[categoriesArray.length()];

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
                            //newFood.set_price(foodObject.getInt(TAG_PRICE));
                            newFood.set_thumbnailImageUrl(foodObject.getString(TAG_THUMBNAIL));
                            JSONArray imageUrlsArray = foodObject.getJSONArray(TAG_PICTURES);

                            String[] imageUrls = new String[imageUrlsArray.length()];

                            for (int k = 0, p = imageUrlsArray.length();k<p ;k++) {
                                imageUrls[k] = imageUrlsArray.getString(k);
                            }

                            newFood.set_imagesUrls(imageUrls);
                            foodsInNewCategory[j] = newFood;
                        }
                        newCategory.set_foods(foodsInNewCategory);
                        restaurantCategories[i] = newCategory;
                    }

                    restaurantData.set_categories(restaurantCategories);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return  restaurantData;
        }


        @Override
        protected void onPostExecute(RestaurantData result) {
            restaurantData = result;
            hidePDialog();
            if (result != null) {
                HomeActivity.instance.dataReceived(result);
            }
        }

        private void hidePDialog() {
            if (pDialog != null) {
                pDialog.dismiss();
                pDialog = null;
            }
        }
    }


    private class KioskAsyncImageDownloader extends  AsyncTask<String, Void, Bitmap> {
        public Callable<Void> CallBack;
        @Override
        protected Bitmap doInBackground(String... urls) {
            for (String currentUrl : urls) {
                InputStream data = null;
                try {
                    URL url = new URL(currentUrl);
                    URLConnection urlConn = url.openConnection();
                    HttpURLConnection httpConn = (HttpURLConnection) urlConn;
                    httpConn.connect();

                    data = httpConn.getInputStream();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(data != null) {
                    Bitmap bImage = BitmapFactory.decodeStream(data);

                    int index = currentUrl.lastIndexOf("/");
                    String keyName = currentUrl.substring(index + 1);
                    ImagePool.put(keyName, bImage);

                    FileOutputStream out = null;
                    try {
                        out = new FileOutputStream(assetsDirectory.getPath() + '/' + keyName);
                        bImage.compress(Bitmap.CompressFormat.JPEG, 90, out);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    finally {
                        try {
                            if (out != null) {
                                out.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    return  bImage;
                }
            }
            return  null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            try {
                CallBack.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
