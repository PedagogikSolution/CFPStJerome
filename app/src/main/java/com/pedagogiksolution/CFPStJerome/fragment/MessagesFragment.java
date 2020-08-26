package com.pedagogiksolution.CFPStJerome.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.ListFragment;

import com.pedagogiksolution.CFPStJerome.R;
import com.pedagogiksolution.CFPStJerome.adapter.MessagesListViewAdapter;
import com.pedagogiksolution.CFPStJerome.utils.DatabaseHelper;


public class MessagesFragment extends ListFragment implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {

	View v;
	AlertDialog.Builder builder;
	AlertDialog builder2;
	MessagesListViewAdapter mAdapter;
	DatabaseHelper mDatabase;
	int mId;
	Cursor map;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		v = inflater.inflate(R.layout.mes_messages, container, false);
		mDatabase = new DatabaseHelper(getActivity());
		mDatabase.open();
		setHasOptionsMenu(true);

		return v;
	}



	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		boolean mTest5 = getActivity().getIntent().getBooleanExtra("PUSH", false);
		if(mTest5) {

			String title = getActivity().getIntent().getStringExtra("TITLE");
			String body = getActivity().getIntent().getStringExtra("BODY");
			String url = getActivity().getIntent().getStringExtra("URL");
			getActivity().getIntent().removeExtra("PUSH");
			getActivity().getIntent().removeExtra("PUSH2");
			getActivity().getIntent().removeExtra("TITLE");
			getActivity().getIntent().removeExtra("BODY");
			getActivity().getIntent().removeExtra("URL");
			DialogFragment dialogueFragment = new DialogueFragment();
			Bundle mType = new Bundle();
			mType.putInt("dialogType", 4);
			mType.putString("title", title);
			mType.putString("body", body);
			mType.putString("url", url);
			dialogueFragment.setArguments(mType);
			dialogueFragment.show(getActivity().getSupportFragmentManager(), "dialog");

		}
		getListView().setDivider(this.getResources().getDrawable(R.drawable.transperent_color));
		getListView().setDividerHeight(5);

		new Handler().post(new Runnable() {

			@Override
			public void run() {

				mAdapter = new MessagesListViewAdapter(getActivity(), mDatabase.getListeMessages());

				setListAdapter(mAdapter);
			}
		});

		getListView().setOnItemLongClickListener(this);
        getListView().setOnItemClickListener(this);
	}


	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);

		inflater.inflate(R.menu.main, menu);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.ajouter_rappel :
				Fragment fragment = new RappelsAjoutFragment();
				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
				break;
			case R.id.delete_rappel :
				builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("INFORMATION");
				builder.setMessage("Pour détruire un message, vous n'avez qu'à appuyer longuement sur la fiche de celui-ci");
				builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
					}
				});
				builder2=builder.create();
				builder2.show();

				break;
		}
		return true;
	}


	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		if (builder2 != null) {
			builder2.dismiss();
			builder2 = null;
		}
	}




	@Override
	public boolean onItemLongClick(AdapterView<?> adapter, View v, int position,
								   long id) {
		map = (Cursor) adapter.getAdapter().getItem(position);
		mId = map.getInt(map.getColumnIndex(map.getColumnName(0)));
		mDatabase = new DatabaseHelper(getActivity());
		mDatabase.open();
		mDatabase.deleteMesMessages(mId);
		mDatabase.close();

		Fragment fragment = new MessagesFragment();
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
		return true;
	}


	@Override
	public void onItemClick(AdapterView<?> adapter, View v, int position,
						long id) {
		map = (Cursor) adapter.getAdapter().getItem(position);
		String mUrl = map.getString(map.getColumnIndex(map.getColumnName(4)));
		if(mUrl!=null) {
			Intent mI = new Intent(Intent.ACTION_VIEW, Uri.parse(mUrl));
			startActivity(mI);
		}

	}
}
