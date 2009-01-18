package com.trungly.friendfeed;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.trungly.friendfeed.data.Comment;
import com.trungly.friendfeed.data.Entry;

public class Home extends ListActivity {
	private DbHelper dbHelper;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		dbHelper =  new DbHelper(this);

		try {
			String responseBody = getFriendFeed("trungly", "dryad385pents");

			JSONObject feed = new JSONObject(responseBody);
			JSONArray entries = feed.getJSONArray("entries");

			LinearLayout layout = (LinearLayout) findViewById(R.id.main_window);
			
			// delete all data
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			db.delete("entries", "1", null);
			db.delete("users", "1", null);
			db.delete("services", "1", null);
			db.delete("comments", "1", null);
			
			for (int i = 0; i < entries.length(); i++) {
				Entry entry = new Entry(entries.getJSONObject(i));

				View entryView = entry.getView(this);
				TextView separator = new TextView(this);
				separator.setText("");
				layout.addView(separator);
				layout.addView(entryView);
				
				ArrayList<Comment> comments = entry.getComments();
				for (Comment comment : comments) {
					layout.addView(comment.getView(this));
				}

				entry.save(db);
			}
		} catch (IOException ioe) {
			//TODO: show error to user
		} catch (JSONException je) {
			//TODO: show error to user
		} catch (AuthenticationException ae) {
			//TODO: show error to user
		} finally {
			dbHelper.close();
		}
		
		
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, textArrayResId, textViewResId) 
			
			new ArrayAdapter(this, R.layout.entry_item, cursor, FROM, TO);
		setListAdapter(adapter);
	}

	private String getFriendFeed(String username, String password) throws IOException, AuthenticationException {
		String url = "http://friendfeed.com/api/feed/user/" + username + "/friends";
		String responseBody = new String();
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
//		client.getParams().setAuthenticationPreemptive(true);
		Credentials creds = new UsernamePasswordCredentials(username, password);
//		client.getState().setCredentials(new AuthScope("myhost", 80, AuthScope.ANY_REALM), defaultcreds);
		BasicScheme scheme = new BasicScheme();
		
		try {
			scheme.authenticate(creds, request);
			HttpResponse response = client.execute(request);

			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new IOException("Error from service: " + response.getStatusLine().getReasonPhrase());
			}
			responseBody = new BasicResponseHandler().handleResponse(response);
		} catch (IOException ioe) {
			throw ioe;
		} catch (AuthenticationException ae) {
			throw ae;
		}

		return responseBody;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

}