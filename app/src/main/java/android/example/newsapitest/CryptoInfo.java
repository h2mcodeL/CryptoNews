package android.example.newsapitest;

import java.net.URL;

/* This class holds the items for the unique structure within this app.
*
* The constructor uses overloading to present different configurations. */

/* The news API site has the following components description, author, title, url */


public class CryptoInfo {

    private String mTitle;
    private String mDesc;
    private String mUrl;
    private String mAuthor;
    private String mPublished;

    public CryptoInfo(String title, String desc, String url, String author, String published) {
        this.mTitle = title;
        this.mDesc = desc;
        this.mUrl = url;
        this.mAuthor = author;
        this.mPublished = published;
    }

    public CryptoInfo(String title, String url, String published) {
        this.mTitle = title;
        this.mUrl = url;
        this.mPublished = published;
    }

    public String getTitle() { return mTitle; }

    public String getDesc() { return mDesc; }

    public String getUrl() { return mUrl; }

    public String getAuthor() { return mAuthor; }

    public String getmPublished() { return mPublished;}
}
