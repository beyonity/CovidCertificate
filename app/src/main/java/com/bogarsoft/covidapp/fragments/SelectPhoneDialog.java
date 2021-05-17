package com.bogarsoft.covidapp.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bogarsoft.covidapp.R;
import com.bogarsoft.covidapp.adapters.SelectPhoneAdapter;
import com.bogarsoft.covidapp.models.CallPhone;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class SelectPhoneDialog extends BottomSheetDialogFragment {



    RecyclerView rv;
    SelectPhoneAdapter selectPhoneAdapter;
    TextView emptytext;
    List<CallPhone> addressList = new ArrayList<>();

    AppCompatImageView toggle;
    MaterialButton select;


    OnSelected onSelected;

    public void setOnSelected(OnSelected onSelected) {
        this.onSelected = onSelected;
    }

    public interface OnSelected{
        void onClose();
        void selected(CallPhone callPhone);
    }

    public SelectPhoneDialog(List<CallPhone> emaillist) {
        this.addressList = emaillist;
    }

    private static final String TAG = "PaymentSummaryDialog";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //bottom sheet round corners can be obtained but the while background appears to remove that we need to add this.
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.AppBottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.select_email_dialog,container,false);
        rv = view.findViewById(R.id.addressrv);
        select = view.findViewById(R.id.select);
        selectPhoneAdapter = new SelectPhoneAdapter(addressList,getActivity());
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(selectPhoneAdapter);
        toggle = view.findViewById(R.id.toggle);
        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (CallPhone  a: addressList){
                    if(a.isSelected()){
                        if(onSelected!=null){
                            onSelected.selected(a);
                        }
                    }
                }
            }
        });

        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
// androidx should use: com.google.android.material.R.id.design_bottom_sheet
                FrameLayout bottomSheet = (FrameLayout)
                dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet );
                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setPeekHeight(0);
            }
        });
    }



    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if(onSelected!=null){
            onSelected.onClose();
        }
    }
}
