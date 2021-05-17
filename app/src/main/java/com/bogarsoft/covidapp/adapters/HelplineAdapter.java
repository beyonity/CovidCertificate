package com.bogarsoft.covidapp.adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bogarsoft.covidapp.R;
import com.bogarsoft.covidapp.models.Helpline;

import java.util.List;

public class HelplineAdapter extends RecyclerView.Adapter<HelplineAdapter.ViewHolder> {

        List<Helpline> helplineList;
        Activity activity;


    public HelplineAdapter(List<Helpline> helplines, Activity activity) {
        this.helplineList = helplines;
        this.activity = activity;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.phonenumber,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HelplineAdapter.ViewHolder holder, int position) {
        Helpline helpline = helplineList.get(position);
        holder.state.setText(helpline.getState());
        holder.regional.setText(helpline.getRegional());
        holder.phone.setText(helpline.getPhone());
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helpline.getPhone().contains("-")){
                    Intent intentDial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + helpline.getPhone()));
                    activity.startActivity(intentDial);
                }else {

                    Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://wa.me/+91"+helpline.getPhone()));
                    activity.startActivity(intent);
                }

            }
        });
        if ((helpline.getPhone().contains("-"))){
            holder.call.setImageResource(R.drawable.telephone);
        }else {

            holder.call.setImageResource(R.drawable.whatsapp);
        }
        if (position %2==0){
            holder.card.setBackgroundColor(activity.getResources().getColor(R.color.Platinum));
        }else {
            holder.card.setBackgroundColor(activity.getResources().getColor(R.color.white));

        }

    }

    @Override
    public int getItemCount() {
        return helplineList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView state,phone,regional;
        LinearLayout card;
        ImageView call;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            state = itemView.findViewById(R.id.state);
            phone = itemView.findViewById(R.id.phone);
            regional = itemView.findViewById(R.id.district);
            call = itemView.findViewById(R.id.call);
            card = itemView.findViewById(R.id.linearlayout);

        }
    }
}
