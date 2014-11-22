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
import android.widget.Toast;

public class NotificationActivity extends Activity {

	final int REQUEST_CODE_MAIL = 1;
	final int REQUEST_CODE_SMS = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		sendEmail(getIntent().getStringArrayListExtra("Email"),"Teste");

	}

	public void sendEmail(ArrayList<String> contacts, String template){
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.setType("message/rfc822");

		String[] emails = new String[contacts.size()];

		for (int i = 0; i < contacts.size(); i++) {

			emails[i] = contacts.get(i);

		}

		emailIntent.putExtra(Intent.EXTRA_EMAIL  , emails);
		emailIntent.putExtra(Intent.EXTRA_TEXT, Integer.toString(contacts.size()));

		try {
			startActivityForResult(Intent.createChooser(emailIntent, "Send mail..."),REQUEST_CODE_MAIL);
			//finish();
			Log.i("Finished sending email...", "");
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
		}

	}

	public void sendSMS(ArrayList<String> contacts, String template){

		Intent smsIntent = new Intent(Intent.ACTION_SENDTO,Uri.parse("smsto:"+contacts.toArray()[0]));
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
			sendSMS(getIntent().getStringArrayListExtra("Phones"), "Teste");
			break;
		case REQUEST_CODE_SMS:
			Intent intent = new Intent(this, EventSecondStepActivity.class);
			//attemptLogin();
			startActivity(intent);
			Toast.makeText(this, "SUCH SUCCESS.WOW!", Toast.LENGTH_SHORT).show();
			break;
		}
	}
}
