/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

import java.util.*;

import com.domainlanguage.testutil.SerializationTest;

import junit.framework.*;

public class TimeIntervalTest extends TestCase {
	private TimePoint dec19_2003 = TimePoint.atMidnightGMT(2003, 12, 19);
	private TimePoint dec20_2003 = TimePoint.atMidnightGMT(2003, 12, 20);
	private TimePoint dec21_2003 = TimePoint.atMidnightGMT(2003, 12, 21);
	private TimePoint dec22_2003 = TimePoint.atMidnightGMT(2003, 12, 22);
	private TimePoint dec23_2003 = TimePoint.atMidnightGMT(2003, 12, 23);

    public void testSerialization() {
		TimeInterval interval = TimeInterval.closed(dec20_2003, dec22_2003);
    	SerializationTest.assertSerializationWorks(interval);
    }
	
	public void testBeforeClosed() {
			TimeInterval interval = TimeInterval.closed(dec20_2003, dec22_2003);
		// Only the upper end should matter for this test.
		assertFalse(interval.isBefore(dec21_2003));
		assertFalse(interval.isBefore(dec22_2003));
		assertTrue(interval.isBefore(dec23_2003));
	}

	public void testAfterClosed() {
		TimeInterval interval = TimeInterval.closed(dec20_2003, dec22_2003);
		// Only the lower end should matter for this test.
		assertTrue(interval.isAfter(dec19_2003));
		assertFalse(interval.isAfter(dec20_2003));
		assertFalse(interval.isAfter(dec21_2003));
	}

	public void testIncludesClosed() {
		TimeInterval interval = TimeInterval.closed(dec20_2003, dec22_2003);
		assertFalse(interval.includes(dec19_2003));
		assertTrue(interval.includes(dec20_2003));
		assertTrue(interval.includes(dec21_2003));
		assertTrue(interval.includes(dec22_2003));
		assertFalse(interval.includes(dec23_2003));
	}

	public void testBeforeOpen() {
		TimeInterval interval = TimeInterval.open(dec20_2003, dec22_2003);
		// Only the upper end should matter for this test.
		assertFalse(interval.isBefore(dec21_2003));
		assertTrue(interval.isBefore(dec22_2003));
		assertTrue(interval.isBefore(dec23_2003));
	}

	public void testAfterOpen() {
		TimeInterval interval = TimeInterval.open(dec20_2003, dec22_2003);
		// Only the lower end should matter for this test.
		assertTrue(interval.isAfter(dec19_2003));
		assertTrue(interval.isAfter(dec20_2003));
		assertFalse(interval.isAfter(dec21_2003));
	}

	public void testIncludesOpen() {
		TimeInterval interval = TimeInterval.open(dec20_2003, dec22_2003);
		assertFalse(interval.includes(dec19_2003));
		assertFalse(interval.includes(dec20_2003));
		assertTrue(interval.includes(dec21_2003));
		assertFalse(interval.includes(dec22_2003));
		assertFalse(interval.includes(dec23_2003));
	}

	public void testIncludesHalfOpen() {
		TimeInterval interval = TimeInterval.over(dec20_2003, true, dec22_2003, false);
		assertFalse(interval.includes(dec19_2003));
		assertTrue(interval.includes(dec20_2003));
		assertTrue(interval.includes(dec21_2003));
		assertFalse(interval.includes(dec22_2003));
		assertFalse(interval.includes(dec23_2003));
	}

	public void testCreateWithDurationFrom() {
		Duration twoDays = Duration.days(2);
		TimeInterval following = TimeInterval.startingFrom(dec20_2003, true, twoDays, true);
		assertEquals("[ dec20", dec20_2003, following.start());
		assertEquals("dec 22]", dec22_2003, following.end());
		
	}

	public void testCreateWithDurationUntil() {
		Duration twoDays = Duration.days(2);
		TimeInterval preceding = TimeInterval.preceding(dec21_2003, true, twoDays, true);
		assertEquals("[ dec19", dec19_2003, preceding.start());
		assertEquals("dec21 )", dec21_2003, preceding.end());
	}

	public void testDefaultFromPoints() {
		// Default is closed start, open end [start, end)
		// which is the most common convention. For example,
		// Days include 12:00am at their start, but do not
		// include the 12:00am that end them.

		TimeInterval interval = TimeInterval.over(dec20_2003, dec22_2003);
		assertFalse(interval.includes(dec19_2003));
		assertTrue(interval.includes(dec20_2003));
		assertTrue(interval.includes(dec21_2003));
		assertFalse(interval.includes(dec22_2003));
		assertFalse(interval.includes(dec23_2003));
	}

	public void testDefaultFromDuration() {
		// Default is closed start, open end [start, end)
		// which is the most common convention. For example,
		// Days include 12:00am at their start, but do not
		// include the 12:00am that end them.

		TimeInterval interval = TimeInterval.startingFrom(dec20_2003, Duration.hours(48));
		assertFalse(interval.includes(dec19_2003));
		assertTrue(interval.includes(dec20_2003));
		assertTrue(interval.includes(dec21_2003));
		assertFalse(interval.includes(dec22_2003));
		assertFalse(interval.includes(dec23_2003));
	}

	public void testEverFrom() {
		TimeInterval afterDec20 = TimeInterval.everFrom(dec20_2003);
		assertTrue(afterDec20.includes(TimePoint.atMidnightGMT(2062, 3, 5)));
		assertFalse(afterDec20.includes(TimePoint.atMidnightGMT(1776, 7, 4)));
		assertTrue(afterDec20.includes(dec20_2003));
	}

	public void testEverUntil() {
		TimeInterval afterDec20 = TimeInterval.everPreceding(dec20_2003);
		assertFalse(afterDec20.includes(TimePoint.atMidnightGMT(2062, 3, 5)));
		assertTrue(afterDec20.includes(TimePoint.atMidnightGMT(1776, 7, 4)));
		assertFalse(afterDec20.includes(dec20_2003));
	}

	public void testLength() {
		TimeInterval interval = TimeInterval.open(dec20_2003, dec22_2003);
		assertEquals(Duration.days(2), interval.length());
		
		TimePoint first = TimePoint.atGMT(2004, 1, 1, 1, 1, 1, 1);
		TimePoint second = TimePoint.atGMT(2004, 1, 6, 5, 4, 3, 2);
		interval = TimeInterval.closed(first, second);
		Duration expectedLength = Duration.daysHoursMinutesSecondsMilliseconds(5, 4, 3, 2, 1);
		assertEquals(expectedLength, interval.length());
	}

	public void testDaysIterator() {
		TimePoint start = TimePoint.atGMT(2004, 2, 5, 10, 0);
		TimePoint end = TimePoint.atGMT(2004, 2, 8, 2, 0);
		TimeInterval interval = TimeInterval.over(start, end);
		Iterator it = interval.daysIterator();
		assertTrue(it.hasNext());
		assertEquals(start, it.next());
		assertTrue(it.hasNext());
		assertEquals(TimePoint.atGMT(2004, 2, 6, 10, 0), it.next());
		assertTrue(it.hasNext());
		assertEquals(TimePoint.atGMT(2004, 2, 7, 10, 0), it.next());
		assertFalse(it.hasNext());
	}
	
	public void testSubintervalIterator() {
		TimePoint d4_h10 = TimePoint.atGMT(2004, 2, 4, 10, 0);
		TimePoint d5_h10 = TimePoint.atGMT(2004, 2, 5, 10, 0);
		TimePoint d6_h10 = TimePoint.atGMT(2004, 2, 6, 10, 0);
		TimePoint d7_h10 = TimePoint.atGMT(2004, 2, 7, 10, 0);
		TimePoint d8_h10 = TimePoint.atGMT(2004, 2, 8, 10, 0);
		TimePoint d9_h10 = TimePoint.atGMT(2004, 2, 9, 10, 0);
		TimePoint d9_h2 = TimePoint.atGMT(2004, 2, 9, 2, 0);

		TimeInterval interval = TimeInterval.over(d4_h10, d9_h2);
		Iterator it = interval.iterator(Duration.days(2));
		assertTrue(it.hasNext());
		assertEquals(TimeInterval.over(d4_h10, d6_h10), it.next());
		assertTrue(it.hasNext());
		assertEquals(TimeInterval.over(d6_h10, d8_h10), it.next());
		assertFalse(it.hasNext());
		assertNull(it.next());

		it = interval.iterator(Duration.weeks(1));
		assertFalse(it.hasNext());
		
		TimePoint h2 = d9_h2;
		TimePoint h3_m30 = TimePoint.atGMT(2004, 2, 9, 3, 30);
		TimePoint h5 = TimePoint.atGMT(2004, 2, 9, 5, 0);
		TimePoint h6_m30 = TimePoint.atGMT(2004, 2, 9, 6, 30);
		TimePoint h8 = TimePoint.atGMT(2004, 2, 9, 8, 0);
		
		TimeInterval interval2 = TimeInterval.over(h2, h8);
		it = interval2.iterator(Duration.minutes(90));
		assertTrue(it.hasNext());
		assertEquals(TimeInterval.over(h2, h3_m30), it.next());
		assertTrue(it.hasNext());
		assertEquals(TimeInterval.over(h3_m30, h5), it.next());
		assertTrue(it.hasNext());
		assertEquals(TimeInterval.over(h5, h6_m30), it.next());
		assertTrue(it.hasNext());
		assertEquals(TimeInterval.over(h6_m30, h8), it.next());
		assertFalse(it.hasNext());
		assertNull(it.next());
	
	}
	
	public void testIntersection() {
		TimeInterval i19_22 = TimeInterval.over(dec19_2003, dec22_2003);
		TimeInterval i20_23 = TimeInterval.over(dec20_2003, dec23_2003);
		TimeInterval intersection = i19_22.intersect(i20_23);
		assertEquals(dec20_2003, intersection.start());
		assertEquals(dec22_2003, intersection.end());
		assertTrue("intersects true", i19_22.intersects(i20_23));

		TimeInterval i19_21 = TimeInterval.over(dec19_2003, dec21_2003);
		TimeInterval i22_23 = TimeInterval.over(dec22_2003, dec23_2003);
		assertFalse("intersects false",i19_21.intersects(i22_23));
	}
	
	
}
