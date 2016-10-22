package com.akshaykhole.whatsnews.articles;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.akshaykhole.whatsnews.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ArticleSearch extends AppCompatActivity {
    @BindView(R.id.btnSearch) Button btnSearch;
    @BindView(R.id.gvArticles) GridView gvArticles;
    @BindView(R.id.searchQuery) EditText searchQuery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_search);
        ButterKnife.bind(this);
    }

    @OnClick
    public void onArticleSearch(View view) {
        String query = searchQuery.getText().toString();
        ArticleSearchClient articleFetcher = new ArticleSearchClient();
        articleFetcher.search(query, 0);
        showToast("Searching for " + query);
    }

    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}
