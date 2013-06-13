package com.zhenxin.medicine.reminder;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
 
public class AlarmListActivity extends ListActivity {
		
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_list_layout);
        ListAdapter adapter = createAdapter();
        setListAdapter(adapter);
        //setListAdapter(ArrayAdapter.createFromResource(getApplicationContext(),R.array.tut_titles, R.layout.list_item));
    }
    
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	Intent intent = new Intent(this, AlarmViewActivity.class);
    	ListAdapter adapter = getListAdapter();
    	// Send over extra properties to the view class
    	String medicineName = (String) adapter.getItem(position);
    	
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
    	
    	//TODO: input timeList into the data!
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
    	
    	// now that we have set everything, start the activity! 
    	startActivity(intent);
    }
    
    private AlarmManagerBroadcastReceiver getAlarmByPosition(int position) {
		return null;
	}

	protected ListAdapter createAdapter()
    {
    	// Create some mock data
    	String[] testValues = new String[] {
    			"Fluoxetine",
    			"Zolpidem",
    			"Haloperidol"
    	};
 
    	// Create a simple array adapter (of type string) with the test values
    	ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, testValues);
 
    	return adapter;
    }
}
