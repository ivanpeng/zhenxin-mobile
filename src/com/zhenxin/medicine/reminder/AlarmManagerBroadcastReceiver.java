package com.zhenxin.medicine.reminder;

import java.util.Calendar;

import android.app.Activity;
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
	public static final String TIME_PICKER_HOUR_KEY = "TIME_PICKER_HOUR";
	public static final String TIME_PICKER_MIN_KEY = "TIME_PICKER_MIN";
	public static final String TIME_LIST_KEY = "TIME_LIST";

	final public static String ONE_TIME = "onetime";
	
	public AlarmManagerBroadcastReceiver ()	{
		super();

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

		//TODO: this is a placeholder to remind us that this may need to be removed.
		String medicineName = intent.getStringExtra(MEDICINE_NAME_KEY);
		int numPills = intent.getIntExtra(NUM_PILLS_KEY, -1);
		int pillFrequency = intent.getIntExtra(PILL_FREQUENCY_KEY, -1);
		String alarmCode = intent.getStringExtra(AlarmManagerActivity.ALARM_CODE_KEY);
		
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
 				.setContentTitle(medicineName)
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
	public void SetAlarm(Context context, int whichAlarm, long timeInMillis, AlarmBroadcastReceiverWrapper data)
    {
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.putExtra(ONE_TIME, Boolean.FALSE);
        // try putting intent extras here to send the message?
        // TODO: fix!
        intent.putExtra(MEDICINE_NAME_KEY, data.getMedicineName());
        intent.putExtra(NUM_PILLS_KEY, data.getNumPills());
        intent.putExtra(PILL_FREQUENCY_KEY, data.getPillFrequency());
        PendingIntent pi = PendingIntent.getBroadcast(context, whichAlarm, intent, 0);

        am.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, AlarmManager.INTERVAL_DAY, pi);
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

}
