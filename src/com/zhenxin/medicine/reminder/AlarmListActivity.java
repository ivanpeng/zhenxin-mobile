package com.zhenxin.medicine.reminder;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
 
public class AlarmListActivity extends ListActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_list_layout);
        ListAdapter adapter = createAdapter();
        setListAdapter(adapter);
        //setListAdapter(ArrayAdapter.createFromResource(getApplicationContext(),R.array.tut_titles, R.layout.list_item));
    }
    
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	// TODO: need to handle list item clicks, now it just all points to alarm manager
    	Intent intent = new Intent(this, AlarmManagerActivity.class);
    	ListAdapter adapter = getListAdapter();
    	String medicineName = (String) adapter.getItem(position);
    	intent.putExtra(AlarmManagerBroadcastReceiver.MEDICINE_NAME_KEY, medicineName);
    	startActivity(intent);
    }
    protected ListAdapter createAdapter()
    {
    	// Create some mock data
    	String[] testValues = new String[] {
    			"Fluoxetine",
    			"Zolpidem",
    			"Haloperidol"
    	};
 
    	// Create a simple array adapter (of type string) with the test values
    	ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, testValues);
 
    	return adapter;
    }
}
