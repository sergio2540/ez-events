package com.ezevents.android.app;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class OfferHelpActivity extends Activity {

	private final List<String> selectedItemsList = new ArrayList<String>();
	private ArrayAdapter<String> selectedItemsListAdaper;
	private ListView selectedItemsListView; 
	public static Intent intent;
	String username;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_offer_help);
		intent = getIntent();
		String[] items = intent.getStringArrayExtra("Items");
		username = getIntent().getStringExtra("Username");
		
		for(String item : getIntent().getStringArrayListExtra("Items")){
        	if(!item.equals(""))
			selectedItemsList.add(item);
			
		}
		
		selectedItemsListView = (ListView)findViewById(R.id.offerHelp);
		selectedItemsListAdaper = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice, selectedItemsList);
		selectedItemsListAdaper.notifyDataSetChanged();
		selectedItemsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		selectedItemsListView.setAdapter(selectedItemsListAdaper);
		
	

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.offer_help, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_offerHelp_next) {
			Intent intent = getIntent();
			intent.setClass(this, NotificationActivity.class);

			SparseBooleanArray checked = selectedItemsListView.getCheckedItemPositions();
			ArrayList<String> selectedItems = new ArrayList<String>();

			for (int i = 0; i < checked.size(); i++) {
				// Item position in adapter
				// Add sport if it is checked i.e.) == TRUE!
				if (checked.valueAt(i)){

					String selected =  selectedItemsListView.getAdapter().getItem(
							checked.keyAt(i)).toString();

					selectedItems.add(selected);

				}
		
			}
			
			new AsyncOfferHelp(getIntent().getStringExtra("Username"), selectedItems).execute();
		}

		return super.onOptionsItemSelected(item);
	}
}
