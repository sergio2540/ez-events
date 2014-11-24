package com.ezevents.android.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class AsyncPersistence extends AsyncTask<String, Void, String> {

	Activity previous = null;
	Intent intent = null;

	public AsyncPersistence(Intent intent){
		this.intent = intent;

	}

	@Override
	protected String doInBackground(String... urls) {
		String creator = intent.getStringExtra("Username");
		String title = intent.getStringExtra("Title");
		String description = intent.getStringExtra("Description");
		String date = intent.getStringExtra("Date");
		String time = intent.getStringExtra("Time");
		String place = "https://maps.google?q=" + intent.getStringExtra("Lat") + "," + intent.getStringExtra("Lon");

		String tableEvent = null;

		tableEvent = "https://web.ist.utl.pt/ist170515/save-event.php?Creator="+creator+"&Title="+title+"&Description="+description+"&Date="+date+"&Time="+time+"&Place="+place;
		
		
		String result = GET(tableEvent);

		String tableGuests;
		ArrayList<String> guestEmails = intent.getStringArrayListExtra("Emails");
		for(String email : guestEmails){

			String emailUsername = GET("https://web.ist.utl.pt/ist170515/check-user.php?Email="+email);

			if(!emailUsername.equals(""))
				GET("https://web.ist.utl.pt/ist170515/add-guest.php?Creator="+creator+"&Date="+date+"&Time="+time+"&Guest="+emailUsername);
		}

		ArrayList<String> checkList = intent.getStringArrayListExtra("CheckList");
		for(String item : checkList){

			GET("https://web.ist.utl.pt/ist170515/add-resource.php?Creator="+creator+"&Date="+date+"&Time="+time+"&Resource="+item);
		}
		
		return "";

	}
	// onPostExecute displays the results of the AsyncTask.
	@Override
	protected void onPostExecute(String result) {
		

	}

	public static String GET(String url){
		InputStream inputStream = null;
		String result = "";
		try {

			// create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			// make GET request to the given URL
			HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

			// receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();

			// convert inputstream to string
			if(inputStream != null)
				result = convertInputStreamToString(inputStream);
			else
				result = "Did not work!";

		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}

		return result;
	}
	private static String convertInputStreamToString(InputStream inputStream) throws IOException{
		BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

	}

}
