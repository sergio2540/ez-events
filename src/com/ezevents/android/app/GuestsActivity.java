package com.ezevents.android.app;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemLongClickListener;

public class GuestsActivity extends Activity {

	private final List<String> guestList = new ArrayList<String>();
	private ArrayAdapter<String> checkListAdapter;
	private ListView guestListView; 


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guests);
		guestListView = (ListView)findViewById(R.id.guestList);
		checkListAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice, guestList);
		checkListAdapter.notifyDataSetChanged();
		guestListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		guestListView.setAdapter(checkListAdapter);

		ContentResolver cr = getContentResolver();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		if (cur.getCount() > 0) {
			while (cur.moveToNext()) {
				String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
				String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				Cursor emailCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{id}, null); 
				if (emailCur != null) { 
					// This would allow you get several email addresses
					// if the email addresses were stored in an array
					//int index = emailCur.getColumnName(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
					try{
						String email = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
						String guest = name + " " + email;
						guestList.add(guest);
						emailCur.close();

					}catch(Exception e){

						Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?", new String[]{id}, null);
						if (pCur != null) {
							try{
								String phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA));
								String guest = name + phone;
								guestList.add(guest);
								pCur.close();
							}catch (Exception e1){

								continue;
							}
						}
					}
				} else{

					Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?", new String[]{id}, null);
					if (pCur != null) {

						try{
							String phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA));
							String guest = name + phone;
							guestList.add(guest);
							pCur.close();
						}catch(Exception e){


						}
					}


				}

			}
		}

		Button mFirstNextButton = (Button) findViewById(R.id.guestNext);

		mFirstNextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				//Intent intent = new Intent(view.getContext(), NotificationActivity.class);
				//attemptLogin();
				Intent intent = getIntent();
				intent.setClass(view.getContext(), NotificationActivity.class);

				SparseBooleanArray checked = guestListView.getCheckedItemPositions();
				ArrayList<String> selectedEmails = new ArrayList<String>();
				ArrayList<String> selectedPhones = new ArrayList<String>();

				for (int i = 0; i < checked.size(); i++) {
					// Item position in adapter
					// Add sport if it is checked i.e.) == TRUE!
					if (checked.valueAt(i)){
						String selected = guestList.get(i);

						if(selected.contains("@")){
							selectedEmails.add(guestList.get(i));

						}else 
							selectedPhones.add(guestList.get(i));
					}
				}

				intent.putExtra("Email",selectedEmails);
				intent.putExtra("Phones", selectedPhones);
				startActivity(intent);

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.guests, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}





}
