package com.zhenxin.medicine.reminder;

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
	public static final String DAILY_FREQUENCY_KEY = "DAILY_FREQUENCY";
	public static final String PILL_FREQUENCY_KEY = "PILL_FREQUENCY";
	public static final String NUM_PILLS_KEY = "NUM_PILLS";
	
	private static String medicineName;
	private static int pillFrequency;
	private static boolean dailyFrequency;
	private static int numPills;

	final public static String ONE_TIME = "onetime";
	
	public AlarmManagerBroadcastReceiver ()	{
		super();
	}
	
	public AlarmManagerBroadcastReceiver (String medicineName, int pillFrequency,
			boolean dailyFrequency, int numPills)	{
		super();
		AlarmManagerBroadcastReceiver.medicineName = medicineName;
		AlarmManagerBroadcastReceiver.pillFrequency = pillFrequency;
		AlarmManagerBroadcastReceiver.dailyFrequency = dailyFrequency;
		AlarmManagerBroadcastReceiver.numPills = numPills;
	}
	
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
		intent2.putExtra(DAILY_FREQUENCY_KEY, dailyFrequency);
		intent2.putExtra(NUM_PILLS_KEY, numPills);
		
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
	public void SetAlarm(Context context)
    {
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.putExtra(ONE_TIME, Boolean.FALSE);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        //After after 30 seconds
        //TODO: Do the frequency processing here
        // When we have the dialog input here, modify this.
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 5 , pi); 
    }

    public void CancelAlarm(Context context)
    {
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
    
	public String getMedicineName() {
		return medicineName;
	}
	public void setMedicineName(String medicineName) {
		AlarmManagerBroadcastReceiver.medicineName = medicineName;
	}
	public boolean getDailyFrequency() {
		return dailyFrequency;
	}
	public void setDailyFrequency(boolean dailyFrequency) {
		AlarmManagerBroadcastReceiver.dailyFrequency = dailyFrequency;
	}
	public int getNumPills() {
		return numPills;
	}
	public void setNumPills(int numPills) {
		AlarmManagerBroadcastReceiver.numPills = numPills;
	}

	public static int getPillFrequency() {
		return pillFrequency;
	}

	public void setPillFrequency(int pillFrequency) {
		AlarmManagerBroadcastReceiver.pillFrequency = pillFrequency;
	}

}
