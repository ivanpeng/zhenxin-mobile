package com.zhenxin.medicine.reminder;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
 
public class AlarmListActivity extends ListActivity {
	
	public static final String IS_NEW_ALARM_KEY = "IS_NEW_ALARM";
		
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_list_layout);
        ListAdapter adapter = createAdapter();
        setListAdapter(adapter);
        //setListAdapter(ArrayAdapter.createFromResource(getApplicationContext(),R.array.tut_titles, R.layout.list_item));
    }
    
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	ListAdapter adapter = getListAdapter();
    	// Send over extra properties to the view class
    	String medicineName = (String) adapter.getItem(position);
    	
    	Intent intent;
    	// Before processing with the list item, check if it's the last item, which states us for adding a new drug to the list
    	if (adapter.getCount() == (position + 1))	{
    		// We are inputting a new item to the list; wire intent to be so!
    		intent = new Intent(this, AlarmManagerActivity.class);
    		intent.putExtra(AlarmManagerActivity.ALARM_CODE_KEY, String.valueOf(position));
    		intent.putExtra(AlarmListActivity.IS_NEW_ALARM_KEY, true);
    	} else {

        	intent = new Intent(this, AlarmViewActivity.class);
	    	// Before we instantiate the alarm, we check the saved properties.
	    	String fileName = AlarmManagerActivity.SAVED_FILE_PREFIX + "_" + medicineName + ".dat";
	
			FileInputStream fis = null;
			String[] dataArray = null;
	    	try	{
	    		fis = openFileInput(fileName);
	    		BufferedReader r = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
	    		List<String> data = new ArrayList<String>();
	    		String line;
	    		while ((line = r.readLine()) != null)	{
	    			data.add(line);
	    		}
	    		dataArray = data.toArray(new String[]{});
	    		
	    	} catch (IOException ioex)	{
	    		// Catch if file doesn't exist;
	    	} finally {
	    		if (fis != null)
					try {
						fis.close();
					} catch (IOException e) {
					}
	    	}
	    	// We have the data in order of the following: medicineName, numPills, pillFreq, and timeList
	    	// Should create a function to read data, but organize for later
	    	// First check Medicine Name...necessary?
	    	//if (dataArray[0] != null || dataArray[0].length() != 0)
	    		
	    	intent.putExtra(AlarmManagerBroadcastReceiver.MEDICINE_NAME_KEY, medicineName);
	    	
	    	//intent.putExtra(AlarmManagerBroadcastReceiver.NUM_PILLS_KEY, alarm.getNumPills());
	    	int numPills;
	    	if (dataArray != null && dataArray[1].length() !=0)
	    		numPills = Integer.parseInt(dataArray[1]);
	    	else
	    		numPills = 2;
	    	intent.putExtra(AlarmManagerBroadcastReceiver.NUM_PILLS_KEY, numPills);
	    	
	    	// Do the processing on the pillFrequency
	    	int pillFrequency;
	    	if (dataArray != null && dataArray[2].length() != 0)	
	    		pillFrequency = Integer.parseInt(dataArray[2]);
	    	else
	    		pillFrequency = 2;
	    	intent.putExtra(AlarmManagerBroadcastReceiver.PILL_FREQUENCY_KEY, pillFrequency);
	    	
	    	// Do a little bit of processing with the timeList
	    	String[] timeList;
	    	// checking null is a bit weird.
	    	if (dataArray != null && !dataArray[3].equals("null") && dataArray[3].length() != 0)	{
	    		//if 
	    		timeList = dataArray[3].split(",");
	    		//Toast.makeText(this.getApplicationContext(), "existing time list has been entered", Toast.LENGTH_LONG).show();
	    		Toast.makeText(getApplicationContext(), dataArray[3], Toast.LENGTH_LONG).show();
	    	} else	{
		    	// Default Option!
		    	// Algorithm: 8am initial time, partition day into 12/(n) intervals
		    	timeList = new String[pillFrequency];
		    	int defaultStartTime = 8;
		    	int interval;
		    	if (pillFrequency == 1)
		    		interval = 0;
		    	else
		    		interval = 12/(timeList.length-1);
		    	for (int i = 0; i < timeList.length; i++)	{
		    		int temp = defaultStartTime + i*interval;
		    		if (temp > 24)
		    			temp = temp -24;
		    		timeList[i] = temp + ":00";
		    	}
	    		Toast.makeText(this.getApplicationContext(), "creating new list with default time as " + timeList[0], Toast.LENGTH_LONG).show();
	
	    	}
	    	intent.putExtra(AlarmManagerBroadcastReceiver.TIME_LIST_KEY, timeList);
	    	
	    	//Now grab the last line; this describes the position, which will be used in coding the alarmCode.
	    	if (dataArray != null && dataArray[4] != null && dataArray[4].length() != 0)	{
	    		intent.putExtra(AlarmManagerActivity.ALARM_CODE_KEY, String.valueOf(dataArray[4]));
	    	} else	{
	    		intent.putExtra(AlarmManagerActivity.ALARM_CODE_KEY, String.valueOf(position));
	    	}
	    	//Before we leave this look to start activity, add intent of whether or not this is a new alarm
	    	intent.putExtra(AlarmListActivity.IS_NEW_ALARM_KEY, false);
    	}
    	
    	// now that we have set everything, start the activity! 
    	startActivity(intent);
    }
    
    /**
     * On resume of this main page, it will update the listAdapter
     */
    @Override
	protected void onResume() {
		super.onResume();
		ListAdapter adapter = updateAdapter();
		setListAdapter(adapter);
		// before writing, generate saved data file
		StringBuilder savedData = new StringBuilder();
		for (int i = 0; i < adapter.getCount(); i++)	{
			savedData.append(adapter.getItem(i).toString()).append("\n");
		}
		// write to file
		String fileName = AlarmManagerActivity.SAVED_FILE_PREFIX + "_master.dat";
    	FileOutputStream fos = null;
		try	{
			fos = openFileOutput(fileName, Context.MODE_PRIVATE);
			fos.write(savedData.toString().getBytes());

		} catch (IOException ioex)	{
			// Handles both IOException and FileNotFoundException
			Toast.makeText(getApplicationContext(), "Writing while saving unsuccessful!", Toast.LENGTH_LONG).show();
		} finally	{
			if (fos != null)
				try {
					fos.close();
				} catch (IOException e) {
				}
			
		}
		
		
	}

	private AlarmManagerBroadcastReceiver getAlarmByPosition(int position) {
		return null;
	}

	protected ListAdapter createAdapter()
    {
		String fileName = AlarmManagerActivity.SAVED_FILE_PREFIX + "_master.dat";
		FileInputStream fis = null;
		String[] dataArray = null;
    	try	{
    		fis = openFileInput(fileName);
    		BufferedReader r = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
    		List<String> data = new ArrayList<String>();
    		String line;
    		while ((line = r.readLine()) != null)	{
    			data.add(line);
    		}
    		dataArray = data.toArray(new String[]{});
    		
    	} catch (IOException ioex)	{
    		// Catch if file doesn't exist;
    		dataArray = new String[] {
        			"Fluoxetine",
        			"Zolpidem",
        			"Haloperidol",
        			"Add a prescription"
        	};
    	} finally {
    		if (fis != null)
				try {
					fis.close();
				} catch (IOException e) {
				}
    	} 
    	// Create a simple array adapter (of type string) with the test values
    	ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataArray);
    	return adapter;
    }
	
	/**
	 * Create updated listadapter to show on AlarmListActivity. Must read from AlarmManagerActivity
	 * @return
	 */
	
	protected ListAdapter updateAdapter()	{
		//TODO: add functionality!
		// Want to write adapter to the file here, along with updating the list
		ListAdapter adapter = getListAdapter();
		
		return adapter;
	}
}
