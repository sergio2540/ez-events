package com.ezevents.android.app;

import java.util.ArrayList;
import java.util.List;





import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class CheckListActivity extends Activity {

    private final List<String> checkList = new ArrayList<String>();

    private int selected = 0; 

    private final ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

	// Called when the action mode is created; startActionMode() was called
	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
	    // Inflate a menu resource providing context menu items
	    MenuInflater inflater = mode.getMenuInflater();
	    inflater.inflate(R.menu.check_element, menu);
	    return true;
	}

	// Called each time the action mode is shown. Always called after onCreateActionMode, but
	// may be called multiple times if the mode is invalidated.
	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
	    return false; // Return false if nothing is done
	}

	// Called when the user selects a contextual menu item
	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
	    int id = item.getItemId();
	    if (id == R.id.action_edit_todo){
		showDialog(ItemOption.EDIT,titleEdit,positiveEdit);
		mode.finish(); // Action picked, so close the CAB
		return true;
	    }
	    if (id == R.id.action_remove_todo){
		removeTODO(selected);
		mode.finish(); // Action picked, so close the CAB
		return true;
	    }
	    else {
		return false;
	    }
	}


	private void removeTODO(int selected) {

	    checkListAdapter.remove(checkList.get(selected));

	}



	// Called when the user exits the action mode
	@Override
	public void onDestroyActionMode(ActionMode mode) {
	    mActionMode = null;
	}
    };

    protected Object mActionMode;

    private ArrayAdapter<String> checkListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_check_list);

	

	ListView checkListView =  (ListView)findViewById(R.id.checkList);
	checkListAdapter = new ArrayAdapter<String>(this,R.layout.check_list_view, checkList);
	checkListAdapter.notifyDataSetChanged();


	checkListView.setOnItemLongClickListener(new OnItemLongClickListener() {


	    @Override
	    public boolean onItemLongClick(AdapterView<?> adapter, View view,
		    int index, long arg3) {

		if (mActionMode != null) {
		    return false;
		}

		selected = index;

		// Start the CAB using the ActionMode.Callback defined above
		mActionMode = CheckListActivity.this.startActionMode(mActionModeCallback);
		view.setSelected(true);

		return false;
	    }
	});

	checkListView.setAdapter(checkListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.check_list, menu);
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
	else if (id == R.id.action_add_todo)
	{
	    showDialog(ItemOption.ADD,titleAdd,positiveAdd );

	}else if (id == R.id.action_next_checklist)
	{
	    Intent intent = getIntent();
	    intent.setClass(this, GuestsActivity.class);
	    intent.putExtra("CheckList", checkList.toArray(new String[checkList.size()]));
	    startActivity(intent);

	}
	return super.onOptionsItemSelected(item);
    }

    private enum ItemOption {
	ADD,EDIT,
    }

    //ADD POPUP INFO
    private final String titleAdd = "Add TODO";
    private final String positiveAdd = "Add";

    //EDIT POPUP INFO
    String titleEdit = "Edit TODO";
    String positiveEdit = "Save";

    private void showDialog(final ItemOption type, String title, String positive) {
	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	builder.setTitle(title);

	// Set up the input
	final EditText input = new EditText(this);
	// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
	input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT | InputType.TYPE_TEXT_FLAG_MULTI_LINE );
	builder.setView(input);

	if (type == ItemOption.EDIT){
	    String text_to_edit = checkList.get(selected);
	    input.setText(text_to_edit);
	    input.setSelection(text_to_edit.trim().length());
	}

	// Set up the buttons
	builder.setPositiveButton(positive, new DialogInterface.OnClickListener() { 
	    @Override
	    public void onClick(DialogInterface dialog, int which) {

		if (type == ItemOption.ADD)
		    checkList.add(input.getText().toString());
		else if (type == ItemOption.EDIT){
		    ((ArrayList<String>)checkList).set(selected,input.getText().toString());
		}
	    } 
	});
	builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
		dialog.cancel();
	    }
	});

	builder.show();
    }

}
