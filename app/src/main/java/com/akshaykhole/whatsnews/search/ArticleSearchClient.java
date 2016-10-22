package com.akshaykhole.whatsnews.search;

import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by akshay on 10/22/16.
 */

public class ArticleSearchClient {

    private static final String articleApiKey = "84f5c1eb2a0e4968aed120ca5cdae1b6";
    private static final String articleSearchUrl = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
    private JSONArray articles;

    public JSONArray search(String query, Integer page) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("api-key", articleApiKey);
        params.put("page", page);
        params.put("q", query);

        client.get(articleSearchUrl, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    articles = response.getJSONObject("response").getJSONArray("docs");
                    Log.d("DEBUG", articles.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("DEBUG", responseString);
            }
        });

        return articles;
    }
}
