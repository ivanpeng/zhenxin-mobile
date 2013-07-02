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

import com.zhenxin.medicine.R;

/**
 * This class deals with the summary of alarm screen, with the option to edit
 * @author Ivan
 *
 */
public class AlarmViewActivity extends Activity {

	private boolean resumeHasRun = false;
	// We can declare an instance of this alarm; just don't call setAlarm here and we'll be fine
	private AlarmBroadcastReceiverWrapper alarms;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_view);
		Intent intent = getIntent();
		// Set medicine name
		final String text = intent.getStringExtra(AlarmManagerBroadcastReceiver.MEDICINE_NAME_KEY).toString();
		TextView medicineNameView = (TextView) findViewById(R.id.view_medicine_name);	// or R.id.view_medicine_name
		medicineNameView.setText("Drug Name: " + text);
		medicineNameView.setTextSize(30);
		
		// Set number of pills per instance
		final int numPills = intent.getIntExtra(AlarmManagerBroadcastReceiver.NUM_PILLS_KEY, 0);
		TextView numPillsView = (TextView) findViewById(R.id.view_num_pills);
		// This is a tricky motherfucker! If you set text to just number, android will assume it's a resource ID. Remember for next time.
		numPillsView.setText("For each instance, "+ numPills + " pills will be taken.");
		/*
		// Set alarm times of when it is active now
		// This is a challenge because the AlarmManagerBroadcastReceiver needs to save instances of it.
		List<String> timeList = new ArrayList<String>();
		int pillFrequency = intent.getIntExtra(AlarmManagerBroadcastReceiver.PILL_FREQUENCY_KEY, 1);
		int beginTime = 8;	// need to grab from intent eventually
		int interval = 12/pillFrequency;
		for (int i = 0; i < pillFrequency; i++)	{
			timeList.add(beginTime + ":00");
			beginTime += interval; 
		}
		*/
		final int timePickerHour = intent.getIntExtra(AlarmManagerBroadcastReceiver.TIME_PICKER_HOUR_KEY, 8);
		final int timePickerMin = intent.getIntExtra(AlarmManagerBroadcastReceiver.TIME_PICKER_MIN_KEY, 0);
		
		final String[] timeList = intent.getStringArrayExtra(AlarmManagerBroadcastReceiver.TIME_LIST_KEY);
		if (timeList == null || timeList.length == 0)
			Toast.makeText(this, "TimeList is null!", Toast.LENGTH_SHORT).show();
		else	{
			ListAdapter listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, timeList);
			ListView listView = (ListView) findViewById(R.id.view_alarm_times);
			listView.setAdapter(listAdapter);
		}
		final String positionCode = intent.getStringExtra(AlarmManagerActivity.ALARM_CODE_KEY);
		alarms = new AlarmBroadcastReceiverWrapper(text, timeList.length, numPills, timeList, positionCode); // set to 8 for now, need to either set it in AlarmListActivity or parse from timeList
		
		
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
				intent.putExtra(AlarmManagerBroadcastReceiver.MEDICINE_NAME_KEY, text);
				intent.putExtra(AlarmManagerBroadcastReceiver.NUM_PILLS_KEY, numPills);
				intent.putExtra(AlarmManagerBroadcastReceiver.PILL_FREQUENCY_KEY,timeList.length);
				intent.putExtra(AlarmManagerBroadcastReceiver.TIME_LIST_KEY, timeList);
				intent.putExtra(AlarmManagerBroadcastReceiver.TIME_PICKER_HOUR_KEY, timePickerHour);
				intent.putExtra(AlarmManagerBroadcastReceiver.TIME_PICKER_MIN_KEY, timePickerMin);
				intent.putExtra(AlarmManagerActivity.ALARM_CODE_KEY, positionCode);
				
				// should send default time start as well
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
	
	@Override
	protected void onResume() {
		super.onResume();
	    if (!resumeHasRun) {
	        resumeHasRun = true;
	        return;
	    }
	    else	{
			TextView numPillsView = (TextView) findViewById(R.id.view_num_pills);
			int numPills = alarms.getNumPills();
			numPillsView.setText("For each instance, " + numPills + " pills will be taken");
			// now set the list for alarms
			//TODO: change this preset!
			String[] timeList = alarms.getTimeList();
			//timeList[0] = alarm.getDefaultStartTime() + ":00";
			if (timeList == null || timeList.length == 0)
				Toast.makeText(this, "TimeList is null!", Toast.LENGTH_SHORT).show();
			else	{
				ListAdapter listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, timeList);
				ListView listView = (ListView) findViewById(R.id.view_alarm_times);
				listView.setAdapter(listAdapter);
			}
		}
	}
	
	public void removeAlarmView(View view)	{
		// This is the class to remove the alarm. Delete the file and update list from previous page
	}


}
