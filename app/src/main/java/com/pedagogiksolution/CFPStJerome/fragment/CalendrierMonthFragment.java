package com.pedagogiksolution.CFPStJerome.fragment;


import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.pedagogiksolution.CFPStJerome.R;
import com.pedagogiksolution.CFPStJerome.utils.DatabaseHelper;

public class CalendrierMonthFragment extends Fragment {

	private static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
	private static final String EXTRA_IMAGE = "EXTRA_IMAGE";
	DatabaseHelper mDB;
	Cursor c;

	 public static final CalendrierMonthFragment newInstance(int month,String message)

	 {

	CalendrierMonthFragment f = new CalendrierMonthFragment();

	   Bundle bdl = new Bundle();
	 //  Bundle bdl2 = new Bundle();

	   bdl.putString(EXTRA_MESSAGE, message);
	   bdl.putInt(EXTRA_IMAGE, month);

	   f.setArguments(bdl);
	   f.setArguments(bdl);

	   return f;

	 }

	 @Override

	 public View onCreateView(LayoutInflater inflater, ViewGroup container,

	   Bundle savedInstanceState) {
		 
		 
	   byte[] bb = null; 
       int imageMonth = getArguments().getInt(EXTRA_IMAGE); 
	   String message = getArguments().getString(EXTRA_MESSAGE);

	   
	   mDB = new DatabaseHelper(getActivity());
	   mDB.open();
	   
	   c = mDB.getImageCalendrier(imageMonth);
	    
	   if( c != null && c.moveToFirst() ){
	   
	   
	   bb = c.getBlob(c.getColumnIndex(c.getColumnName(2)));
	   
	   }
	   mDB.close();


	   View v = inflater.inflate(R.layout.calendrier_view_pager_item, container, false);
	   
	   ScrollView mScrollView = v.findViewById(R.id.scrollViewCalendrier);
	   mScrollView.fullScroll(ScrollView.FOCUS_UP);
	   ImageView myImage =  v.findViewById(R.id.imageView1);

	   if(bb!=null) {
		   myImage.setImageBitmap(BitmapFactory.decodeByteArray(bb, 0, bb.length));
	   } else {

	   }

	   TextView messageTextView = v.findViewById(R.id.textView1);

	   messageTextView.setText(message);
	   

	   return v;

	 }

	}
