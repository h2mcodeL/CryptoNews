package android.example.newsapitest;

import java.net.URL;

public class CryptoInfo {

    private String mTitle;
    private String mUrl;

    public CryptoInfo(String title, String url) {
        this.mTitle = title;
        this.mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getUrl() { return mUrl;}
}
