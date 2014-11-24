package com.ezevents.android.app;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class NotificationActivity extends Activity {

	final int REQUEST_CODE_MAIL = 1;
	final int REQUEST_CODE_SMS = 2;
	
	String title = "";
	String description = "";
	String date = "";
	String time = "";
	String place = "https://maps.google?q=";
	String checkList = "";
	String message = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		
		title = getIntent().getStringExtra("Title");
		description = getIntent().getStringExtra("Description");
		date = getIntent().getStringExtra("Date");
		time = getIntent().getStringExtra("Time");
		place += getIntent().getStringExtra("Lat") + "," + getIntent().getStringExtra("Lon");
		String[] checkListTemp = getIntent().getStringArrayExtra("CheckList");
		if(checkListTemp != null)
		for(String item : checkListTemp){
				checkList+= (item + "\n");
			
		}

		message = "Hi!\n You have been invited to the " + title + " event : " + description + ".\n The event will take place " + date + " at " +time+ " in " + place + ".\n" + "Please attent to the following checklist:\n" + checkList + "\n. Be there :)"; 
		
		if(getIntent().getStringArrayListExtra("Emails").size() != 0)
			sendEmail(getIntent().getStringArrayListExtra("Emails"),message);
		else{
			
			if(getIntent().getStringArrayListExtra("Phones").size() != 0)
				sendSMS(getIntent().getStringArrayListExtra("Phones"), message);
			else {
				new AsyncPersistence(getIntent()).execute();
				Intent intent = new Intent(this, MainActivity.class);
				startActivity(intent);	
			}
			
		}
			
		
	}

	public void sendEmail(ArrayList<String> contacts, String template){
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.setType("message/rfc822");

		String[] emails = new String[contacts.size()];

		for (int i = 0; i < contacts.size(); i++) {

			emails[i] = contacts.get(i);

		}

		emailIntent.putExtra(Intent.EXTRA_EMAIL  , emails);
		emailIntent.putExtra(Intent.EXTRA_SUBJECT  , "Ez-Events - " + title);
		emailIntent.putExtra(Intent.EXTRA_TEXT, template);

		try {
			startActivityForResult(Intent.createChooser(emailIntent, "Send mail..."),REQUEST_CODE_MAIL);
			//finish();
			Log.i("Finished sending email...", "");
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
		}

	}

	public void sendSMS(ArrayList<String> contacts, String template){
		
		String phones = "";
		
		for (String phone : contacts){
			phones += phone + ";";
			
		}

		Intent smsIntent = new Intent(Intent.ACTION_SENDTO,Uri.parse("smsto:"+phones));
		smsIntent.putExtra("sms_body", template);

		try {
			startActivityForResult(Intent.createChooser(smsIntent, "Send sms..."),REQUEST_CODE_SMS);
			//finish();
			Log.i("Finished sending email...", "");
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(this, "There is no sms client installed.", Toast.LENGTH_SHORT).show();
		}

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode) {
		case REQUEST_CODE_MAIL:
			if(getIntent().getStringArrayListExtra("Phones").size() != 0)
				sendSMS(getIntent().getStringArrayListExtra("Phones"), message);
			else {
				new AsyncPersistence(getIntent()).execute();
				Intent intent = new Intent(this, MainActivity.class);
				startActivity(intent);	
			}
			break;
		case REQUEST_CODE_SMS:
			new AsyncPersistence(getIntent()).execute();
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			break;
		}
	}

	
}
