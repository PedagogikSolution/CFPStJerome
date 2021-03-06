package com.pedagogiksolution.CFPStJerome.fragment;


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.pedagogiksolution.CFPStJerome.R;

import java.util.Objects;


public class SolutionPedagogikFragment extends Fragment implements OnClickListener {
	Button bt1;
	View v;
	Intent mI;
	TextView tvSpVersion;
	PackageInfo pInfo;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.solutionpedagogik, container, false);
		return v;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		tvSpVersion = (TextView) v.findViewById(R.id.tvSpVersion);
		bt1 = (Button) v.findViewById(R.id.bt_sp1);
		bt1.setOnClickListener(this);
		try {
			pInfo = Objects.requireNonNull(getActivity()).getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
		} catch (NameNotFoundException e) {			
			e.printStackTrace();
		}
		String version_info = pInfo.versionName;
		tvSpVersion.setText(getString(R.string.versionText)+version_info);

	}


	

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.bt_sp1) {
			mI = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.solutionpedagogik.com"));
			startActivity(mI);
		}
		
	}

}
