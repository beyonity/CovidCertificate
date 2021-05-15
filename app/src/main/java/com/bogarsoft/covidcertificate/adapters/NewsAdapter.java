package com.bogarsoft.covidcertificate.adapters;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.RecyclerView;

import com.bogarsoft.covidcertificate.R;
import com.bogarsoft.covidcertificate.models.News;
import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;


import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    List<Object> newsList;
    Activity activity;

    private static final String TAG = "NewsAdapter";
    public NewsAdapter(List<Object> newsList, Activity activity) {
        this.newsList = newsList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        News news = (News)newsList.get(position);
        holder.source.setText(news.getSource());
        holder.title.setText(news.getTitle());


        SimpleDateFormat SDF = new SimpleDateFormat("dd MMM yyyy HH:mm a");
        //SDF.setTimeZone(TimeZone.getTimeZone("IST"));
        DateTime dateTime = new DateTime(news.getDate());
        Date date = new Date();
        date.setTime(dateTime.getMillis());
       // Log.d(TAG, "onBindViewHolder: "+date.getTime());
        String text = SDF.format(date);


        holder.date.setText(text);
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(activity, Uri.parse(news.getUrl()));

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
        public ViewHolder(@NonNull  View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.coverimage);
            source =itemView.findViewById(R.id.source);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            card = itemView.findViewById(R.id.card);
        }
    }
}
