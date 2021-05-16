package com.bogarsoft.covidcertificate.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

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
import com.bogarsoft.covidcertificate.utils.Constants;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.common.hash.Hashing;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class DownloadVaccineCertificateActivity extends AppCompatActivity {

    private static final String TAG = "DownloadVaccineCertific";
    TextView desc;
    MaterialButton btn;
    TextInputLayout phonelayout,otplayout,reslayout;
    TextInputEditText phone,otp,refid;
    int mode = 0;
    String txnId = "";
    String token = "";
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

        desc = findViewById(R.id.desc);
        btn = findViewById(R.id.btn);
        phone = findViewById(R.id.phone);
        otp = findViewById(R.id.otp);
        refid = findViewById(R.id.refid);
        phonelayout = findViewById(R.id.phonelayout);
        otplayout = findViewById(R.id.otplayout);
        reslayout = findViewById(R.id.reflayout);
        otplayout.setVisibility(View.GONE);
        reslayout.setVisibility(View.GONE);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode==0){
                    if (TextUtils.isEmpty(phone.getText()) || phone.getText().length() > 10){
                        phone.setError("invalid phone number");
                        return;
                    }else {
                        phone.setError(null);
                    }
                    Log.d(TAG, "onClick: "+phone.getText().toString().trim());
                    JSONObject body = new JSONObject();
                    try {
                        body.put("mobile",phone.getText().toString().trim());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    AndroidNetworking.post(Constants.GENERATE_OTP)
                            .addHeaders("accept","application/json")
                            .addHeaders("Accept-Language","en_US")
                            .addHeaders("charset","utf-8")
                            .addHeaders("Content-Type","application/json")
                            .addHeaders("User-Agent","Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36")
                            .addJSONObjectBody(body)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d(TAG, "onResponse: "+response);
                                    try {

                                            Toast.makeText(DownloadVaccineCertificateActivity.this, "Otp sent", Toast.LENGTH_SHORT).show();
                                            txnId = response.getString("txnId");
                                            desc.setText("You Will get an OTP vis SMS");
                                            mode = 1;
                                            phonelayout.setVisibility(View.GONE);
                                            otplayout.setVisibility(View.VISIBLE);
                                            reslayout.setVisibility(View.GONE);
                                            btn.setText("Verifiy");


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        try {
                                            Toast.makeText(DownloadVaccineCertificateActivity.this, response.getString("error"), Toast.LENGTH_LONG).show();
                                        } catch (JSONException jsonException) {
                                            jsonException.printStackTrace();
                                        }

                                    }
                                }

                                @Override
                                public void onError(ANError anError) {
                                    Log.d(TAG, "onError: "+anError.getErrorDetail());
                                    Log.d(TAG, "onError: "+anError.getErrorCode());
                                    Toast.makeText(DownloadVaccineCertificateActivity.this, "Government Server Not responding Try later", Toast.LENGTH_LONG).show();

                                }
                            });
                }else if (mode == 1){
                    if (TextUtils.isEmpty(otp.getText())){
                        otp.setError("Enter otp");
                        return;
                    }else {
                        otp.setError(null);
                    }

                    String sha256hex = Hashing.sha256()
                            .hashString(otp.getText().toString().trim(), StandardCharsets.UTF_8)
                            .toString();

                    JSONObject body = new JSONObject();
                    try {
                        body.put("otp",sha256hex);
                        body.put("txnId",txnId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    AndroidNetworking.post(Constants.CONFIRM_OTP)
                            .addHeaders("accept","application/json")
                            .addHeaders("Accept-Language","en_US")
                            .addHeaders("Content-Type","application/json")
                            .addHeaders("User-Agent","Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36")
                            .addBodyParameter(body)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d(TAG, "onResponse: "+response);
                                    try {

                                            Toast.makeText(DownloadVaccineCertificateActivity.this, "Otp verified", Toast.LENGTH_SHORT).show();
                                            token = response.getString("token");
                                            desc.setText(refid.getHint());
                                            mode = 2;
                                            phonelayout.setVisibility(View.GONE);
                                            otplayout.setVisibility(View.GONE);
                                            reslayout.setVisibility(View.VISIBLE);
                                            btn.setText("Download Certificate");



                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        try {
                                            Toast.makeText(DownloadVaccineCertificateActivity.this, response.getString("error"), Toast.LENGTH_LONG).show();
                                        } catch (JSONException jsonException) {
                                            jsonException.printStackTrace();
                                        }

                                    }
                                }

                                @Override
                                public void onError(ANError anError) {
                                    Log.d(TAG, "onError: "+anError.getErrorDetail());
                                    Log.d(TAG, "onError: "+anError.getErrorCode());
                                    Toast.makeText(DownloadVaccineCertificateActivity.this, "Government Server Not responding Try later", Toast.LENGTH_LONG).show();
                                }
                            });
                }else if (mode == 2){
                    if (TextUtils.isEmpty(refid.getText())){
                        refid.setError(refid.getHint());
                        return;
                    }else {
                        refid.setError(null);
                    }
                    AndroidNetworking.download(Constants.DOWNLOAD_CERTIFICATE,getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath(),"vaccine_certificate.pdf")
                            .addHeaders("accept","application/pdf")
                            .addHeaders("User-Agent","Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36")
                            .addHeaders("Authorization","Bearer "+token)
                            .addQueryParameter("beneficiary_reference_id",refid.getText().toString().trim())
                            .build()
                            .setDownloadProgressListener(new DownloadProgressListener() {
                                @Override
                                public void onProgress(long bytesDownloaded, long totalBytes) {
                                    Log.d(TAG, "onProgress: "+bytesDownloaded);
                                }
                            })
                            .startDownload(new DownloadListener() {
                                @Override
                                public void onDownloadComplete() {
                                    mode = 0;
                                    phonelayout.setVisibility(View.VISIBLE);
                                    otplayout.setVisibility(View.GONE);
                                    reslayout.setVisibility(View.GONE);
                                    btn.setText("get otp");
                                    desc.setText("We Will Send you OTP on you Phone");
                                    Toast.makeText(DownloadVaccineCertificateActivity.this, "Download completed", Toast.LENGTH_SHORT).show();
                                    File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath(), "vaccine_certificate.pdf");
                                    Uri fileUri = FileProvider.getUriForFile(DownloadVaccineCertificateActivity.this, BuildConfig.APPLICATION_ID + ".provider", file);


                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setDataAndType(fileUri, "application/pdf");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //dont forget add this line
                                    startActivity(intent);
                                }

                                @Override
                                public void onError(ANError anError) {
                                    Log.d(TAG, "onError: "+anError.getErrorDetail());
                                    Toast.makeText(DownloadVaccineCertificateActivity.this, anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });


    }
}