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
import android.widget.AdapterView;
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
import cz.msebera.android.httpclient.Header;

public class ArticleSearchActivity extends AppCompatActivity {
    Integer REQUEST_CODE = 1;
    @BindView(R.id.gvArticles) GridView gvArticles;

    private ArrayList<ArticlesModel> articles;
    private static RequestParams params;
    private static Integer page = 0;
    private static String searchQuery;
    private static SearchView searchView;
    private static final String oopsString = "Oops! Something went wrong. We are trying hard to fix it!";
    private static Intent intentFromFilter;
    ArticleArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_search);
        ButterKnife.bind(this);
        initialize();

        // Listen for scrolls
        gvArticles.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                loadMoreArticles(page);
                return true;
            }
        });
    }

    public void loadMoreArticles(int page) {
        Log.d("DEBUG", "Loading more..." + page);
        this.page = page - 1;
        setParamsForSearch();
        setSearchResults();
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
                setParamsForSearch();
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

        ArticleSearchClient articleFetcher = new ArticleSearchClient();
        articleFetcher.search(params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray articlesResponse = response.getJSONObject("response").getJSONArray("docs");
                    articles.addAll(ArticlesModel.fromJSONArray(articlesResponse));

                    if(articles.isEmpty()) {
                        showToast("No News found");
                    }

                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast(oopsString);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString,
                                  Throwable throwable) {
                showToast(oopsString);
            }
        });

        searchView.clearFocus();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Clear adapter after returning from filter activity
        adapter.clear();

        try {
            intentFromFilter = data;
            setParamsForSearch();
            setSearchResults();
        } catch (Exception e) {
            e.printStackTrace();
            showToast(oopsString);
        }
    }

    private void setParamsForSearch() {
        params.put("q", searchQuery);
        params.put("page", page);
        if (intentFromFilter != null) {
            if (!intentFromFilter.getStringExtra("startDate").equals("NULL")) {
                params.put("begin_date", intentFromFilter.getStringExtra("startDate"));
            }

            if (!intentFromFilter.getStringExtra("endDate").equals("NULL")) {
                params.put("end_date", intentFromFilter.getStringExtra("endDate"));
            }

            if (!intentFromFilter.getStringExtra("sortOrder").equals("NULL")) {
                params.put("sort", intentFromFilter.getStringExtra("sortOrder"));
            }

            if (!intentFromFilter.getStringExtra("newsDesk").equals("NULL")) {
                params.put("fq", intentFromFilter.getStringExtra("newsDesk"));
            }
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
        intentFromFilter = null;
        searchQuery = null;
        params.put("api-key", ArticleSearchClient.articleApiKey);
        params.put("page", page);
//        setParamsForSearch();
//        setSearchResults();

        // Click listener on GV

        gvArticles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent webviewIntent = new Intent(getApplicationContext(), ArticleViewActivity.class);
                ArticlesModel art = articles.get(i);
                webviewIntent.putExtra("url", art.getWebUrl());
                startActivity(webviewIntent);
            }
        });
    }
}
