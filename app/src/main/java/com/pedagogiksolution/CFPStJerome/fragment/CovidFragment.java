package com.pedagogiksolution.CFPStJerome.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.pedagogiksolution.CFPStJerome.R;

public class CovidFragment extends Fragment implements View.OnClickListener {

    Context mContext;
    Button covid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        return inflater.inflate(R.layout.covid, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        mContext = getActivity();


        covid = view.findViewById(R.id.bt_covid);


        covid.setOnClickListener(this);


    }




    @Override
    public void onClick(View v) {
        String mUrl;
        Intent mI;
        if (v.getId() == R.id.bt_covid) {
            mUrl = "https://acp.csrdn.qc.ca/Formulaire/LoginOST.aspx";
            mI = new Intent(Intent.ACTION_VIEW, Uri.parse(mUrl));
            startActivity(mI);
        }
    }
}