package com.zhenxin.medicine.reminder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class NotificationActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification);
		//TODO: Set text View with the customized message here!
		Intent intent = getIntent();
		String medicineName = intent.getStringExtra(AlarmManagerBroadcastReceiver.MEDICINE_NAME_KEY);
		boolean dailyFrequency = intent.getBooleanExtra(AlarmManagerBroadcastReceiver.DAILY_FREQUENCY_KEY, true);
		int numPills = intent.getIntExtra(AlarmManagerBroadcastReceiver.NUM_PILLS_KEY, 0);
		
		String text = "You have a reminder set to take " + medicineName + 
				" twice per day. Take " + numPills + " now!"; 
		TextView textView = new TextView(this);
		textView.setText(text);
		setContentView(textView);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.notification, menu);
		return true;
	}

}
