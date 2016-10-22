package com.akshaykhole.whatsnews.articles;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by akshay on 10/22/16.
 */

public class ArticlesModel {
    String webUrl;
    String headline;
    String thumbNail;

    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadline() {
        return headline;
    }

    public String getThumbNail() {
        return thumbNail;
    }

    public ArticlesModel(JSONObject article) {
        try {
            this.webUrl = article.getString("web_url");
            this.headline = article.getJSONObject("headline").getString("main");
            JSONArray multimedia = article.getJSONArray("multimedia");

            if (multimedia.length() > 0) {
                JSONObject multimediaJSON = multimedia.getJSONObject(0);
                this.thumbNail = multimediaJSON.getString("url");
                this.thumbNail = ArticleSearchClient.baseUrl + this.thumbNail;
            } else {
                this.thumbNail = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<ArticlesModel> fromJSONArray(JSONArray array) {
        ArrayList<ArticlesModel> results = new ArrayList<>();

        try {
            for(int x = 0; x < array.length(); ++x) {
                results.add(new ArticlesModel(array.getJSONObject(x)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return results;
    }
}
