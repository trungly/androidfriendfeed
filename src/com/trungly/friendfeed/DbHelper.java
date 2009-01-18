package com.trungly.friendfeed;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DbHelper extends SQLiteOpenHelper {

	public DbHelper(Context context) {
		super(context, "friendfeed.db", null, 2);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE entries ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"identifier TEXT NOT NULL, " +
				"header TEXT, " +
				"body TEXT, " +
				"date INTEGER, " +
				"user_id INTEGER, " +
				"service_id INTEGER );");
		db.execSQL("CREATE TABLE services ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"name TEXT NOT NULL, " +
				"profile_url TEXT );");
		db.execSQL("CREATE TABLE users ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"identifier TEXT NOT NULL, " +
				"name TEXT );");
		db.execSQL("CREATE TABLE comments ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"identifier TEXT NOT NULL, " +
				"entry_id INTEGER, " +
				"body TEXT, " +
				"user_id INTEGER, " +
				"date INTEGER );");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS entries");
		db.execSQL("DROP TABLE IF EXISTS services");
		db.execSQL("DROP TABLE IF EXISTS comments");
		db.execSQL("DROP TABLE IF EXISTS users");

		onCreate(db);
	}

}
