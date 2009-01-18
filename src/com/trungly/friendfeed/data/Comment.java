package com.trungly.friendfeed.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.trungly.friendfeed.R;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Comment {
	
	private String mIdentifier;
	private String mBody;
	private User mUser;
//	private Date mDate;
	private String mDate;
	
	public Comment(JSONObject json) {
		try {
			mIdentifier = json.getString("id");
			mBody = json.getString("body");
			mUser = new User(json.getJSONObject("user"));
//	    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
//	    	mDate = sdf.parse(json.getString("date"));
			mDate = json.getString("date");
		}
//		catch (ParseException pe) {
//			pe.printStackTrace();
//		}
		catch (JSONException je) {
	    	je.printStackTrace();
	    }
	}

	public String getBody() {
		return mBody;
	}

	public void setBody(String body) {
		mBody = body;
	}

	public User getUser() {
		return mUser;
	}

	public void setUser(User user) {
		mUser = user;
	}

//	public Date getDate() {
//		return mDate;
//	}
//
//	public void setDate(Date date) {
//		mDate = date;
//	}

	public View getView(Activity activity) {
		LayoutInflater inf = activity.getLayoutInflater();
		View commentView = inf.inflate(R.layout.comment_item, new LinearLayout(activity));
		
		ImageView commentIcon = (ImageView) commentView.findViewById(R.id.comment_icon);
		commentIcon.setImageResource(R.drawable.comment_friend);
		
		TextView commentText = (TextView) commentView.findViewById(R.id.comment_body);
		commentText.setText(" " + mBody + " - <i>" + mUser.getName() + "</i>");
		
		return commentView;
	}

	public long save(SQLiteDatabase db, long entryId) {
		ContentValues values = new ContentValues();
		values.put("identifier", mIdentifier);
		values.put("entry_id", entryId);
		values.put("body", mBody);
		long userId = mUser.save(db);
		values.put("user_id", userId);
		values.put("date", mDate);
		return db.insertOrThrow("comments", null, values);
	}

}
