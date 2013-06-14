package com.zhenxin.medicine.reminder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;

/**
 * This class serves as a wrapper for the AlarmManagerBroadcastReceiver activity.
 * AlarmManagerActivity will activate one of this; each alarm will have a set
 * of properties, followed by an array of alarm broadcast receivers to register.
 * This class will manage the alarms properly
 * @author Ivan
 *
 */
public class AlarmBroadcastReceiverWrapper {

	private static List<AlarmManagerBroadcastReceiver> alarms; // static?
	
	private static String medicineName;
	private static int pillFrequency;
	private static int numPills;
	private static int defaultStartTime;
	private static int timePickerHour;
	private static int timePickerMinute;
	private static String[] timeList;
	
	private static String positionCode;
	
	public AlarmBroadcastReceiverWrapper ()	{
		super();
	}
	
	public AlarmBroadcastReceiverWrapper (String medicineName, int pillFrequency, int numPills, String[] timeList)	{
		AlarmBroadcastReceiverWrapper.medicineName = medicineName;
		AlarmBroadcastReceiverWrapper.pillFrequency = pillFrequency;
		AlarmBroadcastReceiverWrapper.numPills = numPills;
		AlarmBroadcastReceiverWrapper.timeList = timeList;
		alarms = new ArrayList<AlarmManagerBroadcastReceiver>();
	}
	
	public AlarmBroadcastReceiverWrapper (String medicineName, int pillFrequency, int numPills, String[] timeList, String positionCode)	{
		AlarmBroadcastReceiverWrapper.medicineName = medicineName;
		AlarmBroadcastReceiverWrapper.pillFrequency = pillFrequency;
		AlarmBroadcastReceiverWrapper.numPills = numPills;
		AlarmBroadcastReceiverWrapper.timeList = timeList;
		AlarmBroadcastReceiverWrapper.positionCode = positionCode;
		alarms = new ArrayList<AlarmManagerBroadcastReceiver>();
	}	

	/**
	 * THis function accesses the AlarmManagerBroadcastReceiver class.
	 * @param context
	 * @param postionCode
	 */
	public void SetAlarm(Context context, String positionCode) {
		// We are given the positionCode and the context which this operates in.
		// We also know how many times this is called, based off the other
		// properties, so all we need to do is set a loop going with set alarm
		
		for (int i = 0; i < alarms.size(); i++)	{
			// First set code
			StringBuilder sb = new StringBuilder();
			sb.append(positionCode).append(AlarmManagerActivity.pad(i+1));
			AlarmManagerBroadcastReceiver alarm = new AlarmManagerBroadcastReceiver();
			
			// Modify time to send in to set the alarm
	        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
	        Date date;
	        Calendar cal = Calendar.getInstance();
			try {
				date = sdf.parse(timeList[i]);
		        cal.setTime(date);
			} catch (ParseException e) {
				// Do something here to catch.
			}
	        int hour = cal.get(Calendar.HOUR_OF_DAY);
	        int minute = cal.get(Calendar.MINUTE);
			
	        Calendar calAlarm = Calendar.getInstance();
	        // month, day, and year already set from this; 
	        calAlarm.set(Calendar.HOUR_OF_DAY, hour);
	        calAlarm.set(Calendar.MINUTE, minute);
	        calAlarm.set(Calendar.SECOND, 0);
	        long triggerTime = System.currentTimeMillis() -  calAlarm.getTimeInMillis();
	        if (triggerTime > 0)
	        	// the time has already passed; set calAlarm for next day, and get repeating
	        	calAlarm.add(Calendar.DAY_OF_MONTH, 1);
	        
			alarm.SetAlarm(context, Integer.parseInt(sb.toString()), calAlarm.getTimeInMillis());

	        //After all this, put alarm down
	        alarms.add(alarm);
		}
		
		
	}
	

	/**
	 * This function serves the same purpose as the SetAlarm in the AlarmBroadcastReceiverWrapper.
	 * @param context
	 * @param positionCode
	 */
	public void CancelAlarm(Context context, String positionCode) {
		for (int i = 0; i < alarms.size(); i++)	{
			// First set code
			StringBuilder sb = new StringBuilder();
			sb.append(positionCode).append(AlarmManagerActivity.pad(i+1));
			AlarmManagerBroadcastReceiver alarm = alarms.get(i);
			
			alarm.CancelAlarm(context, Integer.parseInt(sb.toString()));
		}
	}

	
	
	public List<AlarmManagerBroadcastReceiver> getAlarms() {
		return alarms;
	}
	public void setAlarms(List<AlarmManagerBroadcastReceiver> alarms) {
		this.alarms = alarms;
	}
	public static String getMedicineName() {
		return medicineName;
	}
	public static void setMedicineName(String medicineName) {
		AlarmBroadcastReceiverWrapper.medicineName = medicineName;
	}
	public static int getPillFrequency() {
		return pillFrequency;
	}
	public static void setPillFrequency(int pillFrequency) {
		AlarmBroadcastReceiverWrapper.pillFrequency = pillFrequency;
	}
	public static int getNumPills() {
		return numPills;
	}
	public static void setNumPills(int numPills) {
		AlarmBroadcastReceiverWrapper.numPills = numPills;
	}
	public static int getDefaultStartTime() {
		return defaultStartTime;
	}
	public static void setDefaultStartTime(int defaultStartTime) {
		AlarmBroadcastReceiverWrapper.defaultStartTime = defaultStartTime;
	}
	public static int getTimePickerHour() {
		return timePickerHour;
	}
	public static void setTimePickerHour(int timePickerHour) {
		AlarmBroadcastReceiverWrapper.timePickerHour = timePickerHour;
	}
	public static int getTimePickerMinute() {
		return timePickerMinute;
	}
	public static void setTimePickerMinute(int timePickerMinute) {
		AlarmBroadcastReceiverWrapper.timePickerMinute = timePickerMinute;
	}
	public static String[] getTimeList() {
		return timeList;
	}
	public static void setTimeList(String[] timeList) {
		AlarmBroadcastReceiverWrapper.timeList = timeList;
	}
	public static String getPositionCode() {
		return positionCode;
	}
	public static void setPositionCode(String positionCode) {
		AlarmBroadcastReceiverWrapper.positionCode = positionCode;
	}

	
	
}
