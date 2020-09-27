package com.pedagogiksolution.CFPStJerome.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.squareup.okhttp.Response;

import java.io.IOException;


public class DatabaseHelper {
	private static final String DATABASE_NAME ="database_performance_plus";
	// version 5 --> 2019-08-25
	private static final int DATABASE_VERSION =2;
	private static final String KEY_ROWID   = "_id";
	private static final String KEY_DATE   = "date";

	private static final String KEY_URL = "url";

	private static final String DATABASE_TABLE_RAPPEL = "mes_rappels";
	private static final String KEY_TITRE   = "titre";
	private static final String KEY_DESC   = "description";
	private static final String KEY_TIME = "time";

	private static final String DATABASE_TABLE_IMAGE_CALENDRIER = "image_calendrier";
	private static final String KEY_MOIS   = "mois";
	private static final String KEY_IMAGE   = "image";

	private static final String DATABASE_TABLE_DESCRIPTION_CALENDRIER = "description_calendrier";

	private static final String DATABASE_TABLE_MESSAGES = "mes_messages";

	private static final String SQL_CREATE_DESCRIPTION_CALENDRIER = "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE_DESCRIPTION_CALENDRIER + " (" +
			KEY_ROWID + " INTEGER PRIMARY KEY," +
			KEY_DATE + " TEXT," +
			KEY_MOIS + " TEXT," +
			KEY_DESC + " TEXT)";

	private static final String SQL_CREATE_IMAGE_CALENDRIER = "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE_IMAGE_CALENDRIER + " (" +
			KEY_ROWID + " INTEGER PRIMARY KEY," +
			KEY_MOIS + " NUMERIC," +
			KEY_IMAGE + " BLOB)";

	private static final String SQL_CREATE_RAPPEL = "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE_RAPPEL + " (" +
			KEY_ROWID + " INTEGER PRIMARY KEY," +
			KEY_TIME + " TEXT," +
			KEY_TITRE + " TEXT," +
			KEY_DESC + " TEXT," +
			KEY_DATE + " TEXT)";


	private static final String SQL_CREATE_MESSAGES = "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE_MESSAGES + " (" +
			KEY_ROWID + " INTEGER PRIMARY KEY," +
			KEY_TITRE + " TEXT," +
			KEY_DATE + " TEXT," +
			KEY_DESC + " TEXT," +
			KEY_URL + " TEXT)";


	
	private DbHelper mHelper;
	private final Context mContext;
	private SQLiteDatabase mDatabase;




	private static class DbHelper extends SQLiteOpenHelper {



		private DbHelper(Context context) {
			
			
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
	
		}

		@Override
		public void onCreate(SQLiteDatabase db) {


			db.execSQL(SQL_CREATE_DESCRIPTION_CALENDRIER);
			db.execSQL(SQL_CREATE_IMAGE_CALENDRIER);
			db.execSQL(SQL_CREATE_RAPPEL);
			db.execSQL(SQL_CREATE_MESSAGES);


		}




        @Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


			
		}
	}
	
	public DatabaseHelper(Context c) {
		mContext = c;
	}
	
    public void open() throws SQLException{
		
		
		mHelper = new DbHelper(mContext);
		
		mDatabase = mHelper.getWritableDatabase();

	}
	
	public void close(){
		mHelper.close();
	}
	


	public Cursor getListeRappel() {
		
		String buildSQL = "SELECT * FROM " + DATABASE_TABLE_RAPPEL; 
		return mDatabase.rawQuery(buildSQL, null);
	}
	
	public Cursor getListeRappelModif(int mId) {
		
		String buildSQL = "SELECT * FROM " + DATABASE_TABLE_RAPPEL + " WHERE _id=" + mId; 
		return mDatabase.rawQuery(buildSQL, null);
	}
	
	public void insertMesRappels(String titre, String date, String desc, String time){
		
			ContentValues args = new ContentValues();
		    args.put(KEY_TITRE, titre);
		    args.put(KEY_DATE, date);
		    args.put(KEY_DESC, desc);
		    args.put(KEY_TIME, time);
		    mDatabase.insert(DATABASE_TABLE_RAPPEL, null, args);
		}
	
	public void deleteMesRappels(int mId){
		mDatabase.delete(DATABASE_TABLE_RAPPEL, KEY_ROWID + "=" + mId, null);	
	}
	
	public void updateMesRappels(String titre, String date, String desc, String time, int mId){
		//	int val = 1;
		ContentValues args = new ContentValues();
	    args.put(KEY_TITRE, titre);
	    args.put(KEY_DATE, date);
	    args.put(KEY_DESC, desc);
	    args.put(KEY_TIME, time);
		    mDatabase.update(DATABASE_TABLE_RAPPEL, args, "_id=" + mId, null );
			
		}

	public Cursor getNextId() {
		String buildSQL = "SELECT _id FROM mes_rappels ORDER BY _id DESC LIMIT 1";
		return mDatabase.rawQuery(buildSQL, null);
	}

	public Cursor checkEmpty() {
		
		String testEmptyness = "SELECT * FROM mes_rappels";
		
		
		return mDatabase.rawQuery(testEmptyness, null);
	}
	




	public void deleteImageCalendrier() {
		mDatabase.delete(DATABASE_TABLE_IMAGE_CALENDRIER, null, null);		
	}

	public void insertCalendrierImage(int i, Response entity) throws IOException {
		ContentValues values = new ContentValues();

		try {
			values.put(KEY_IMAGE, entity.body().bytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		values.put(KEY_MOIS, i);
		mDatabase.insert(DATABASE_TABLE_IMAGE_CALENDRIER, null, values);
		}

	public Cursor getImageCalendrier(int imageMonth) {
		String buildSQL = "SELECT * FROM image_calendrier WHERE mois=" + imageMonth;
		return mDatabase.rawQuery(buildSQL, null);

	}

	public Cursor getListeMessages() {
		String buildSQL = "SELECT * FROM " + DATABASE_TABLE_MESSAGES;
		return mDatabase.rawQuery(buildSQL, null);
	}

	public void deleteMesMessages(int mId) {
		mDatabase.delete(DATABASE_TABLE_MESSAGES, KEY_ROWID + "=" + mId, null);
	}

	public void insertMesMessages(String titre, String date, String desc, String url){

		ContentValues args = new ContentValues();
		args.put(KEY_TITRE, titre);
		args.put(KEY_DATE, date);
		args.put(KEY_DESC, desc);
		args.put(KEY_URL, url);
		mDatabase.insert(DATABASE_TABLE_MESSAGES, null, args);
	}
}
