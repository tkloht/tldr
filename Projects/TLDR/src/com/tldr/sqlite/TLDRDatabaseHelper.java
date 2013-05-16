package com.tldr.sqlite;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class TLDRDatabaseHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME="tldr_db";
	public static final int DATABASE_VERSION=1;
	public static final String USERAUTH_TABLE_NAME="user";
	public static final String USERAUTH_TABLE_CREATE="CREATE TABLE " + USERAUTH_TABLE_NAME + " (" + "id INTEGER PRIMARY KEY AUTOINCREMENT," +  "user_name TEXT NOT NULL," + "email TEXT NOT NULL," + "password TEXT NOT NULL)";
	
	
	
	public TLDRDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null,  DATABASE_VERSION );
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(USERAUTH_TABLE_CREATE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
