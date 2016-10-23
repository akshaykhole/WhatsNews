package com.akshaykhole.whatsnews.articles;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by akshay on 10/22/16.
 */

public class ArticleSearchClient {

    public static final String articleApiKey = "84f5c1eb2a0e4968aed120ca5cdae1b6";
    public static final String articleSearchUrl = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
    public static final String baseUrl = "https://nytimes.com/";
    private AsyncHttpClient client;

    public ArticleSearchClient() {
        this.client = new AsyncHttpClient();
    }

    public void search(RequestParams params, JsonHttpResponseHandler handler) {
        Log.d("PARAMS", params.toString());
        client.get(articleSearchUrl, params, handler);
    }
}
