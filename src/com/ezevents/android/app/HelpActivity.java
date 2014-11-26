package com.ezevents.android.app;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class HelpActivity extends Activity {

	private final List<String> helpList = new ArrayList<String>();
	private ArrayAdapter<String> helpListAdapter;
	private ListView helpListView; 
	private String username;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		

		helpListView = (ListView)findViewById(R.id.helpList);
		helpListAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, helpList);
		helpListAdapter.notifyDataSetChanged();
		helpListView.setAdapter(helpListAdapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.help, menu);
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
		else if (id == R.id.action_help_next) {
			

			SparseBooleanArray checked = helpListView.getCheckedItemPositions();
			ArrayList<String> selectedItems = new ArrayList<String>();

			for (int i = 0; i < checked.size(); i++) {
				// Item position in adapter
				// Add sport if it is checked i.e.) == TRUE!
				if (checked.valueAt(i)){

					String selected =  helpListView.getAdapter().getItem(
							checked.keyAt(i)).toString();

				
				}
			}
			
			new AsyncHelp(getIntent().getStringExtra("Username"),selectedItems).execute();

		}
		return super.onOptionsItemSelected(item);
	}




}
