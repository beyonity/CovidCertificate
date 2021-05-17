package com.bogarsoft.covidapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bogarsoft.covidapp.R;
import com.bogarsoft.covidapp.adapters.FileListAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DownloadedCertificate#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DownloadedCertificate extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static final String TAG = "DownloadedCertificate";


    RecyclerView recyclerView;
    FileListAdapter fileListAdapter;
    List<String> filelist = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;
    TextView nofiles;
    public DownloadedCertificate() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DownloadedCertificate.
     */
    // TODO: Rename and change types and number of parameters
    public static DownloadedCertificate newInstance(String param1, String param2) {
        DownloadedCertificate fragment = new DownloadedCertificate();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_downloaded_certificate, container, false);
        nofiles = view.findViewById(R.id.nodownloads);
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        recyclerView = view.findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fileListAdapter = new FileListAdapter(filelist,getActivity());
        recyclerView.setAdapter(fileListAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });


        return view;
    }

    private void getData(){

        File file = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath());
        if (!file.exists()){
            file.mkdir();
        }
        filelist.clear();

        for (String names : file.list()){
            filelist.add(names);
        }

        if (filelist.size() > 0){
            nofiles.setVisibility(View.GONE);
        }else {
            nofiles.setVisibility(View.VISIBLE);
        }
        swipeRefreshLayout.setRefreshing(false);
        fileListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }
}