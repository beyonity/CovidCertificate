package com.bogarsoft.covidcertificate.adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bogarsoft.covidcertificate.R;
import com.bogarsoft.covidcertificate.activites.NewsActivity;
import com.bogarsoft.covidcertificate.models.News;
import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    List<News> newsList;
    Activity activity;

    public NewsAdapter(List<News> newsList, Activity activity) {
        this.newsList = newsList;
        this.activity = activity;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

        News news = newsList.get(position);
        holder.source.setText(news.getSource());
        holder.title.setText(news.getTitle());
        holder.date.setText(news.getDate());
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent view = new Intent(activity, NewsActivity.class);
                view.putExtra("url",news.getUrl());
                view.putExtra("title",news.getSource());
                activity.startActivity(view);
            }
        });

        Glide.with(activity).load(news.getImage()).error(R.drawable.cover).into(holder.cover);


    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView cover;
        MaterialCardView card;
        TextView source,title,date;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.coverimage);
            source =itemView.findViewById(R.id.source);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            card = itemView.findViewById(R.id.card);
        }
    }
}
