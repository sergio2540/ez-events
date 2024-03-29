package com.ezevents.android.app;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract.EventsEntity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class EventFirstStepActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_event_first_step);

	
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.event_first_step, menu);
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
	else if (id == R.id.action_basic_info_next) {
	    
	    Intent intent = getIntent();
	    intent.setClass(this, EventSecondStepActivity.class);
	    intent.putExtra("Title", ((EditText)findViewById(R.id.editText1)).getText().toString());
	    intent.putExtra("Description", ((EditText)findViewById(R.id.editText2)).getText().toString());
	    startActivity(intent);
	
	}

	return super.onOptionsItemSelected(item);
    }
}
