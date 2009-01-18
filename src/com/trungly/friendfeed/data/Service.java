package com.trungly.friendfeed.data;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.trungly.friendfeed.R;
import com.trungly.friendfeed.R.drawable;

public class Service {
	
	private String mId;
	private String mName;
	private String mProfileUrl; // TODO: change to URL type?
	private int mIconResource;
	
	public Service(JSONObject json) {
		try {
	    	mId = json.getString("id");
	    	mName = json.getString("name");
	    	mProfileUrl = json.getString("profileUrl");

	    	if ("facebook".equals(mId)) {
	    		mIconResource = R.drawable.facebook;
	    	}
	    	else if ("blog".equals(mId)) {
	    		mIconResource = R.drawable.blogger;
	    	}
	    	else if ("twitter".equals(mId)) {
	    		mIconResource = R.drawable.twitter;
	    	}
	    	else if ("netflix".equals(mId)) {
	    		mIconResource = R.drawable.netflix;
	    	}
	    	else if ("goodreads".equals(mId)) {
	    		mIconResource = R.drawable.blogger;
	    	}
	    	else if ("picasa".equals(mId)) {
	    		mIconResource = R.drawable.picasa;
	    	}
	    	else if ("googlereader".equals(mId)) {
	    		mIconResource = R.drawable.googlereader;
	    	}
	    	else if ("delicious".equals(mId)) {
	    		mIconResource = R.drawable.delicious;
	    	}
	    	else if ("flickr".equals(mId)) {
	    		mIconResource = R.drawable.flickr;
	    	}
	    	else if ("internal".equals(mId)) {
	    		mIconResource = R.drawable.ff;
	    	}
	    	else if ("feed".equals(mId)) {
	    		mIconResource = R.drawable.feed;
	    	}
	    	else {
	    		mIconResource = R.drawable.feed;
	    	}
	    	//TODO: Add the rest you lazy bum
	    }
	    catch (JSONException je) {
	    	je.printStackTrace();
	    }
	}

	public int getIconResource() {
		return mIconResource;
	}

	public void setIconResource(int iconResource) {
		mIconResource = iconResource;
	}

	public String getId() {
		return mId;
	}

	public void setId(String id) {
		mId = id;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public String getProfileUrl() {
		return mProfileUrl;
	}

	public void setProfileUrl(String profileUrl) {
		mProfileUrl = profileUrl;
	}

	public long save(SQLiteDatabase db) {
		ContentValues values = new ContentValues();
		values.put("name", mName);
		values.put("profile_url", mProfileUrl);
		return db.insertOrThrow("services", null, values);
	}
}
