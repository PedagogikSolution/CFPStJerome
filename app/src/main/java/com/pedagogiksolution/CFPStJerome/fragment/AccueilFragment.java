package com.pedagogiksolution.CFPStJerome.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.pedagogiksolution.CFPStJerome.R;


public class AccueilFragment extends Fragment implements View.OnClickListener {

	Context mContext;
	ImageView youtube,facebook,instagram;
	TextView tv_courriel_accueil;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

	return inflater.inflate(R.layout.accueil, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {

		super.onViewCreated(view, savedInstanceState);

		mContext = getActivity();


		youtube = view.findViewById(R.id.iv_youtube);
		facebook = view.findViewById(R.id.iv_facebook);
		instagram = view.findViewById(R.id.iv_instagram);

		
		youtube.setOnClickListener(this);
		facebook.setOnClickListener(this);
		instagram.setOnClickListener(this);
		

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		super.onCreateOptionsMenu(menu, inflater);

		inflater.inflate(R.menu.repertoire_admin, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.repertoire_appel:

			Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
			phoneIntent.setData(Uri.parse("tel:450-565-0006"));
			startActivity(phoneIntent);
			break;

		}
		return true;
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {

		super.onPrepareOptionsMenu(menu);
	}
	

	@Override
	public void onClick(View v) {
		String mUrl;
		Intent mI;
		switch (v.getId()){

			case R.id.iv_youtube:
				mUrl = "http://www.youtube.com/user/CEPSaintJerome";
				mI = new Intent(Intent.ACTION_VIEW, Uri.parse(mUrl));
				startActivity(mI);

				break;
			case R.id.iv_facebook:
				mUrl = "http://www.facebook.com/formationcep/";
				mI = new Intent(Intent.ACTION_VIEW, Uri.parse(mUrl));
				startActivity(mI);
				break;
			case R.id.iv_instagram:
				mUrl = "http://www.instagram.com/cep_saintjerome/";
				mI = new Intent(Intent.ACTION_VIEW, Uri.parse(mUrl));
				startActivity(mI);
				break;




		}
	}
}