package com.zhenxin.medicine.reminder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class AlarmBroadcastReceiverWrapperTest {

	String dateParser = "HH:mm";
	String date = "13:00";
	String dateTestParser = "MM/dd/yyyy HH:mm";
	String dateTest = "01/01/2012 01:00";
	
	/**
	 * This class tests the dates of the AlarmBroadcastReceiverWrapper
	 * From this we have learned that with Calendar.setDate(), if you don't
	 * set the other date parameters, it will be declared as the the epoch 
	 * time. This may be okay, depending on what we're working with. If we
	 * want to get Maven incorporated into this, we should have test cases 
	 * better wired.
	 */
	@Test
	public void testDates() {
		SimpleDateFormat sdf = new SimpleDateFormat(dateTestParser);
		SimpleDateFormat sdf2 = new SimpleDateFormat(dateParser);
		try {
			Date datetime = sdf.parse(dateTest);
			Date datetime2 = sdf2.parse(date);
			Calendar cal = Calendar.getInstance();
			//cal.setTime(datetime);
			cal.setTime(datetime2); 
			
			//System.out.println(cal.toString());
			
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

