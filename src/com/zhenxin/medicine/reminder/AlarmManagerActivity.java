package com.zhenxin.medicine.reminder;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
/**
 * This view opens up the editing window for one specific instance of an alarm.
 * @author Ivan
 *
 */
public class AlarmManagerActivity extends Activity {

	private AlarmManagerBroadcastReceiver alarm;
	
	private int hour;
	private int minute;
	static final int TIME_DIALOG_ID = 999;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_manager);
        alarm = new AlarmManagerBroadcastReceiver();
        // If this was started from clicking bottom, update the editText field 
        Intent intent = this.getIntent();
        String medicineName = "";
        int numPills = 2;
        int pillFrequency = 2;
        // if there is a value
        if (intent.getStringExtra(AlarmManagerBroadcastReceiver.MEDICINE_NAME_KEY) != null)	{
        	medicineName = intent.getStringExtra(AlarmManagerBroadcastReceiver.MEDICINE_NAME_KEY);
        	// now put it in the content view
        	EditText editText = (EditText) findViewById(R.id.medicine_name);
        	editText.setText(medicineName);
        }
		pillFrequency = intent.getIntExtra(AlarmManagerBroadcastReceiver.PILL_FREQUENCY_KEY, 2);
		// now put it in the content view
		EditText editText2 = (EditText) findViewById(R.id.pill_frequency);
		editText2.setText(String.valueOf(pillFrequency));
		numPills = intent.getIntExtra(AlarmManagerBroadcastReceiver.NUM_PILLS_KEY, 2);
		// now put it in the content view
		EditText editText3 = (EditText) findViewById(R.id.num_pills_check);
		editText3.setText(String.valueOf(numPills));
        
        //TODO: get default times from default values of hour and minute
        Button changeDialogButton = (Button) findViewById(R.id.alarm_edit_default_time);
        changeDialogButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Here, open dialog box and set the time manager
				showDialog(TIME_DIALOG_ID);
				
			}
		});
        
    }
    
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case TIME_DIALOG_ID:
			// set time picker as current time
			return new TimePickerDialog(this, new OnTimeSetListener() {
				
				@Override
				public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
					// TODO Auto-generated method stub
					hour = selectedHour;
					minute = selectedMinute;
					if (alarm != null)	{
						alarm.setTimePickerHour(hour);
						alarm.setTimePickerMinute(minute);
					}

			        // Edit custom changing message
			        TextView defaultMessage = (TextView) findViewById(R.id.textView1);
			        int hour12;
			        String ampm;
			        if (hour < 12)	{
			        	ampm = "am";
			        	if (alarm.getTimePickerHour() != 0)
			        		hour12 = alarm.getTimePickerHour();
			        	else
			        		hour12 = 12;
			        } else	{
			        	ampm = "pm";
			        	hour12 = alarm.getTimePickerHour() - 12;
			        }
			        
			        defaultMessage.setText("The alarm time is set to " + hour12 + " " + ampm + ". Press to change.");

					
				}
			}, hour, minute,true);
		}
		return null;
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
            EditText numPills = (EditText) findViewById(R.id.num_pills_check);
            alarm.setNumPills(Integer.parseInt(numPills.getText().toString()));
            alarm.setTimePickerHour(hour);
            alarm.setTimePickerMinute(minute);
    		alarm.SetAlarm(context,0);
    		Toast.makeText(context, "Alarm is set for " + hour + ":" + minute, Toast.LENGTH_LONG).show();
            
    	}else{
    		Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
    	}
    }
    
    public void cancelRepeatingTimer(View view){
    	Context context = this.getApplicationContext();
    	if(alarm != null){
    		alarm.CancelAlarm(context,0);
    	}else{
    		Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
    	}
    }
    
    /**
     * Finish the alarm manager activity.
     * @param view
     */
    public void onClickEnd(View view)	{
    	this.finish();
    }
    
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.alarm_manager, menu);
        return true;
    }

    
}
