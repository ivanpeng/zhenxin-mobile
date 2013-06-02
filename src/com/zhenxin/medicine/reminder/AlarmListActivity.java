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
	
	public final static String TIME_LIST_KEY = "TIME_LIST";
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_list_layout);
        ListAdapter adapter = createAdapter();
        setListAdapter(adapter);
        //setListAdapter(ArrayAdapter.createFromResource(getApplicationContext(),R.array.tut_titles, R.layout.list_item));
    }
    
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	// TODO: need to handle list item clicks, now it just all points to alarm manager
    	Intent intent = new Intent(this, AlarmViewActivity.class);
    	ListAdapter adapter = getListAdapter();
    	// Send over extra properties to the view class
    	String medicineName = (String) adapter.getItem(position);
    	//AlarmManagerBroadcastReceiver alarm = getAlarmByPosition(position);
    	// Now that we have alarm instance, populate the intent with its properties
    	intent.putExtra(AlarmManagerBroadcastReceiver.MEDICINE_NAME_KEY, medicineName);
    	
    	//intent.putExtra(AlarmManagerBroadcastReceiver.NUM_PILLS_KEY, alarm.getNumPills());
    	int pillFrequency = 2;
    	intent.putExtra(AlarmManagerBroadcastReceiver.NUM_PILLS_KEY, pillFrequency);
    	// Do a little bit of processing with the pill frequency
    	// Algorithm: 8am initial time, partition day into 12/(n) intervals
    	String [] reminderTimes = new String[pillFrequency];
    	int defaultStartTime = 8;
    	int interval;
    	if (pillFrequency == 1)
    		interval = 0;
    	else
    		interval = 12/(reminderTimes.length-1);
    	for (int i = 0; i < reminderTimes.length; i++)	{
    		int temp = defaultStartTime + i*interval;
    		reminderTimes[i] = temp + ":00";
    	}
    	intent.putExtra(AlarmListActivity.TIME_LIST_KEY, reminderTimes);
    	startActivity(intent);
    }
    
    private AlarmManagerBroadcastReceiver getAlarmByPosition(int position) {
		// TODO Auto-generated method stub
		return null;
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
