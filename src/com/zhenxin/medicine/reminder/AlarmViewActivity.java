package com.zhenxin.medicine.reminder;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * This class deals with the summary of alarm screen, with the option to edit
 * @author Ivan
 *
 */
public class AlarmViewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_view);
		Intent intent = getIntent();
		// Set medicine name
		String text = intent.getStringArrayExtra(AlarmManagerBroadcastReceiver.MEDICINE_NAME_KEY).toString();
		TextView medicineNameView = (TextView) findViewById(R.id.view_medicine_name);	// or R.id.view_medicine_name
		medicineNameView.setText(text);
		
		// Set number of pills per instance
		int numPills = intent.getIntExtra(AlarmManagerBroadcastReceiver.NUM_PILLS_KEY, 0);
		TextView numPillsView = (TextView) findViewById(R.id.view_num_pills);
		numPillsView.setText(numPills);
		
		// Set alarm times of when it is active now
		// This is a challenge because the AlarmManagerBroadcastReceiver needs to save instances of it.
		List<String> timeList = new ArrayList<String>();
		// TODO: process timeList
		
        ListAdapter listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, timeList);
        ListView listView = (ListView) findViewById(R.id.view_alarm_times);
        listView.setAdapter(listAdapter);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.alarm_view, menu);
		return true;
	}
	
	public void editAlarmView(View view)	{
		
	}

}
