package com.zhenxin.medicine.reminder;

import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
@SuppressWarnings("static-access")
public class AlarmManagerActivity extends Activity {

	private AlarmBroadcastReceiverWrapper alarms;
	
	private int hour;
	private int minute;
	static final int TIME_DIALOG_ID = 999;
	public static final String NOTIFICATION_MESSAGE_KEY = "NOTIFICATION_MESSAGE";
	public static final String SAVED_FILE_PREFIX = "zhenxin_alarms";
	public static final String ALARM_CODE_KEY = "ALARM_CODE";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_manager);
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
        String[] timeList = intent.getStringArrayExtra(AlarmManagerBroadcastReceiver.TIME_LIST_KEY);
        String positionCode = intent.getStringExtra(AlarmManagerActivity.ALARM_CODE_KEY);

        alarms = new AlarmBroadcastReceiverWrapper(medicineName, pillFrequency, numPills, timeList, positionCode);
        
        
    }
    
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case TIME_DIALOG_ID:
			// set time picker as current time
			return new TimePickerDialog(this, new OnTimeSetListener() {
				
				@Override
				public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
					hour = selectedHour;
					minute = selectedMinute;
					if (alarms != null)	{
						alarms.setTimePickerHour(hour);
						alarms.setTimePickerMinute(minute);
					}

			        // Edit custom changing message
			        TextView defaultMessage = (TextView) findViewById(R.id.textView1);
			        int hour12;
			        String ampm;
			        if (hour == 0)	{
			        	ampm = "am";
			        	hour12 = alarms.getTimePickerHour()+12;
			        }
			        else if (hour <= 12)	{
			        	if (hour == 12)
			        		ampm = "pm";
			        	else
			        		ampm = "am";
			        	hour12 = alarms.getTimePickerHour();
			        } else	{
			        	ampm = "pm";
			        	hour12 = alarms.getTimePickerHour() - 12;
			        }
			        defaultMessage.setText("The alarm time is set to " + pad(hour12) + ":" + pad(alarms.getTimePickerMinute()) + " " + ampm + ". Press to change.");

					alarms.setDefaultStartTime(hour);
				}
			}, hour, minute,true);
		}
		return null;
	}
    

	
    @Override
	protected void onStart() {
		super.onStart();
	}

    /**
     * This function is started by the button press of Start Alarm in activity_alarm_manager
     * @param view
     */
	public void startRepeatingTimer(View view) {
    	Context context = this.getApplicationContext();
    	if(alarms != null){
    		// Set complementary properties
            EditText medicineName = (EditText) findViewById(R.id.medicine_name);
            alarms.setMedicineName(medicineName.getText().toString());
            EditText pillFrequency = (EditText) findViewById(R.id.pill_frequency);
            alarms.setPillFrequency(Integer.parseInt(pillFrequency.getText().toString()));
            EditText numPills = (EditText) findViewById(R.id.num_pills_check);
            alarms.setNumPills(Integer.parseInt(numPills.getText().toString()));
            alarms.setTimePickerHour(hour);
            alarms.setTimePickerMinute(minute);
    		
    		String fileName = AlarmManagerActivity.SAVED_FILE_PREFIX + "_" + alarms.getMedicineName() + ".dat";
    		StringBuilder savedData = new StringBuilder();
    		savedData.append(alarms.getMedicineName()).append("\n")
    				.append(alarms.getNumPills()).append("\n")
    				.append(alarms.getPillFrequency()).append("\n");
    		// Before we append the timeList, do some processing to get it into 1 line first
    		String[] alarmTimes = new String[alarms.getPillFrequency()]; 
			int defaultHour = alarms.getTimePickerHour();
			int defaultMin = alarms.getTimePickerMinute();
			int interval;
			//TODO: add check if interval is not exactly 4 hours...
			if (alarms.getPillFrequency() == 1)
				interval = 0;
			else
				interval = 12 / (alarmTimes.length - 1);
			for (int i = 0; i < alarmTimes.length; i++) {
				int temp = defaultHour + i * interval;
				if (temp > 24)
					temp = temp - 24;
				alarmTimes[i] = pad(temp) + ":" + pad(defaultMin);
			}

			alarms.setTimeList(alarmTimes);

    		
			// Now that all properties are set, we register the receivers. Do
			// some manipulation with the whichAlarm parameter
            // the position code variable never changes, so we don't need to modify it.
            alarms.SetAlarm(context,alarms.getPositionCode());
            
            // Register alarms, declare intent filter first
            IntentFilter filter = new IntentFilter();
            for (AlarmManagerBroadcastReceiver alarm :alarms.getAlarms())	{
            	registerReceiver(alarm, filter);
            }
			
			// Now set the times in string 
			String times = AlarmManagerActivity.arrayToAlarmListString(alarmTimes);
    		savedData.append(times).append("\n"); // Writing the timeList might be a bit problematic; check with a JUnit test!
    		// now append the alarm code.
    		savedData.append(alarms.getPositionCode());
    		FileOutputStream fos = null;
    		try	{
    			fos = openFileOutput(fileName, Context.MODE_PRIVATE);
    			fos.write(savedData.toString().getBytes());

    		} catch (IOException ioex)	{
    			// Handles both IOException and FileNotFoundException
    			Toast.makeText(context, "Writing while saving unsuccessful!", Toast.LENGTH_LONG).show();
    		} finally	{
    			if (fos != null)
					try {
						fos.close();
					} catch (IOException e) {
					}
    			
    		}

    		Toast.makeText(context, "Alarm times: " + times + " with position code " + alarms.getPositionCode(), Toast.LENGTH_LONG).show();

            
    	}else{
    		Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
    	}
    }
    
    public static String arrayToAlarmListString(String[] timeList) {
    	StringBuilder list = new StringBuilder();
		for (int i = 0; i < timeList.length; i++)	{
			if (i != 0)
				list.append(",");
			list.append(timeList[i]);
		}
		return list.toString();
	}

	public void cancelRepeatingTimer(View view){
    	Context context = this.getApplicationContext();
    	if(alarms != null){
    		alarms.CancelAlarm(context,alarms.getPositionCode());
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

	public static String pad(int c) {
		if (c >= 10)
		   return String.valueOf(c);
		else
		   return "0" + String.valueOf(c);
	}
    
}
