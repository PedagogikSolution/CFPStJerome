package com.pedagogiksolution.CFPStJerome.fragment;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.pedagogiksolution.CFPStJerome.R;
import com.pedagogiksolution.CFPStJerome.service.MyRappelService;
import com.pedagogiksolution.CFPStJerome.utils.DatabaseHelper;

import java.util.Calendar;

public class RappelsAjoutFragment extends Fragment implements OnClickListener {
	View v;
    static TextView tvDate;
	static TextView tvTime;
    EditText editTitre, editDesc;
    String mTitre,mDate,mDesc,mTime;
    DatabaseHelper mDatabase;
    ImageButton btDate,btTime;
    Button bt_ajouter_rappel;
    static int mYears;
    static int mMonths,mMonths2;
    static int mDays;
    static int mHourOfDay;
    static int mMinutes;
    SharedPreferences sharedPref;
    int mInt,mInt1;
    Calendar mCal;
    Cursor c;
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.rappel_ajout , container, false);
		tvDate = (TextView) v.findViewById(R.id.tv_date_rappel);
		tvTime = (TextView) v.findViewById(R.id.tv_time_rappel);
		editTitre = (EditText) v.findViewById(R.id.editText1);
		editDesc = (EditText) v.findViewById(R.id.editText2);
		btDate = (ImageButton) v.findViewById(R.id.ib_date_rappel);
		btTime = (ImageButton) v.findViewById(R.id.ib_time_rappel);
		bt_ajouter_rappel = (Button) v.findViewById(R.id.bt_ajouter_rappel);
		btDate.setOnClickListener(this);
		btTime.setOnClickListener(this);
		bt_ajouter_rappel.setOnClickListener(this);
		
		
		
		mDatabase = new DatabaseHelper(getActivity());
		mDatabase.open();
		Cursor c1 = mDatabase.checkEmpty();
		int rowCount=c1.getCount();
		if(rowCount!=0){
		c = mDatabase.getNextId();
		c.moveToFirst();
		mInt1 = c.getInt(c.getColumnIndex(c.getColumnName(0)));
		mInt = mInt1 +1;
		}else {
		mInt=0;
		}
		mDatabase.close();
	
		return v;
	}
	
	@Override
	public void onClick(View v) {
		
		
		
		switch(v.getId()) {
		case R.id.ib_date_rappel:
			DialogFragment newFragment = new DatePickerFragment();
			newFragment.show(getChildFragmentManager(), "DatePicker");
			
			break;
		case R.id.ib_time_rappel:
			DialogFragment newFragment2 = new TimePickerFragment();
			newFragment2.show(getChildFragmentManager(), "DatePicker");
			break;
		case R.id.bt_ajouter_rappel:
			asyncInsertRappel();
			registerAlert(mYears,mMonths,mDays,mHourOfDay,mMinutes,mInt);
			Fragment fragment = new RappelsFragment();
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
			break;
		}
		
	}
	
	private void registerAlert(int years2, int months2, int days2, int hourOfDay2, int minutes2, int mInt2) {
		
		
		
		mCal = Calendar.getInstance();		
		mCal.set(Calendar.YEAR, years2);
		mCal.set(Calendar.MONTH, months2);
		mCal.set(Calendar.DAY_OF_MONTH, days2);
		mCal.set(Calendar.HOUR_OF_DAY, hourOfDay2);
		mCal.set(Calendar.MINUTE, minutes2);
		
		
		
		Intent mIntent1 = new Intent(getActivity(),
				MyRappelService.class);
		
		PendingIntent pi = PendingIntent.getService(getActivity(), mInt2,
				mIntent1, PendingIntent.FLAG_UPDATE_CURRENT);
		
		AlarmManager am = (AlarmManager) getActivity()
				.getSystemService(Context.ALARM_SERVICE);
		
		am.set(AlarmManager.RTC_WAKEUP, mCal.getTimeInMillis(), pi);
		
		
	}

	private void asyncInsertRappel() {
		
		sharedPref = getActivity().getSharedPreferences("rappel_alarm",0);
	 	mInt1 = sharedPref.getInt("mInt", 0);
	 	
	 	mInt = mInt1 +1;
		
		String titre = editTitre.getText().toString();
		String desc = editDesc.getText().toString();
		String date = tvDate.getText().toString();
		String time = tvTime.getText().toString();
		DatabaseHelper mDatabase = new DatabaseHelper(getActivity());
		mDatabase.open();
		mDatabase.insertMesRappels(titre, date, desc, time);
		mDatabase.close();
		
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt("mInt", mInt);
	 	editor.commit();
		
	}

	public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Calendar calendar = Calendar.getInstance();
		int yy = calendar.get(Calendar.YEAR);
		int mm = calendar.get(Calendar.MONTH);
		int dd = calendar.get(Calendar.DAY_OF_MONTH);
		return new DatePickerDialog(getActivity(), this, yy, mm, dd);
		}
		 
		
		@Override
		public void onDateSet(DatePicker view, int year, int month, int day) {
			
		mYears = year;
		mMonths = month;
		mMonths2 = month + 1;
		mDays = day;
		if(mMonths2<10 && mDays<10){
		tvDate.setText(year+"-0"+ mMonths2 +"-0"+ mDays);}
		else if (mMonths2<10 && mDays>9) {
			tvDate.setText(year+"-0"+ mMonths2 +"-"+ mDays);	
		}
		else if (mMonths2>9 && mDays<10) {
			tvDate.setText(year+"-"+ mMonths2 +"-0"+ mDays);	
		}
		else {
			tvDate.setText(year+"-"+ mMonths2 +"-"+ mDays);
		}
		tvDate.setTextSize(20);
			
		}	
	}
	
	public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Calendar c = Calendar.getInstance();
	        int hour = c.get(Calendar.HOUR_OF_DAY);
	        int minute = c.get(Calendar.MINUTE);
	        return new TimePickerDialog(getActivity(), this, hour, minute,
	                DateFormat.is24HourFormat(getActivity()));
		}
		 
		
		public void onTimeSet(TimePicker view, int hour, int minute) {
		  if(minute<10){
			  tvTime.setText(hour +":0" + minute);  
		  }else{
	      tvTime.setText(hour +":" + minute);}
	      tvTime.setTextSize(20);
	      mHourOfDay = hour;
	      mMinutes=minute;
	    }
	}
	
}


