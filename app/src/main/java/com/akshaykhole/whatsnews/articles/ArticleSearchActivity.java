package com.akshaykhole.whatsnews.articles;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.akshaykhole.whatsnews.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class ArticleSearchActivity extends AppCompatActivity {
    Integer REQUEST_CODE = 1;
    @BindView(R.id.gvArticles) GridView gvArticles;

    private ArrayList<ArticlesModel> articles;
    private static RequestParams params;
    private static Integer page = 0;
    private static String searchQuery;
    private static SearchView searchView;
    ArticleArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_search);
        ButterKnife.bind(this);
        initialize();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.article_search_action_bar, menu);

        // Set Filter Actions
        MenuItem filterMenuItem = menu.findItem(R.id.action_filter);

        filterMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent i = new Intent(ArticleSearchActivity.this, ArticleFilterActivity.class);
                startActivityForResult(i, REQUEST_CODE);
                return true;
            }
        });

        // Set Search actions
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery = query;
                setSearchResults();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    public void setSearchResults() {
        adapter.clear();

        ArticleSearchClient articleFetcher = new ArticleSearchClient();

        articleFetcher.search(params, page, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray articlesResponse = response.getJSONObject("response").getJSONArray("docs");
                    articles.addAll(ArticlesModel.fromJSONArray(articlesResponse));
                    adapter.notifyDataSetChanged();
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

        searchView.clearFocus();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            Log.d("DEBUGG", data.getStringExtra("startDate"));
            Log.d("DEBUGG", data.getStringExtra("endDate"));
            Log.d("DEBUGG", data.getStringExtra("sortOrder"));
            Log.d("DEBUGG", data.getStringExtra("newsDesk"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    private void initialize() {
        articles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(this, articles);
        gvArticles.setAdapter(adapter);
        params = new RequestParams();
        params.put("api-key", ArticleSearchClient.articleApiKey);
        params.put("page", page);
        params.put("q", searchQuery);
    }
}
