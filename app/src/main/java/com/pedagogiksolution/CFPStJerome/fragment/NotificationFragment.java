package com.pedagogiksolution.CFPStJerome.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.pedagogiksolution.CFPStJerome.R;
import com.pedagogiksolution.CFPStJerome.adapter.NotificationAdapter;
import com.pedagogiksolution.CFPStJerome.beans.NotificationsBean;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class NotificationFragment extends Fragment implements OnItemClickListener {


	Context context;
	String nomLastCategorie;
	NotificationAdapter mAdapter;
	NotificationsBean mBean;
	ArrayList<NotificationsBean> dataModels;
	ListView listView;
	FirebaseFirestore db;
	private static String TAG_NAME_TOPIC_ABONNEMENT = "Abonnement";
	SharedPreferences.Editor[] editor = new SharedPreferences.Editor[1];

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.notification_listview, container, false);


		context= Objects.requireNonNull(getActivity()).getApplicationContext();


		listView=v.findViewById(R.id.list);
		nomLastCategorie="vide";
		dataModels = new ArrayList<>();
		db = FirebaseFirestore.getInstance();
		db.collection("segmentNotifications").orderBy("rangCategorie").orderBy("rang")
				.get()
				.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
					@Override
					public void onComplete(@NonNull Task<QuerySnapshot> task) {
						if (task.isSuccessful()) {

							for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())) {



									Map mData = document.getData();
									String categorie = (String) mData.get("categorie");
									String segment = (String) mData.get("segment");
									String documentId = document.getId();


									boolean isCategorie = checkIfNewCategorie(categorie, nomLastCategorie);


									if(isCategorie){
										mBean = new NotificationsBean();
										mBean.setCategorie(categorie);
										mBean.setSection(true);
										dataModels.add(mBean);

										mBean = new NotificationsBean();
										mBean.setSegment(segment);
										mBean.setDocumentId(documentId);
										mBean.setSection(false);

										dataModels.add(mBean);
									} else {
										mBean = new NotificationsBean();
										mBean.setSegment(segment);
										mBean.setDocumentId(documentId);
										mBean.setSection(false);

										dataModels.add(mBean);

									}
						}


							mAdapter = new NotificationAdapter(dataModels,context);
							listView.setAdapter(mAdapter);

						}
					}
				});
		listView.setOnItemClickListener(this);

		return v;

	}

	private boolean checkIfNewCategorie(String categorie, String lastNomCategorie) {

		if (categorie.equalsIgnoreCase(lastNomCategorie)) {
			nomLastCategorie =categorie;
			return false;
		} else {
			nomLastCategorie = categorie;
			return true;
		}

	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {

		Switch mSwitchButton = view.findViewById(R.id.switchSegment);

		NotificationsBean mBean = (NotificationsBean) parent.getAdapter().getItem(position);


		SharedPreferences mSharedPref = getActivity().getSharedPreferences(mBean.getDocumentId(),0);
		boolean mAbonnement = mSharedPref.getBoolean(TAG_NAME_TOPIC_ABONNEMENT,false);
		editor[0] = mSharedPref.edit();


		if(mAbonnement){
			mSwitchButton.setChecked(false);
			Task<Void> mResult = FirebaseMessaging.getInstance().unsubscribeFromTopic(mBean.getDocumentId());
			mResult.addOnSuccessListener(new OnSuccessListener<Void>() {
				@Override
				public void onSuccess(Void aVoid) {
					editor[0].putBoolean(TAG_NAME_TOPIC_ABONNEMENT,false);
					editor[0].commit();
					Toast.makeText(getContext(),"Vous êtes correctement désabonné",Toast.LENGTH_LONG).show();
				}
			});

		} else {
			 mSwitchButton.setChecked(true);
			Task<Void> mResult = FirebaseMessaging.getInstance().subscribeToTopic(mBean.getDocumentId());
			mResult.addOnSuccessListener(new OnSuccessListener<Void>() {
				@Override
				public void onSuccess(Void aVoid) {
					editor[0].putBoolean(TAG_NAME_TOPIC_ABONNEMENT,true);
					editor[0].commit();
					Toast.makeText(getContext(),"Vous êtes correctement abonné",Toast.LENGTH_LONG).show();
				}
			});

		}

	}

}