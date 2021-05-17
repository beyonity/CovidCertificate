package com.bogarsoft.covidapp.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bogarsoft.covidapp.R;
import com.bogarsoft.covidapp.models.Bed;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class BedAdapter extends RecyclerView.Adapter<BedAdapter.ViewHolder> {

        List<Bed> bedList;
        Activity activity;


    public BedAdapter(List<Bed> bedList, Activity activity) {
        this.bedList = bedList;
        this.activity = activity;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bed_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BedAdapter.ViewHolder holder, int position) {
        Bed bed = bedList.get(position);
        holder.state.setText(bed.getName());
        holder.ruralhos.setText(bed.getRuralhos());
        holder.ruralbeds.setText(bed.getRuralbeds());
        holder.urbanbeds.setText(bed.getUrbanbeds());
        holder.urbanhos.setText(bed.getUrbanhos());

        if (position %2==0){
            holder.card.setBackgroundColor(activity.getResources().getColor(R.color.Platinum));
        }else {
            holder.card.setBackgroundColor(activity.getResources().getColor(R.color.white));

        }

    }

    @Override
    public int getItemCount() {
        return bedList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView state,ruralhos,ruralbeds,urbanhos,urbanbeds;
        MaterialCardView card;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            state = itemView.findViewById(R.id.state);
            ruralhos = itemView.findViewById(R.id.ruralhospitals);
            ruralbeds = itemView.findViewById(R.id.ruralbeds);
            urbanhos = itemView.findViewById(R.id.urbanhospitals);
            card = itemView.findViewById(R.id.card);
            urbanbeds = itemView.findViewById(R.id.urbanbeds);
        }
    }
}
