package com.pedagogiksolution.CFPStJerome.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.google.firebase.firestore.QuerySnapshot;
import com.pedagogiksolution.CFPStJerome.R;
import com.pedagogiksolution.CFPStJerome.adapter.RepertoireAdminAdapter;
import com.pedagogiksolution.CFPStJerome.beans.RepertoireAdminBean;


import java.util.ArrayList;
import java.util.Map;



public class RepertoireAdminFragment extends Fragment implements OnItemClickListener {

	View v;	
	Context context;
	RepertoireAdminAdapter mAdapter;
	RepertoireAdminBean mBean;
	ArrayList<RepertoireAdminBean> dataModels;
	ListView listView;
	FirebaseFirestore db;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View v = inflater.inflate(R.layout.repertoire_listview, container, false);

		context=getActivity().getApplicationContext();
		dataModels= new ArrayList<>();
		listView=(ListView) v.findViewById(R.id.list);
		db = FirebaseFirestore.getInstance();
		db.collection("repertoireAdministration").orderBy("rang")
				.get()
				.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
					@Override
					public void onComplete(@NonNull Task<QuerySnapshot> task) {
						if (task.isSuccessful()) {
							for (DocumentSnapshot document : task.getResult()) {
								mBean = new RepertoireAdminBean();
								Map mData = document.getData();
								String nom = (String) mData.get("nom");
								String prenom = (String) mData.get("prenom");
								String fonction = (String) mData.get("fonction");
								String courriel = (String) mData.get("courriel");
								String poste = (String) mData.get("poste");

								mBean.setPrenom(prenom);
								mBean.setNom(nom);
								mBean.setFonction(fonction);
								mBean.setCourriel(courriel);
								mBean.setPoste(poste);

								dataModels.add(mBean);


							}


							mAdapter = new RepertoireAdminAdapter(dataModels,context);
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
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {


		RepertoireAdminBean mBean = (RepertoireAdminBean) parent.getAdapter().getItem(position);
		String test = mBean.getCourriel();
		
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.setType("text/plain");
		emailIntent.putExtra(Intent.EXTRA_EMAIL,new String[] {test}); 
		startActivity(emailIntent);
		
		
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);

		inflater.inflate(R.menu.repertoire_admin, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.repertoire_appel:

			Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
			phoneIntent.setData(Uri.parse("tel:450–565-0006"));
			startActivity(phoneIntent);
			break;

		}
		return true;
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onPrepareOptionsMenu(menu);
	}
	
	
	
	
}
	
