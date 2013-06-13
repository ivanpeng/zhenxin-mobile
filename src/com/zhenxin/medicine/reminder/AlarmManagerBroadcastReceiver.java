package com.zhenxin.medicine.reminder;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

/**
 * The class for the broadcast receiver for a medicine alarm. Each 
 * instance of the broadcast receiver has some properties, such as 
 * the medicine name, if it's daily, how many times per day, and 
 * how many pills per time.
 * 
 * @author Ivan
 *
 */
public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {
	
	public static final String MEDICINE_NAME_KEY = "MEDICINE_NAME";
	public static final String PILL_FREQUENCY_KEY = "PILL_FREQUENCY";
	public static final String NUM_PILLS_KEY = "NUM_PILLS";
	public static final String TIME_LIST_KEY = "TIME_LIST";
	
	private static String medicineName;
	private static int pillFrequency;
	private static int numPills;
	private static int defaultStartTime;
	private static int timePickerHour;
	private static int timePickerMinute;
	private static String[] timeList;

	final public static String ONE_TIME = "onetime";
	
	public AlarmManagerBroadcastReceiver ()	{
		super();
		AlarmManagerBroadcastReceiver.defaultStartTime = 8;

	}
	
	public AlarmManagerBroadcastReceiver (String medicineName, int pillFrequency,
			int numPills)	{
		super();
		AlarmManagerBroadcastReceiver.medicineName = medicineName;
		AlarmManagerBroadcastReceiver.pillFrequency = pillFrequency;
		AlarmManagerBroadcastReceiver.numPills = numPills;
		AlarmManagerBroadcastReceiver.defaultStartTime = 8;

	}
	
	public AlarmManagerBroadcastReceiver (String medicineName, int pillFrequency,
			int numPills, int defaultStartTime)	{
		super();
		AlarmManagerBroadcastReceiver.medicineName = medicineName;
		AlarmManagerBroadcastReceiver.pillFrequency = pillFrequency;
		AlarmManagerBroadcastReceiver.numPills = numPills;
		AlarmManagerBroadcastReceiver.defaultStartTime = defaultStartTime;
	}
	
	/**
	 * This function is called when the alarm broadcast receiver broadcasts that the alarm is going. 
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		// When this method is completed, the instance is deleted.
		// Need to reinitialize every time.
		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock(
				PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
		// Acquire the lock
		wl.acquire();

		Toast.makeText(context, "Alarm is going!", Toast.LENGTH_LONG).show();

		// Must wire intent between notification activity and this
		Intent intent2 = new Intent(context, NotificationActivity.class);
		intent2.putExtra(MEDICINE_NAME_KEY, medicineName);
		intent2.putExtra(NUM_PILLS_KEY, numPills);

		String text = "You have a reminder set to take " + medicineName + 
				" " + pillFrequency + " times per day. Take " + numPills + " now!";
		intent2.putExtra(AlarmManagerActivity.NOTIFICATION_MESSAGE_KEY, text);
 		// Now create pending intent
 		PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent2, 0);

 		// Build notification 
 		// Actions are just fake
 		// For this, don't forget to set sounds, lights, vibration, etc.
 		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
 		builder.setContentIntent(pIntent)
 				.setTicker("Medicine Reminder!")
 				.setContentTitle(AlarmManagerBroadcastReceiver.medicineName)
 				.setContentText("Take your pills!")
 				.setSmallIcon(R.drawable.alarm);
 		
 		Notification noti = builder.getNotification();
 		
 		NotificationManager notificationManager = 
 		  (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

 		
 		
 		// Hide the notification after its selected
 		noti.flags |= Notification.FLAG_AUTO_CANCEL;

 		notificationManager.notify(0, noti); 
         
         //Release the lock
         wl.release();
                  
	}
	/**
	 * This function sets the alarm based off of a flag with an indicator
	 * @param context
	 * @param whichAlarm
	 */
	public void SetAlarm(Context context, int whichAlarm)
    {
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.putExtra(ONE_TIME, Boolean.FALSE);
        PendingIntent pi = PendingIntent.getBroadcast(context, whichAlarm, intent, 0);
        //After after 30 seconds
        //TODO: Do the frequency processing here
        // When we have the dialog input here, modify this.
        // First check if the time 
        Calendar cal = Calendar.getInstance();
        Calendar calAlarm = Calendar.getInstance();
        // month, day, and year already set from this; 
        calAlarm.set(Calendar.HOUR_OF_DAY, timePickerHour);
        calAlarm.set(Calendar.MINUTE, timePickerMinute);
        calAlarm.set(Calendar.SECOND, 0);
        long triggerTime = System.currentTimeMillis() -  calAlarm.getTimeInMillis();
        if (triggerTime > 0)
        	// the time has already passed; set calAlarm for next day, and get repeating
        	calAlarm.add(Calendar.DAY_OF_MONTH, 1);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calAlarm.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);
        //am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 5 , pi); 
    }

	/**
	 * This function cancels the selected alarm
	 * @param context
	 * @param whichAlarm
	 */
    public void CancelAlarm(Context context, int whichAlarm)
    {
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, whichAlarm, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
    
	public String getMedicineName() {
		return medicineName;
	}
	public void setMedicineName(String medicineName) {
		AlarmManagerBroadcastReceiver.medicineName = medicineName;
	}
	public int getNumPills() {
		return numPills;
	}
	public void setNumPills(int numPills) {
		AlarmManagerBroadcastReceiver.numPills = numPills;
	}

	public int getPillFrequency() {
		return pillFrequency;
	}

	public void setPillFrequency(int pillFrequency) {
		AlarmManagerBroadcastReceiver.pillFrequency = pillFrequency;
	}

	public int getDefaultStartTime() {
		return defaultStartTime;
	}

	public void setDefaultStartTime(int defaultStartTime) {
		AlarmManagerBroadcastReceiver.defaultStartTime = defaultStartTime;
	}

	public int getTimePickerHour() {
		return timePickerHour;
	}

	public void setTimePickerHour(int timePickerHour) {
		this.timePickerHour = timePickerHour;
	}

	public int getTimePickerMinute() {
		return timePickerMinute;
	}

	public void setTimePickerMinute(int timePickerMinute) {
		this.timePickerMinute = timePickerMinute;
	}

	public  String[] getTimeList() {
		return timeList;
	}

	public  void setTimeList(String[] timeList) {
		AlarmManagerBroadcastReceiver.timeList = timeList;
	}

	

}
