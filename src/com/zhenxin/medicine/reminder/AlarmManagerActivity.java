package com.zhenxin.medicine.reminder;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class AlarmManagerActivity extends Activity {

	private AlarmManagerBroadcastReceiver alarm;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_manager);
        alarm = new AlarmManagerBroadcastReceiver();
    }
    
    @Override
	protected void onStart() {
		super.onStart();
	}

    public void startRepeatingTimer(View view) {
    	Context context = this.getApplicationContext();
    	if(alarm != null){
    		alarm.SetAlarm(context);
    		// Set complementary properties
            EditText medicineName = (EditText) findViewById(R.id.medicine_name);
            alarm.setMedicineName(medicineName.getText().toString());
            //CheckBox isDaily = (CheckBox) findViewById(R.id.num_pills_check);
            // Different convention, pretty bad...should switch this around
            //alarm.setWeeklyMedicine(!isDaily.isChecked());
            alarm.setWeeklyMedicine(true);
            EditText dailyFrequency = (EditText) findViewById(R.id.num_pills_check);
            alarm.setDailyFrequency(Integer.parseInt(dailyFrequency.getText().toString()));
            EditText numPills = (EditText) findViewById(R.id.num_pills_check);
            alarm.setNumPills(Integer.parseInt(numPills.getText().toString()));
            
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
    /*
    public void onetimeTimer(View view){
    	Context context = this.getApplicationContext();
    	if(alarm != null){
    		alarm.setOnetimeTimer(context);
    	}else{
    		Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
    	}
    }
    */
    
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.alarm_manager, menu);
        return true;
    }

    
}
