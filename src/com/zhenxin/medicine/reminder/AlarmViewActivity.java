package com.zhenxin.medicine.reminder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
		String text = intent.getStringExtra(AlarmManagerBroadcastReceiver.MEDICINE_NAME_KEY).toString();
		TextView medicineNameView = (TextView) findViewById(R.id.view_medicine_name);	// or R.id.view_medicine_name
		medicineNameView.setText("Drug Name: " + text);
		
		// Set number of pills per instance
		int numPills = intent.getIntExtra(AlarmManagerBroadcastReceiver.NUM_PILLS_KEY, 0);
		TextView numPillsView = (TextView) findViewById(R.id.view_num_pills);
		// This is a tricky motherfucker! If you set text to just number, android will assume it's a resource ID. Remember for next time.
		numPillsView.setText("For each instance, "+ numPills + " pills will be taken.");
		/*
		// Set alarm times of when it is active now
		// This is a challenge because the AlarmManagerBroadcastReceiver needs to save instances of it.
		List<String> timeList = new ArrayList<String>();
		// TODO: process timeList
		int pillFrequency = intent.getIntExtra(AlarmManagerBroadcastReceiver.PILL_FREQUENCY_KEY, 1);
		int beginTime = 8;	// need to grab from intent eventually
		int interval = 12/pillFrequency;
		for (int i = 0; i < pillFrequency; i++)	{
			timeList.add(beginTime + ":00");
			beginTime += interval; 
		}
		*/
		String[] timeList = intent.getStringArrayExtra(AlarmListActivity.TIME_LIST_KEY);
		if (timeList == null || timeList.length == 0)
			Toast.makeText(this, "TimeList is null!", Toast.LENGTH_SHORT).show();
		else	{
			ListAdapter listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, timeList);
			ListView listView = (ListView) findViewById(R.id.view_alarm_times);
			listView.setAdapter(listAdapter);
		}
		
		// Now set functionality to the buttons.
		// If edit is pressed, then wire intent to AlarmManager, and start activity
		// If cancel is pressed, return to previous page with Activity.finish
		Button editButton = (Button) findViewById(R.id.view_edit_alarm);
		editButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view) {
				// jump into alarm Manager
				Context context = view.getContext();
				Intent intent = new Intent(context, AlarmManagerActivity.class);
				context.startActivity(intent);
			}
			
		});
		Button cancelButton = (Button) findViewById(R.id.view_cancel_alarm);
		cancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// finish the activity with cancel
				Context context = v.getContext();
				((Activity)(context)).finish();
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.alarm_view, menu);
		return true;
	}


}
