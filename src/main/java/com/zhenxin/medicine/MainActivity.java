package com.zhenxin.medicine;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.zhenxin.medicine.camera.ScannerActivity;
import com.zhenxin.medicine.contacts.SocialActivity;
import com.zhenxin.medicine.reminder.AlarmListActivity;

public class MainActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		TabHost tabHost = getTabHost();
		
		TabSpec alarmspec = tabHost.newTabSpec("Alarms");
        // setting Title and Icon for the Tab
        alarmspec.setIndicator("", getResources().getDrawable(R.drawable.icon_alarm_tab));
        Intent alarmIntent = new Intent(this, AlarmListActivity.class);
        alarmspec.setContent(alarmIntent);
        
        TabSpec qrspec = tabHost.newTabSpec("Scan");
        // setting Title and Icon for the Tab
        qrspec.setIndicator("", getResources().getDrawable(R.drawable.icon_qr_tab));
        Intent qrIntent = new Intent(this, ScannerActivity.class); // real goddamn slow right now, so disable.
        qrspec.setContent(qrIntent); 
        //qrspec.setContent(alarmIntent); // disable alarm list for now
		
        TabSpec familyspec = tabHost.newTabSpec("Social");
        // setting Title and Icon for the Tab
        familyspec.setIndicator("", getResources().getDrawable(R.drawable.icon_family_tab));
        Intent familyIntent = new Intent(this, SocialActivity.class);
        familyspec.setContent(familyIntent);
		
        tabHost.addTab(alarmspec); // Adding photos tab
        tabHost.addTab(qrspec); // Adding songs tab
        tabHost.addTab(familyspec); // Adding videos tab
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
