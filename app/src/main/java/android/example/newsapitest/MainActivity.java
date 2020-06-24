package android.example.newsapitest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * This app uses LoaderManager to build the URI request and JSON
 */
public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<CryptoInfo>> {

    /** TAG FOR TESTING LOG MESSAGES */
    private static final String LOG_TAG = MainActivity.class.getSimpleName();


    /* URI TO QUERY THE NEWS API WEBSITE DATASET */

   String url_response = "http://newsapi.org/v2/everything?";


    /* Loader ID */
    private static final int CRYPTO_INFO_LOADER = 1;

    /* This provides access to the unique ArrayAdapter */
    private CryptoAdapter mAdapter;

    /* TextView is is displayed when the list is empty */
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView newslist = (ListView) findViewById(R.id.listview);

        //SET UP THE EMPTY VIEW IF THERE IS NO DATA FOUND
        mEmptyStateTextView = findViewById(R.id.empty_view);
        newslist.setEmptyView(mEmptyStateTextView);

        //create a new adapter that takes an empty list of news items
        mAdapter = new CryptoAdapter(this, new ArrayList<CryptoInfo>());

        //Log.i(LOG_TAG, mAdapter + "news output");

        newslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //find the current newsitem that was clicked on
                CryptoInfo currentItem = mAdapter.getItem(position);

                /* We only have one item in our unique layout, so create a single String which is a url for the web link */
                assert currentItem != null;
                String news_url = currentItem.getUrl();
               // Log.i(LOG_TAG, "The new URL is: " + news_url);

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW);
                websiteIntent.setData(Uri.parse(news_url));

                startActivity(websiteIntent);
            }
        });


        //REFER to the Loader methods below for showing/hiding the loadingIndicator
        //set the adapter on the list
        newslist.setAdapter(mAdapter);

        //Check the state of the Network
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        //Get the details on the current active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        //If there is a network connection, fetch the data
        if(networkInfo != null && networkInfo.isConnected()) {

            //Get a reference to the loaderManager
            LoaderManager loaderManager = getSupportLoaderManager();
          //  Loader<List<CryptoInfo>> loaderManager = getSupportLoaderManager().initLoader(CRYPTO_INFO_LOADER, null, this);

            //Initialize the loader, pass in the int ID
            loaderManager.initLoader(CRYPTO_INFO_LOADER, null, this);
        } else {
            // else display error, hide the loading indicator to display the error msg
            View loadingIndicator = findViewById(R.id.progressBar);
            loadingIndicator.setVisibility(View.GONE);

            //Update the view
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

    }

    @NonNull
    @Override
    public Loader<List<CryptoInfo>> onCreateLoader(int id, @Nullable Bundle args) {

        //We create the items to store in the shared preference and also from the url structure
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        //this creates the Orderby view in the SharedPreference pane
        String orderFrom = sharedPreferences.getString(
                getString(R.string.settings_earliest_date_key),
                getString(R.string.settings_earliest_date_label));

        String subject = "cryptocurrency";
        String sortBy = "publishedAt";

        //this gets the url link to the API website
        Uri baseUri = Uri.parse(url_response);

        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("q", subject);
        uriBuilder.appendQueryParameter("from", orderFrom);
        uriBuilder.appendQueryParameter("sortBy",  sortBy);

        //you will have to register with newapi to get a apikey
        uriBuilder.appendQueryParameter("apiKey", "54d7a73eeb264a25887a8f8e5deb8f6d");

        Log.i(LOG_TAG, "API Search link: " + uriBuilder.toString());

        return new CryptoLoader(this, uriBuilder.toString());
    }




    @Override
    public void onLoadFinished(@NonNull Loader<List<CryptoInfo>> loader, List<CryptoInfo> data) {

        //First, show loading indicator whilst data is loading. Then hide indicator once data is loaded
        View loadingIndicator = findViewById(R.id.progressBar);
        loadingIndicator.setVisibility(View.GONE);

        //set empty state text to display "No items found"
        mEmptyStateTextView.setText(R.string.no_news);

        mAdapter.clear();

        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<CryptoInfo>> loader) {

        //Loader reset, so we can clear out our existing data
        mAdapter.clear();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

      switch(item.getItemId()) {

          case R.id.action_setting:
          Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
          startActivity(settingsIntent);
          break;

          case R.id.other_setting:

              Intent other_settings = new Intent(MainActivity.this, MenuActivity.class);
              startActivity(other_settings);
          return false;

      }

        return super.onOptionsItemSelected(item);
    }
}
