/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.basic;

import java.math.BigDecimal;

import com.domainlanguage.basic.ComparableInterval;

import junit.framework.TestCase;


public class ComparableIntervalTest extends TestCase {

   	ComparableInterval r5_10 = ComparableInterval.closed(new BigDecimal(5),new BigDecimal(10));
    ComparableInterval r1_10 = ComparableInterval.closed(new BigDecimal(1),new BigDecimal(10));
    ComparableInterval r4_6 = ComparableInterval.closed(new BigDecimal(4),new BigDecimal(6));
    ComparableInterval r5_15 = ComparableInterval.closed(new BigDecimal(5),new BigDecimal(15));
    ComparableInterval r12_16 = ComparableInterval.closed(new BigDecimal(12),new BigDecimal(16));
    ComparableInterval x10_12 = ComparableInterval.over(new BigDecimal(10), false, new BigDecimal(12), true);

    
//	public void testAssertions() {
//		//Redundant, maybe, but with all the compiler default
//		//confusion at the moment, I decided to throw this in.
//		try {
//			ComparableInterval.closed(new BigDecimal(2.0), new BigDecimal(1.0));
//			fail("Lower bound mustn't be above upper bound.");
//		} catch (AssertionError e) {
//			//Correct. Do nothing.
//		}
//	}

	public void testIsBelow() {
	  	ComparableInterval range = ComparableInterval.closed(new BigDecimal(-5.5), new BigDecimal(6.6));
	    assertFalse (range.isBelow(new BigDecimal(5.0)));
	    assertFalse (range.isBelow(new BigDecimal(-5.5)));
	    assertFalse (range.isBelow(new BigDecimal(-5.4999)));
	    assertFalse (range.isBelow(new BigDecimal(6.6)));
	    assertTrue (range.isBelow(new BigDecimal(6.601)));
	    assertFalse (range.isBelow(new BigDecimal(-5.501)));
	  }

	public void testIncludes() {
	  	ComparableInterval range = ComparableInterval.closed(new BigDecimal(-5.5), new BigDecimal(6.6));
	    assertTrue (range.includes(new BigDecimal(5.0)));
	    assertTrue (range.includes(new BigDecimal(-5.5)));
	    assertTrue (range.includes(new BigDecimal(-5.4999)));
	    assertTrue (range.includes(new BigDecimal(6.6)));
	    assertFalse (range.includes(new BigDecimal(6.601)));
	    assertFalse (range.includes(new BigDecimal(-5.501)));
	  }

	  public void testOpenInterval() {
	  	ComparableInterval exRange = ComparableInterval.over(new BigDecimal(-5.5), false, new BigDecimal(6.6), true);
	    assertTrue (exRange.includes(new BigDecimal(5.0)));
	    assertFalse (exRange.includes(new BigDecimal(-5.5)));
	    assertTrue (exRange.includes(new BigDecimal(-5.4999)));
	    assertTrue (exRange.includes(new BigDecimal(6.6)));
	    assertFalse (exRange.includes(new BigDecimal(6.601)));
	    assertFalse (exRange.includes(new BigDecimal(-5.501)));
	  }
	  	

//		public void testEmpty() {
//	   assertTrue (!Interval.closed(new BigDecimal(5),new BigDecimal(6)).isEmpty());
//	   assertTrue (!Interval.closed(new BigDecimal(6),new BigDecimal(6)).isEmpty());
//	   assertTrue (Interval.closed(new BigDecimal(7),new BigDecimal(6)).isEmpty());
//	  }
//	  public void testUpTo() {
//	    Interval range = Interval.upTo(new BigDecimal(5.5));
//	    assertTrue(range.includes(new BigDecimal(5.5)));
//	    assertTrue(range.includes(new BigDecimal(-5.5)));
//	    assertTrue(range.includes(new BigDecimal(Double.NEGATIVE_INFINITY)));
//	    assertTrue(!range.includes(new BigDecimal(5.5001)));
//	  }
//	  public void testAndMore() {
//	    Interval range = Interval.andMore(5.5);
//	    assertTrue(range.includes(5.5));
//	    assertTrue(!range.includes(5.4999));
//	    assertTrue(!range.includes(-5.5));
//	    assertTrue(range.includes(Double.POSITIVE_INFINITY));
//	    assertTrue(range.includes(5.5001));
//	  }
		public void testIntersection() {
		    assertTrue ("r5_10.intersects(r1_10)",r5_10.intersects(r1_10));
		    assertTrue ("r1_10.intersects(r5_10)",r1_10.intersects(r5_10));
		    assertTrue ("r4_6.intersects(r1_10)",r4_6.intersects(r1_10));
		    assertTrue ("r1_10.intersects(r4_6)",r1_10.intersects(r4_6));
		    assertTrue ("r5_10.intersects(r5_15)",r5_10.intersects(r5_15));
		    assertTrue ("r5_15.intersects(r1_10)",r5_15.intersects(r1_10));
		    assertTrue ("r1_10.intersects(r5_15)",r1_10.intersects(r5_15));
		    assertFalse ("r1_10.intersects(r12_16)",r1_10.intersects(r12_16));
		    assertFalse ("r12_16.intersects(r1_10)",r12_16.intersects(r1_10));
		    assertTrue ("r5_10.intersects(r5_10)",r5_10.intersects(r5_10));
		    assertFalse ("r1_10.intersects(x10_12)",r1_10.intersects(x10_12));
		    assertFalse ("x10_12.intersects(r1_10)",x10_12.intersects(r1_10));
	 	}

		public void testGreaterOfLowerLimits() {
		    assertEquals(new BigDecimal(5), r5_10.greaterOfLowerLimits(r1_10));
		    assertEquals(new BigDecimal(5), r1_10.greaterOfLowerLimits(r5_10));
		    assertEquals(new BigDecimal(12), r1_10.greaterOfLowerLimits(r12_16));
		    assertEquals(new BigDecimal(12), r12_16.greaterOfLowerLimits(r1_10));
	 	}

		public void testLesserOfUpperLimits() {
		    assertEquals(new BigDecimal(10), r5_10.lesserOfUpperLimits(r1_10));
		    assertEquals(new BigDecimal(10), r1_10.lesserOfUpperLimits(r5_10));
		    assertEquals(new BigDecimal(6), r4_6.lesserOfUpperLimits(r12_16));
		    assertEquals(new BigDecimal(6), r12_16.lesserOfUpperLimits(r4_6));
	 	}

		
		public void testIncludesRange() {
	    assertFalse (r5_10.includes(r1_10));
	    assertTrue (r1_10.includes(r5_10));
	    assertFalse (r4_6.includes(r1_10));
	    assertTrue (r1_10.includes(r4_6));
	    assertTrue (r5_10.includes(r5_10));
	    ComparableInterval halfOpen5_10 = ComparableInterval.over(new BigDecimal(5), false, new BigDecimal(10), true);
	    assertTrue("closed incl left-open", r5_10.includes(halfOpen5_10));
	    assertTrue("left-open incl left-open", halfOpen5_10.includes(halfOpen5_10));
	    assertFalse("left-open doesn't include closed", halfOpen5_10.includes(r5_10));
	    //TODO: Need to test other half-open case and full-open case.

	 	}

}
