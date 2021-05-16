package com.bogarsoft.covidcertificate.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bogarsoft.covidcertificate.R;
import com.bogarsoft.covidcertificate.models.VaccineTracker;

import java.util.List;

public class VaccineTrackerAdapter extends RecyclerView.Adapter<VaccineTrackerAdapter.ViewHolder> {

        List<VaccineTracker> vaccineTrackerList;
        Activity activity;


    public VaccineTrackerAdapter(List<VaccineTracker> vaccineTrackerList, Activity activity) {
        this.vaccineTrackerList = vaccineTrackerList;
        this.activity = activity;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vaccine_tracker_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VaccineTrackerAdapter.ViewHolder holder, int position) {
        VaccineTracker vaccineTracker = vaccineTrackerList.get(position);
        holder.centername.setText(vaccineTracker.getCentername());
        holder.avacapacity.setText(vaccineTracker.getAvacapacity());
        holder.date.setText(vaccineTracker.getDate());
        holder.vaccinename.setText(vaccineTracker.getVaccinename());
        holder.otime.setText("From : "+vaccineTracker.getOtime());
        holder.ctime.setText("To : "+vaccineTracker.getCtime());
        holder.address.setText(vaccineTracker.getAddress()+" ,"+vaccineTracker.getDistrict());
        holder.pincode.setText(vaccineTracker.getState()+"-"+vaccineTracker.getPincode());
        holder.slots.setText(vaccineTracker.getSlots());
        holder.fee.setText(vaccineTracker.getFee().equals("0") ? "Free" : vaccineTracker.getFee());


    }

    @Override
    public int getItemCount() {
        return vaccineTrackerList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView centername,avacapacity,date,vaccinename,otime,ctime,address,pincode,slots,minage,fee;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            centername = itemView.findViewById(R.id.centername);
            avacapacity = itemView.findViewById(R.id.avbcapacity);
            date = itemView.findViewById(R.id.date);
            vaccinename = itemView.findViewById(R.id.vaccinename);
            otime = itemView.findViewById(R.id.otime);
            ctime = itemView.findViewById(R.id.ctime);
            address = itemView.findViewById(R.id.address);
            pincode = itemView.findViewById(R.id.pincode);
            slots = itemView.findViewById(R.id.slots);
            minage = itemView.findViewById(R.id.minage);
            fee = itemView.findViewById(R.id.fee);
        }
    }
}
