package com.bogarsoft.covidcertificate.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bogarsoft.covidcertificate.BuildConfig;
import com.bogarsoft.covidcertificate.R;
import com.bogarsoft.covidcertificate.adapters.PageAdapter;
import com.bogarsoft.covidcertificate.fragments.DownloadVaccineCertificate;
import com.bogarsoft.covidcertificate.fragments.DownloadedCertificate;
import com.bogarsoft.covidcertificate.utils.Constants;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.common.hash.Hashing;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class DownloadVaccineCertificateActivity extends AppCompatActivity {

    private static final String TAG = "DownloadVaccineCertific";

    ViewPager vg;
    DownloadVaccineCertificate downloadVaccineCertificate = new DownloadVaccineCertificate();
    DownloadedCertificate downloadedCertificate = new DownloadedCertificate();
    PageAdapter pageAdapter;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_vaccine_certificate);
        ((MaterialButton)findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        vg = findViewById(R.id.vg);
        tabLayout = findViewById(R.id.tablayout);
        pageAdapter = new PageAdapter(getSupportFragmentManager());
        pageAdapter.addFragment(downloadVaccineCertificate,"Download Certificate");
        pageAdapter.addFragment(downloadedCertificate,"Downloaded");

        vg.setAdapter(pageAdapter);
        tabLayout.setupWithViewPager(vg);


        vg.setOffscreenPageLimit(2);


    }
}