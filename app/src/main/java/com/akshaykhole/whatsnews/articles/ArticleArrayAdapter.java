package com.akshaykhole.whatsnews.articles;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.akshaykhole.whatsnews.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by akshay on 10/22/16.
 */

public class ArticleArrayAdapter extends ArrayAdapter<ArticlesModel> {

    private static class ArticleViewHolder {
        ImageView ivArticleMainImage;
        TextView tvArticleHeading;
    }

    public ArticleArrayAdapter(Context context, List<ArticlesModel> articles) {
        super(context, android.R.layout.simple_list_item_1, articles);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ArticlesModel article = getItem(position);
        ArticleViewHolder articleViewHolder;

        if(null == convertView) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.article_item, null);
            articleViewHolder = new ArticleViewHolder();
            articleViewHolder.ivArticleMainImage = (ImageView) convertView.findViewById(R.id.ivArticleMainImage);
            articleViewHolder.tvArticleHeading = (TextView) convertView.findViewById(R.id.tvArticleHeading);
            convertView.setTag(articleViewHolder);
        } else {
            articleViewHolder = (ArticleViewHolder) convertView.getTag();
        }

        if (article.getThumbNail() != "") {
            Log.d("DEBUG", article.getThumbNail());

            Picasso.with(getContext())
                    .load(article.getThumbNail())
                    .fit()
                    .into(articleViewHolder.ivArticleMainImage);
        }

        articleViewHolder.tvArticleHeading.setText(article.getHeadline());

        return convertView;
    }
}
