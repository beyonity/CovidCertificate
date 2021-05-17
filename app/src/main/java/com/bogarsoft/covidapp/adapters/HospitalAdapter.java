package com.bogarsoft.covidapp.adapters;

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

import com.bogarsoft.covidapp.R;

import com.bogarsoft.covidapp.models.Hospital;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.ViewHolder> {

        List<Hospital> hospitalList;
        Activity activity;


    public HospitalAdapter(List<Hospital> hospitalList, Activity activity) {
        this.hospitalList = hospitalList;
        this.activity = activity;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.addresslayout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HospitalAdapter.ViewHolder holder, int position) {
        Hospital hospital = hospitalList.get(position);
        holder.name.setText(hospital.getName());
        holder.address.setText(hospital.getAddress()+", "+hospital.getCity()+",\n"+hospital.getState()+"-"+hospital.getPincode()+".");
        holder.phone.setText(hospital.getPhone());

        holder.map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:"+hospital.getLatitude()+","+hospital.getLongitude()+"?q="+hospital.getName());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(activity.getPackageManager()) != null) {
                    activity.startActivity(mapIntent);
                }
            }
        });

        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + hospital.getPhone()));
               activity.startActivity(intentDial);
            }
        });

    }

    @Override
    public int getItemCount() {
        return hospitalList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,address,phone;
        ImageView map,call;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            phone = itemView.findViewById(R.id.phone);
            map = itemView.findViewById(R.id.map);
            call = itemView.findViewById(R.id.call);
        }
    }
}
