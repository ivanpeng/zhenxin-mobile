package com.zhenxin.medicine.reminder;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
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
	
	private String medicineName;
	private boolean weeklyMedicine;
	private int dailyFrequency;
	private int numPills;

	final public static String ONE_TIME = "onetime";
	@Override
	public void onReceive(Context context, Intent intent) {
		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock(
				PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
		// Acquire the lock
		wl.acquire();

		// You can do the processing here update the widget/remote views.
		Bundle extras = intent.getExtras();
		StringBuilder msgStr = new StringBuilder();

		if (extras != null && extras.getBoolean(ONE_TIME, Boolean.FALSE)) {
			msgStr.append("One time Timer : ");
		}
		Format formatter = new SimpleDateFormat("hh:mm:ss a");
		msgStr.append(formatter.format(new Date()));

		Toast.makeText(context, msgStr, Toast.LENGTH_LONG).show();

        //Now insert the notification
 		/* Take this out for now; we can use this to customize the message
 		// Before sending intent to pending intent, add property of text first
 		EditText editText = (EditText) findViewById(R.id.edit_message);
 		String message = editText.getText().toString();
 		intent.putExtra(EXTRA_MESSAGE, message);
 		//this.startActivity(intent);
 		*/
		
		// Must wire intent between notification activity and this
		Intent intent2 = new Intent(context, NotificationActivity.class);
		
 		// Now create pending intent
 		PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent2, 0);

 		// Build notification
 		// Actions are just fake
 		// For this, don't forget to set sounds, lights, vibration, etc.
 		Notification noti = new Notification.Builder(context)
 				.setTicker("Medicine Reminder!")
 		        .setContentTitle(medicineName)
 		        .setContentText(medicineName)
 		        .setSmallIcon(R.drawable.ic_launcher)	// should change Icon
 		        .setContentIntent(pIntent).getNotification();
 		    
 		NotificationManager notificationManager = 
 		  (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

 		
 		
 		// Hide the notification after its selected
 		noti.flags |= Notification.FLAG_AUTO_CANCEL;

 		notificationManager.notify(0, noti); 
         
         //Release the lock
         wl.release();
         
         // How to manage pending intent from clicking NotificationActivity?
         
	}
	public void SetAlarm(Context context)
    {
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.putExtra(ONE_TIME, Boolean.FALSE);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        //After after 30 seconds
        //TODO: Do the frequency processing here
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
		this.medicineName = medicineName;
	}
	public boolean isWeeklyMedicine() {
		return weeklyMedicine;
	}
	public void setWeeklyMedicine(boolean weeklyMedicine) {
		this.weeklyMedicine = weeklyMedicine;
	}
	public int getDailyFrequency() {
		return dailyFrequency;
	}
	public void setDailyFrequency(int dailyFrequency) {
		this.dailyFrequency = dailyFrequency;
	}
	public int getNumPills() {
		return numPills;
	}
	public void setNumPills(int numPills) {
		this.numPills = numPills;
	}

}
