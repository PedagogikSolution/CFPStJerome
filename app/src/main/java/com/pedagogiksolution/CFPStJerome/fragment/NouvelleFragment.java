package com.pedagogiksolution.CFPStJerome.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.pedagogiksolution.CFPStJerome.R;
import com.pedagogiksolution.CFPStJerome.adapter.NouvelleListViewAdapter;
import com.pedagogiksolution.CFPStJerome.beans.NouvellesBean;

import java.util.ArrayList;
import java.util.Map;


public class NouvelleFragment extends Fragment implements OnItemClickListener {


	View v;
	Context context;
	NouvelleListViewAdapter mAdapter;
	NouvellesBean mBean;
	ArrayList<NouvellesBean> dataModels;
	ListView listView;
	FirebaseFirestore db;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.nouvelle_listview, container, false);

		context=getActivity().getApplicationContext();
		dataModels= new ArrayList<>();
		listView=v.findViewById(R.id.list);
		db = FirebaseFirestore.getInstance();
		db.collection("nouvelles").orderBy("date", Query.Direction.DESCENDING)
				.get()
				.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
					@Override
					public void onComplete(@NonNull Task<QuerySnapshot> task) {
						if (task.isSuccessful()) {
							for (DocumentSnapshot document : task.getResult()) {
								mBean = new NouvellesBean();
								Map mData = document.getData();
								String titre = (String) mData.get("titre");
								String body = (String) mData.get("body");
								String date = (String) mData.get("date");
								String url = (String) mData.get("url");

								mBean.setTitre(titre);
								mBean.setBody(body);
								mBean.setDate(date);
								if(url!=null) {
									mBean.setUrl(url);
								}

								dataModels.add(mBean);


							}


							mAdapter = new NouvelleListViewAdapter(dataModels,context);
							listView.setAdapter(mAdapter);

						} else {
							// on affiche erreur dialogue
						}
					}
				});

		listView.setOnItemClickListener(this);
		return v;
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		NouvellesBean mBean = (NouvellesBean) parent.getAdapter().getItem(position);
		String mUrl = mBean.getUrl();

		if(mUrl!=null){
		Intent mI = new Intent(Intent.ACTION_VIEW, Uri.parse(mUrl));
		startActivity(mI);}

	}
}
