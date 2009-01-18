package com.trungly.friendfeed.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trungly.friendfeed.DbHelper;
import com.trungly.friendfeed.R;

public class Entry {
	
	private String mIdentifier;
	private String mHeaderText;
	private String mBodyText;
//	private Date mDate;
	private String mDate;
	private Service mService;
	private User mUser;
	private ArrayList<Comment> mComments;
	
	public Entry(JSONObject json) {
		try {
			mIdentifier = json.getString("id");
			mService = new Service(json.getJSONObject("service"));
			mUser = new User(json.getJSONObject("user"));
			String userName = mUser.getName(); 
			
			mHeaderText = (userName == null ? "" : mUser.getName() + " - ") + mService.getName();
	    	mBodyText = json.getString("title");
	    	
//	    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
//	    	mDate = sdf.parse(json.getString("published"));
	    	mDate = json.getString("published");
	    	
	    	JSONArray comments = json.getJSONArray("comments");
	    	mComments = new ArrayList<Comment>();
	    	for (int i = 0; i < comments.length(); i++) {
	    		mComments.add(new Comment(comments.getJSONObject(i)));
	    	}
	    }
//		catch (ParseException pe) {
//			pe.printStackTrace();
//		}
	    catch (JSONException je) {
	    	je.printStackTrace();
		}
	}
	
	public View getView(Activity activity) {
		LayoutInflater inf = activity.getLayoutInflater();
		LinearLayout entryView = (LinearLayout) inf.inflate(R.layout.entry_item, new LinearLayout(activity));
		
		ImageView serviceIcon = (ImageView) entryView.findViewById(R.id.service_icon);
		serviceIcon.setImageResource(mService.getIconResource());
		
		TextView headerText = (TextView) entryView.findViewById(R.id.header_text);
		headerText.setText(" " + mHeaderText);
		
		TextView bodyText = (TextView) entryView.findViewById(R.id.body);
		bodyText.setText(mBodyText);
		
		TextView footerText = (TextView) entryView.findViewById(R.id.footer);
		footerText.setText(mDate.toString());
		
		return entryView;
	}

	public ArrayList<Comment> getComments() {
		return mComments;
	}

	public void setComments(ArrayList<Comment> comments) {
		mComments = comments;
	}

	public long save(SQLiteDatabase db) {
		
		// TODO: Do all saves in a transaction
		long serviceId = mService.save(db);
		long userId = mUser.save(db);
		
//		if (serviceId == -1 || userId == -1) return -1;

		ContentValues values = new ContentValues();
		values.put("identifier", mIdentifier);
		values.put("header", mHeaderText);
		values.put("body", mBodyText);
		values.put("date", mDate);
		values.put("user_id", userId);
		values.put("service_id", serviceId);
		
		long entryId = db.insertOrThrow("entries", null, values);
		
		for (Comment comment : mComments) {
//			if (comment.save(db) == -1) return -1;
			comment.save(db, entryId);
		}

		return entryId;
	}

}
