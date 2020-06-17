package android.example.newsapitest;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();


    //Empty constructor, it must be private
    private QueryUtils() {
    }

    public static List<CryptoInfo> fetchNewData(String requestUrl) {

        /*
        This thread causes the fetching of data to pause for 2 seconds odd
         */

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Create URL object. (Also have to create a method for 'createUrl'
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            //Create a jsonResponse class and assign to makeHttpRequest
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        //why have i created this.
        List<CryptoInfo> newsInfo = extractFeatureFromJson(jsonResponse);

        return newsInfo;
    }

    //this method takes in a string
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL", e);
        }
        return url;
    }

    //this method takes in the url from createUrl
    private static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {

                Log.e(LOG_TAG, "Problem retrieving the new info JSON result", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            }
            return jsonResponse;
    }


        private static String readFromStream (InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }


        private static List<CryptoInfo> extractFeatureFromJson(String newsInfoJson) {

        if (TextUtils.isEmpty(newsInfoJson)) {
            return null;
        }


        //we use the List<E> object and not ArrayList<E>
        //this JSON list only has one stage, as shown above, so we go straight into the sources arraylist, no stepping in

        List<CryptoInfo> newItems = new ArrayList<>();

        try {

            JSONObject jsonObject = new JSONObject(newsInfoJson);
            JSONArray arr  =jsonObject.getJSONArray("articles");

            //looping through features
            for (int x = 0; x< arr.length(); x++) {
                JSONObject currentInfo = arr.getJSONObject(x);

               String title = currentInfo.getString("title");            //this is json subject title - which is name
               String desc = currentInfo.getString("description");     //the name must be exactly the same as in the JSON info. e.g description or url
               String url = currentInfo.getString("url");               //i cannot use my own name such as weburl.
               String author = currentInfo.getString("author");

                /*
                String category;

                JSONArray tags = currentInfo.getJSONArray("category");
                if(tags != null && tags.length() > 0) {
                    JSONObject tagsObject = tags.getJSONObject(0);
                    category = tagsObject.optString("category", "No Category");

                } else category = "No category name";
                */

               CryptoInfo cryptodetail = new CryptoInfo(title, desc, url, author);
               newItems.add(cryptodetail);
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problems parsing Json", e);

        }
        return newItems;

    }



}
