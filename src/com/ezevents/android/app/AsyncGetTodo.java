package com.ezevents.android.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

public class AsyncGetTodo extends AsyncTask<String, Void, String> {
	
	static ArrayList<String> itemsList; 
	String username;
	String[] labelFields;
	public AsyncGetTodo(String username) {
		// TODO Auto-generated constructor stub
		this.username = username;
	}
	@Override
	protected String doInBackground(String... eventLabel) {
		
		labelFields = eventLabel[0].split("\n");
		itemsList = new ArrayList<String>();
		
		String username = MainActivity.intent.getStringExtra("Username");
		String query = "https://web.ist.utl.pt/ist170515/get-todo.php?Creator="+labelFields[0]+"&Date="+labelFields[3]+"&Time="+labelFields[4];
		
		String result = GET(query);
		return result;
		
	}
	// onPostExecute displays the results of the AsyncTask.
	@Override
	protected void onPostExecute(String result) {
		
		Intent intent = new Intent(MainActivity.cont, OfferHelpActivity.class);
		intent.putExtra("Items", itemsList);
		intent.putExtra("Username", username);
		intent.putExtra("Creator",labelFields[0]);
		intent.putExtra("Date", labelFields[3] );
		intent.putExtra("Time", labelFields[4] );		
		
		MainActivity.cont.startActivity(intent);
		
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

        
        String [] items = result.split(Pattern.quote("|"));
        
        for(String item : items){
        	itemsList.add(item);	
        	
        }

        return result;

    }

}
