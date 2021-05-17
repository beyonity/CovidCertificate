package com.bogarsoft.covidcertificate.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bogarsoft.covidcertificate.BuildConfig;
import com.bogarsoft.covidcertificate.R;
import com.bogarsoft.covidcertificate.activites.DownloadVaccineCertificateActivity;
import com.bogarsoft.covidcertificate.utils.Constants;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.common.hash.Hashing;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DownloadVaccineCertificate#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DownloadVaccineCertificate extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = "DownloadVaccineCertific";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView desc;
    MaterialButton btn,goback;
    TextInputLayout phonelayout,otplayout,reslayout;
    TextInputEditText phone,otp,refid;
    int mode = 0;
    String txnId = "";
    String token = "";
    public DownloadVaccineCertificate() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DownloadVaccineCertificate.
     */
    // TODO: Rename and change types and number of parameters
    public static DownloadVaccineCertificate newInstance(String param1, String param2) {
        DownloadVaccineCertificate fragment = new DownloadVaccineCertificate();
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
        View view =  inflater.inflate(R.layout.fragment_download_vaccine_certificate, container, false);
        goback = view.findViewById(R.id.goback);
        desc = view.findViewById(R.id.desc);
        btn = view.findViewById(R.id.btn);
        phone = view.findViewById(R.id.phone);
        otp = view.findViewById(R.id.otp);
        refid = view.findViewById(R.id.refid);
        phonelayout = view.findViewById(R.id.phonelayout);
        otplayout = view.findViewById(R.id.otplayout);
        reslayout = view.findViewById(R.id.reflayout);
        otplayout.setVisibility(View.GONE);
        reslayout.setVisibility(View.GONE);
        goback.setVisibility(View.GONE);
        goback = view.findViewById(R.id.goback);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phonelayout.setVisibility(View.VISIBLE);
                otplayout.setVisibility(View.GONE);
                reslayout.setVisibility(View.GONE);
                btn.setText("get otp");
                desc.setText("We Will Send you OTP on your Phone");
                goback.setVisibility(View.GONE);
                txnId = "";
                token = "";
                mode = 0;
            }
        });
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
                            .addHeaders("charset","utf-8")
                            .setContentType("application/json")
                            .setUserAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36")
                            .addJSONObjectBody(body)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d(TAG, "onResponse: "+response);
                                    try {

                                        Toast.makeText(getContext(), "Otp sent", Toast.LENGTH_LONG).show();
                                        txnId = response.getString("txnId");
                                        desc.setText("You Will get an OTP vis SMS");
                                        mode = 1;
                                        phonelayout.setVisibility(View.GONE);
                                        otplayout.setVisibility(View.VISIBLE);
                                        reslayout.setVisibility(View.GONE);
                                        goback.setVisibility(View.VISIBLE);
                                        btn.setText("Verifiy");


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        try {
                                            Toast.makeText(getContext(), response.getString("error"), Toast.LENGTH_LONG).show();
                                        } catch (JSONException jsonException) {
                                            jsonException.printStackTrace();
                                        }

                                    }
                                }

                                @Override
                                public void onError(ANError anError) {
                                    Log.d(TAG, "onError: "+anError.getErrorDetail());
                                    Log.d(TAG, "onError: "+anError.getErrorCode());
                                    Toast.makeText(getContext(), "Government Server Not responding Try later", Toast.LENGTH_LONG).show();

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
                            .setContentType("application/json")
                            .setUserAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36")
                            .addJSONObjectBody(body)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d(TAG, "onResponse: "+response);
                                    try {

                                        Toast.makeText(getContext(), "Otp verified", Toast.LENGTH_LONG).show();
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
                                            Toast.makeText(getContext(), response.getString("error"), Toast.LENGTH_LONG).show();
                                        } catch (JSONException jsonException) {
                                            jsonException.printStackTrace();
                                        }

                                    }
                                }

                                @Override
                                public void onError(ANError anError) {
                                    Log.d(TAG, "onError: "+anError.getErrorDetail());
                                    Log.d(TAG, "onError: "+anError.getErrorCode());
                                    Toast.makeText(getContext(), "Government Server Not responding Try later", Toast.LENGTH_LONG).show();
                                }
                            });
                }else if (mode == 2){
                    if (TextUtils.isEmpty(refid.getText())){
                        refid.setError(refid.getHint());
                        return;
                    }else {
                        refid.setError(null);
                    }
                    String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                    fileName = fileName+"_vaccine_certificte.pdf";
                    String finalFileName = fileName;
                    AndroidNetworking.download(Constants.DOWNLOAD_CERTIFICATE,getActivity().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath(),fileName)
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
                                    desc.setText("We Will Send you OTP on your Phone");
                                    txnId = "";
                                    token = "";
                                    Toast.makeText(getContext(), "Download completed", Toast.LENGTH_SHORT).show();
                                    File file = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath(), finalFileName);
                                    Uri fileUri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider", file);


                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setDataAndType(fileUri, "application/pdf");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //dont forget add this line
                                    startActivity(intent);
                                }

                                @Override
                                public void onError(ANError anError) {
                                    Log.d(TAG, "onError: "+anError.getErrorDetail());
                                    Toast.makeText(getContext(), anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                                    File file = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath(), finalFileName);
                                    if (file.exists()){
                                        file.delete();
                                    }
                                }
                            });
                }
            }
        });



        return view;
    }
}