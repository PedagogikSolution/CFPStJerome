package com.pedagogiksolution.CFPStJerome.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.fragment.app.Fragment;

import com.pedagogiksolution.CFPStJerome.R;

public class NouvelleWebViewFragment extends Fragment {

    View v;
    WebView webView;
    SharedPreferences sharedPref;
    
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		v = inflater.inflate(R.layout.nouvelle_webview, container, false);
		String test2 = getArguments().getString("mURL");
		webView = v.findViewById(R.id.webView1);
		webView.loadUrl(test2);
		
		sharedPref = getActivity().getSharedPreferences("backstack",0);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt("webview", 1);
	 	editor.commit();

		return v;		
	}



}
