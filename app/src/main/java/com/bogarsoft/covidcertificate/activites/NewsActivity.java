package com.bogarsoft.covidcertificate.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.bogarsoft.covidcertificate.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class NewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        ((MaterialButton)findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        MaterialTextView title = findViewById(R.id.title);
        title.setText(getIntent().getStringExtra("title"));
        WebView webView = findViewById(R.id.webview);
        webView.loadUrl(getIntent().getStringExtra("url"));
    }
}