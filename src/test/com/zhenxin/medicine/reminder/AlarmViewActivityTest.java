package com.zhenxin.medicine.reminder;

import org.junit.Assert;
import org.junit.Test;

import junit.framework.TestCase;

public class AlarmViewActivityTest extends TestCase {

	String regex = ",";
	
	@Test
	public void testSplit()	{
		String str1 = "8:00,20:00";
		String str2 = "8:00,14:00,20:00";
		String str3 = "8:00";
		String[] arr1 = str1.split(regex);
		String[] arr2 = str2.split(regex);
		String[] arr3 = str3.split(regex);
		
		Assert.assertArrayEquals(new String[]{"8:00", "20:00"}, arr1);
		Assert.assertArrayEquals(new String[]{"8:00"}, arr3);
	}
}
