package com.pravin.newscyclejava.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pravin.newscyclejava.API.Models.Article;
import com.pravin.newscyclejava.R;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    List<Article> articleList = new ArrayList<>();
    NewsOnclickListener listener;
    Context context;

    public NewsAdapter(NewsOnclickListener listener, Context context) {
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public NewsAdapter.NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item, parent, false);

        NewsViewHolder holder = new NewsViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.OnItemClickListener(articleList.get(holder.getAdapterPosition()));
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.NewsViewHolder holder, int position) {

        Article article = articleList.get(position);

        holder.title.setText(article.title);
        holder.author.setText(article.author);

        Glide.with(context)
                .load(article.urlToImage)
                .centerCrop()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                       // holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.image);


    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }


    class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView title,author;
        ImageView image;

        public NewsViewHolder(View view) {
            super(view);

            title = view.findViewById(R.id.title);
            author = view.findViewById(R.id.author);
            image = view.findViewById(R.id.image);
        }
}


   public void updateNews(List<Article> articles){
        articleList.clear();
        articleList.addAll(articles);
        notifyDataSetChanged();
    }


}

