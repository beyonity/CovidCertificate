package com.bogarsoft.covidapp.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.bogarsoft.covidapp.R;
import com.bogarsoft.covidapp.adapters.PageAdapter;
import com.bogarsoft.covidapp.fragments.DownloadVaccineCertificate;
import com.bogarsoft.covidapp.fragments.DownloadedCertificate;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;

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