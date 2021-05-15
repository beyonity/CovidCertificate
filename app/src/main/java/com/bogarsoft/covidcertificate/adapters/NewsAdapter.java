package com.bogarsoft.covidcertificate.adapters;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.RecyclerView;

import com.bogarsoft.covidcertificate.R;
import com.bogarsoft.covidcertificate.adviewholder.NativeAdViewHolder;
import com.bogarsoft.covidcertificate.models.News;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.material.card.MaterialCardView;


import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Object> newsList;
    Activity activity;

    private static final int MENU_ITEM_VIEW_TYPE = 0;

    // The unified native ad view type.
    private static final int UNIFIED_NATIVE_AD_VIEW_TYPE = 1;
    private static final String TAG = "NewsAdapter";
    public NewsAdapter(List<Object> newsList, Activity activity) {
        this.newsList = newsList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {



        switch (viewType){
            case UNIFIED_NATIVE_AD_VIEW_TYPE:
                View unifiedNativeLayoutView = LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.native_ad_layout,
                        parent, false);
                return new NativeAdViewHolder(unifiedNativeLayoutView);


            case MENU_ITEM_VIEW_TYPE:

            default:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list,parent,false);
                return new ViewHolder(view);
        }



    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType){
            case UNIFIED_NATIVE_AD_VIEW_TYPE:{
                NativeAd nativeAd = (NativeAd) newsList.get(position);
                populateNativeAd(nativeAd, ((NativeAdViewHolder) holder).getAdView());
                break;
            }
            case MENU_ITEM_VIEW_TYPE:{
                ViewHolder myViewHolder = (ViewHolder) holder;
                News news = (News)newsList.get(position);
                myViewHolder.source.setText(news.getSource());
                myViewHolder.title.setText(news.getTitle());


                SimpleDateFormat SDF = new SimpleDateFormat("dd MMM yyyy HH:mm a");
                //SDF.setTimeZone(TimeZone.getTimeZone("IST"));
                DateTime dateTime = new DateTime(news.getDate());
                Date date = new Date();
                date.setTime(dateTime.getMillis());
                // Log.d(TAG, "onBindViewHolder: "+date.getTime());
                String text = SDF.format(date);


                myViewHolder.date.setText(text);
                myViewHolder.card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                        CustomTabsIntent customTabsIntent = builder.build();
                        customTabsIntent.launchUrl(activity, Uri.parse(news.getUrl()));

                    }
                });

                Glide.with(activity).load(news.getImage()).error(R.drawable.cover).into(myViewHolder.cover);
            }
            default:
        }




    }

    @Override
    public int getItemViewType(int position) {
        Object viewItem = newsList.get(position);
        if(viewItem instanceof NativeAd){
            return UNIFIED_NATIVE_AD_VIEW_TYPE;
        }

        return MENU_ITEM_VIEW_TYPE;
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

    private void populateNativeAd(NativeAd nativeAd,NativeAdView nativeAdView){
        nativeAdView.setHeadlineView(nativeAdView.findViewById(R.id.ad_headline));
        nativeAdView.setAdvertiserView(nativeAdView.findViewById(R.id.ad_advertiser));
        nativeAdView.setBodyView(nativeAdView.findViewById(R.id.ad_body_text));
        nativeAdView.setStarRatingView(nativeAdView.findViewById(R.id.star_rating));
        nativeAdView.setMediaView(nativeAdView.findViewById(R.id.media_view));
        nativeAdView.setCallToActionView(nativeAdView.findViewById(R.id.add_call_to_action));
        nativeAdView.setIconView(nativeAdView.findViewById(R.id.adv_icon));


        nativeAdView.getMediaView().setMediaContent(nativeAd.getMediaContent());
        ((TextView)nativeAdView.getHeadlineView()).setText(nativeAd.getHeadline());

        if (nativeAd.getBody()==null){
            nativeAdView.getBodyView().setVisibility(View.INVISIBLE);
        }else {
            ((TextView)nativeAdView.getBodyView()).setText(nativeAd.getBody());
            nativeAdView.getBodyView().setVisibility(View.VISIBLE);

        }

        if (nativeAd.getAdvertiser() == null){
            nativeAdView.getAdvertiserView().setVisibility(View.INVISIBLE);
        }else {
            ((TextView)nativeAdView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            nativeAdView.getAdvertiserView().setVisibility(View.VISIBLE);
        }


        if (nativeAd.getStarRating() == null){
            nativeAdView.getStarRatingView().setVisibility(View.INVISIBLE);
        }else {
            ((RatingBar)nativeAdView.getStarRatingView()).setRating(nativeAd.getStarRating().floatValue());
            nativeAdView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getIcon() == null){
            nativeAdView.getIconView().setVisibility(View.INVISIBLE);
        }else {
            ((ImageView)nativeAdView.getIconView()).setImageDrawable(nativeAd.getIcon().getDrawable());
            nativeAdView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getCallToAction() == null){
            nativeAdView.getCallToActionView().setVisibility(View.INVISIBLE);
        }else {
            ((Button)nativeAdView.getCallToActionView()).setText(nativeAd.getCallToAction());
            nativeAdView.getCallToActionView().setVisibility(View.VISIBLE);
        }

        nativeAdView.setNativeAd(nativeAd);

    }
}
