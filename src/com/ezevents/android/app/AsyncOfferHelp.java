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

public class AsyncOfferHelp extends AsyncTask<String, Void, String> {

	Activity previous = null;
	String username;
	private ArrayList<String> selectedItems;
	String creator;
	String date;
	String time;

	public AsyncOfferHelp(String username, ArrayList<String> selectedItems) {
		// TODO Auto-generated constructor stub
		this.username = username;
		this.selectedItems = selectedItems;
		this.creator = OfferHelpActivity.intent.getStringExtra("Creator");
		this.date = OfferHelpActivity.intent.getStringExtra("Date");
		this.time = OfferHelpActivity.intent.getStringExtra("Time");

	}
	@Override
	protected String doInBackground(String... urls) {
		String url = null;
		String result = null;
		for (String item : selectedItems){
			url = "https://web.ist.utl.pt/ist170515/set-helper.php?Creator="+creator+"&Date="+date+"&Time="+time+"&Resource="+item+"&Helper="+username;
			result = GET(url);
		}
		return "";
	}
	// onPostExecute displays the results of the AsyncTask.
	@Override
	protected void onPostExecute(String result) {


		//if(result.equals("1")){
		Intent intent = new Intent(MainActivity.cont,MainActivity.class);
		intent.putExtra("Username", username);
		LoginActivity.context.startActivity(intent);
		//}
		//else {

		//	Toast.makeText(LoginActivity.context, "Wrong user or password.", Toast.LENGTH_LONG).show();
		//}
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
