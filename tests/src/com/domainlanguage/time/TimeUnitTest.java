/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

import java.util.Calendar;

import com.domainlanguage.testutil.SerializationTest;
import com.domainlanguage.time.TimeUnit;

import junit.framework.TestCase;


public class TimeUnitTest extends TestCase {

//	TODO Why does this test hang?
//	public void testSerialization() {
//    	SerializationTest.assertSerializationWorks(TimeUnit.month);
//    }

	public void testToString() {
		assertEquals("month", TimeUnit.month.toString());
	}
	
	public void testConvertibleToMilliseconds() {
		assertTrue(TimeUnit.millisecond.isConvertibleToMilliseconds());
		assertTrue(TimeUnit.hour.isConvertibleToMilliseconds());
		assertTrue(TimeUnit.day.isConvertibleToMilliseconds());
		assertTrue(TimeUnit.week.isConvertibleToMilliseconds());
		assertFalse(TimeUnit.month.isConvertibleToMilliseconds());
		assertFalse(TimeUnit.year.isConvertibleToMilliseconds());
	}
	
	public void testComparison() {
		assertTrue(TimeUnit.hour.compareTo(TimeUnit.millisecond) >0);
		assertTrue(TimeUnit.day.compareTo(TimeUnit.hour) > 0);
		assertTrue(TimeUnit.month.compareTo(TimeUnit.day) > 0);
		assertTrue(TimeUnit.quarter.compareTo(TimeUnit.hour) > 0);
		assertTrue(TimeUnit.year.compareTo(TimeUnit.quarter) > 0);
	}
	
	public void testJavaCalendarConstantForBaseType() {
		assertEquals(Calendar.MILLISECOND, TimeUnit.millisecond.javaCalendarConstantForBaseType());
		assertEquals(Calendar.MILLISECOND, TimeUnit.hour.javaCalendarConstantForBaseType());
		assertEquals(Calendar.MILLISECOND, TimeUnit.day.javaCalendarConstantForBaseType());
		assertEquals(Calendar.MILLISECOND, TimeUnit.week.javaCalendarConstantForBaseType());
		assertEquals(Calendar.MONTH, TimeUnit.month.javaCalendarConstantForBaseType());
		assertEquals(Calendar.MONTH, TimeUnit.quarter.javaCalendarConstantForBaseType());
		assertEquals(Calendar.MONTH, TimeUnit.year.javaCalendarConstantForBaseType());
	}
	
	public void testIsConvertableTo() {
		assertTrue(TimeUnit.hour.isConvertibleTo(TimeUnit.minute));
		assertTrue(TimeUnit.minute.isConvertibleTo(TimeUnit.hour));		
		assertTrue(TimeUnit.year.isConvertibleTo(TimeUnit.month));
		assertTrue(TimeUnit.month.isConvertibleTo(TimeUnit.year));
		assertFalse(TimeUnit.month.isConvertibleTo(TimeUnit.hour));		
		assertFalse(TimeUnit.hour.isConvertibleTo(TimeUnit.month));	
		
	}
	
	public void testNextFinerUnit() {
		assertEquals(TimeUnit.minute, TimeUnit.hour.nextFinerUnit());
		assertEquals(TimeUnit.month, TimeUnit.quarter.nextFinerUnit());
	}
}
