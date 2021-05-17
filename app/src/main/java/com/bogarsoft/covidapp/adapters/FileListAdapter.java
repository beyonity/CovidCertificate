package com.bogarsoft.covidapp.adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bogarsoft.covidapp.BuildConfig;
import com.bogarsoft.covidapp.R;
import com.google.android.material.card.MaterialCardView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.ViewHolder> {


    List<String> filelist;
    Activity activity;

    public FileListAdapter(List<String> filelist, Activity activity) {
        this.filelist = filelist;
        this.activity = activity;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.downloaded_file_list,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        String name = filelist.get(position);
        holder.name.setText(name);

        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(activity.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath(), name);
                Uri fileUri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".provider", file);


                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(fileUri, "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //dont forget add this line
                activity.startActivity(intent);
            }
        });

        if (position %2==0){
            holder.name.setBackgroundColor(activity.getResources().getColor(R.color.white));
        }else {
            holder.name.setBackgroundColor(activity.getResources().getColor(R.color.Jet_Gray));
        }

        if (position %2==0){
            holder.card.setBackgroundColor(activity.getResources().getColor(R.color.Jet_Gray));
        }else {
            holder.card.setBackgroundColor(activity.getResources().getColor(R.color.white));
        }

    }

    @Override
    public int getItemCount() {
        return filelist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        ImageView download;
        MaterialCardView card;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            card = itemView.findViewById(R.id.card);
            download = itemView.findViewById(R.id.download);
        }
    }
}
