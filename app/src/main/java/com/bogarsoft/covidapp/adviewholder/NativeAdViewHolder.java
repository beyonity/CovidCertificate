package com.bogarsoft.covidapp.adviewholder;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.bogarsoft.covidapp.R;

import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAdView;


public class NativeAdViewHolder extends RecyclerView.ViewHolder {

    private NativeAdView adView;

    public NativeAdView getAdView() {
        return adView;
    }

    public NativeAdViewHolder(View view) {
        super(view);
        adView = (NativeAdView) view.findViewById( R.id.ad_view);

        // The MediaView will display a video asset if one is present in the ad, and the
        // first image asset otherwise.
        adView.setMediaView((MediaView) adView.findViewById(R.id.media_view));

        // Register the view used for each individual asset.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body_text));
        adView.setCallToActionView(adView.findViewById(R.id.add_call_to_action));
        adView.setIconView(adView.findViewById(R.id.adv_icon));
        adView.setStarRatingView(adView.findViewById(R.id.star_rating));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
    }
}
