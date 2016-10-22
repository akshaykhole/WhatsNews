package com.akshaykhole.whatsnews.articles;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.akshaykhole.whatsnews.R;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class ArticleSearchActivity extends AppCompatActivity {
    @BindView(R.id.btnSearch) Button btnSearch;
    @BindView(R.id.gvArticles) GridView gvArticles;
    @BindView(R.id.searchQuery) EditText searchQuery;

    private ArrayList<ArticlesModel> articles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_search);
        ButterKnife.bind(this);

        articles = new ArrayList<>();
    }

    @OnClick
    public void onArticleSearch(View view) {
        String query = searchQuery.getText().toString();
        ArticleSearchClient articleFetcher = new ArticleSearchClient();

        articleFetcher.search(query, 0, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray articlesResponse = response.getJSONObject("response").getJSONArray("docs");
                    articles.addAll(ArticlesModel.fromJSONArray(articlesResponse));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast("Oops! Something went wrong. We are trying hard to fix it!");
                Log.d("DEBUG", responseString);
            }
        });
    }

    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}
