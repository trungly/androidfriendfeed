package com.trungly.friendfeed.data;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class User {

	private String mIdentifier;
	private String mName;
	
	public User(JSONObject json) {
		try {
			mIdentifier = json.getString("id");
			mName = json.getString("name");
	    }
	    catch (JSONException je) {
	    	je.printStackTrace();
	    }
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		this.mName = name;
	}

	public long save(SQLiteDatabase db) {
		ContentValues values = new ContentValues();
		values.put("identifier", mIdentifier);
		values.put("name", mName);
		return db.insertOrThrow("users", null, values);
	}

}
