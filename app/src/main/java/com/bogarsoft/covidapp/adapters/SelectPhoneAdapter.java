package com.bogarsoft.covidapp.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bogarsoft.covidapp.R;
import com.bogarsoft.covidapp.models.CallPhone;


import java.util.Calendar;
import java.util.List;

public class SelectPhoneAdapter extends RecyclerView.Adapter<SelectPhoneAdapter.ViewHolder> {

    List<CallPhone> callPhones;
    Activity activity;
    private static final String TAG = "SelectAddressAdapter";
    public SelectPhoneAdapter(List<CallPhone> callPhones, Activity activity) {
        this.callPhones = callPhones;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_email_list,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CallPhone callPhone = callPhones.get(position);

        Log.d(TAG, "onBindViewHolder: "+callPhone.getPhone());
        holder.email.setText(callPhone.getPhone());
        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: set selected clicked");
                for(CallPhone a : callPhones){
                   callPhones.get(callPhones.indexOf(a)).setSelected(false);

                }

                callPhone.setSelected(true);
                //Log.d(TAG, "onClick: "+address.isSelected());
                notifyDataSetChanged();
            }
        });

        holder.radioButton.setChecked(callPhone.isSelected());


        ;
    }

    @Override
    public int getItemCount() {
        return callPhones
                .size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView email;
        RadioButton radioButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            email = itemView.findViewById(R.id.email);
            radioButton = itemView.findViewById(R.id.selectaddress);

        }
    }


}
