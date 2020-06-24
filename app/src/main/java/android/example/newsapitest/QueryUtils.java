package android.example.newsapitest;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;

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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static List<CryptoInfo> fetchNewData(String requestUrl) {

        /*
        This thread causes the fetching of data to pause for a few seconds
         */

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Create URL object.
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {

            //Create jsonResponse class and assign to makeHttpRequest
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        return extractFeatureFromJson(jsonResponse);
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
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static String readFromStream (InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
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

        //this JSON list only has one stage from the NewApi

        List<CryptoInfo> newItems = new ArrayList<>();

        try {

            JSONObject jsonObject = new JSONObject(newsInfoJson);
            JSONArray arr  =jsonObject.getJSONArray("articles");

            for (int x = 0; x< arr.length(); x++) {
                JSONObject currentInfo = arr.getJSONObject(x);

                String title = currentInfo.getString("title");
                String desc = currentInfo.getString("description");
                String url = currentInfo.getString("url");
                String author = currentInfo.getString("author");
                String published = currentInfo.getString("publishedAt");

                if(!author.equals("null") && author.length() > 0) {
                    author = currentInfo.getString("author");
                } else {
                    author = "No author provided";
                }

                CryptoInfo cryptodetail = new CryptoInfo(title, desc, url, author, published);
                newItems.add(cryptodetail);
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problems parsing Json", e);

        }
        return newItems;
    }
}