package com.zhenxin.medicine.reminder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AlarmManagerActivity extends Activity {

	private AlarmManagerBroadcastReceiver alarm;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_manager);
        alarm = new AlarmManagerBroadcastReceiver();
        // If this was started from clicking bottom, update the editText field 
        Intent intent = this.getIntent();
        // if there is a value
        if (intent.getStringExtra(AlarmManagerBroadcastReceiver.MEDICINE_NAME_KEY) != null)	{
        	String medicineName = intent.getStringExtra(AlarmManagerBroadcastReceiver.MEDICINE_NAME_KEY);
        	// now put it in the content view
        	EditText editText = (EditText) findViewById(R.id.medicine_name);
        	editText.setText(medicineName);
        }
        
        	

    }
    
    @Override
	protected void onStart() {
		super.onStart();
	}

    public void startRepeatingTimer(View view) {
    	Context context = this.getApplicationContext();
    	if(alarm != null){
    		// Set complementary properties
            EditText medicineName = (EditText) findViewById(R.id.medicine_name);
            alarm.setMedicineName(medicineName.getText().toString());
            EditText pillFrequency = (EditText) findViewById(R.id.pill_frequency);
            alarm.setPillFrequency(Integer.parseInt(pillFrequency.getText().toString()));
            //CheckBox isDaily = (CheckBox) findViewById(R.id.daily_weekly_check);
            // Different convention, pretty bad...should switch this around
            //alarm.setDailyFrequency(isDaily.isChecked());
            EditText numPills = (EditText) findViewById(R.id.num_pills_check);
            alarm.setNumPills(Integer.parseInt(numPills.getText().toString()));
            
    		alarm.SetAlarm(context);
            
    	}else{
    		Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
    	}
    }
    
    public void cancelRepeatingTimer(View view){
    	Context context = this.getApplicationContext();
    	if(alarm != null){
    		alarm.CancelAlarm(context);
    	}else{
    		Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
    	}
    }
    
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.alarm_manager, menu);
        return true;
    }

    
}
