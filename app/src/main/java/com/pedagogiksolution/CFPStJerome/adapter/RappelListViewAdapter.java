package com.pedagogiksolution.CFPStJerome.adapter;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.pedagogiksolution.CFPStJerome.R;

public class RappelListViewAdapter extends CursorAdapter {

	@SuppressWarnings("deprecation")
	public RappelListViewAdapter(Context context, Cursor c) {
		super(context, c);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View newView(Context context, Cursor c, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View retView = inflater.inflate(R.layout.rappel_listview_item, parent, false);
		return retView;
	}
	
	@Override
	public void bindView(View v, Context context, Cursor cursor) {
		
		TextView rappelTitre = (TextView) v.findViewById(R.id.rappelTitre);
		TextView rappelDate = (TextView) v.findViewById(R.id.rappelDate);
		TextView rappelDesc = (TextView) v.findViewById(R.id.rappelDesc);
		TextView rappelTime = (TextView) v.findViewById(R.id.rappelTime);

	
		rappelTitre.setText(cursor.getString(cursor.getColumnIndex(cursor
				.getColumnName(2))));
		rappelDate.setText("la journée du " + cursor.getString(cursor.getColumnIndex(cursor
				.getColumnName(4))));
		rappelDesc.setText(cursor.getString(cursor.getColumnIndex(cursor
				.getColumnName(3))));
		rappelTime.setText("Votre alarme sonnera à " + cursor.getString(cursor.getColumnIndex(cursor
				.getColumnName(1))));

	}


}
