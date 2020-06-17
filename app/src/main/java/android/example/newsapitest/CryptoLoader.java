package android.example.newsapitest;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

class CryptoLoader extends AsyncTaskLoader<List<CryptoInfo>> {

    private static final String LOG_TAG = CryptoLoader.class.getSimpleName();
    private String mURL;

    public CryptoLoader(@NonNull Context context, String url) {
        super(context);
        mURL = url;
    }

    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public List<CryptoInfo> loadInBackground() {
        if (mURL == null) {
            return null;
        }

        List<CryptoInfo> newsList = QueryUtils.fetchNewData(mURL);
        return newsList;
    }

}

