/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

import java.util.*;

import com.domainlanguage.basic.*;
import com.domainlanguage.util.*;

public abstract class CalendarInterval extends Interval {

	public static CalendarInterval inclusive(CalendarDate start, CalendarDate end) {
		if (start.equals(end)) return start;
		return ConcreteCalendarInterval.from(start, end);
	}

	public static CalendarInterval inclusive(int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay) {
		CalendarDate startDate = CalendarDate.from(startYear, startMonth, startDay);
		CalendarDate endDate = CalendarDate.from(endYear, endMonth, endDay);
		return ConcreteCalendarInterval.from(startDate, endDate);
	}

	public static CalendarDate date(int year, int month, int day) {
		return CalendarDate.from(year, month, day);
	}

	public static CalendarInterval month(int year, int month) {
		CalendarDate startDate = date(year, month, 1);
		CalendarDate endDate = startDate.plusMonths(1).plusDays(-1);
		return inclusive(startDate, endDate);
	}

	public static CalendarInterval year(int year) {
		CalendarDate startDate = date(year, 1, 1);
		CalendarDate endDate = date(year + 1, 1, 1).plusDays(-1);
		return inclusive(startDate, endDate);
	}

	public abstract TimeInterval asTimeInterval(TimeZone zone);

	public Interval newOfSameType(Comparable lower, boolean isLowerClosed, Comparable upper, boolean isUpperClosed) {
		CalendarDate includedLower = isLowerClosed ? (CalendarDate) lower : ((CalendarDate) lower).plusDays(1);
		CalendarDate includedUpper = isUpperClosed ? (CalendarDate) upper : ((CalendarDate) upper).plusDays(-1);
		return inclusive(includedLower, includedUpper);
	}

	public boolean includesLowerLimit() {
		return true;
	}

	public boolean includesUpperLimit() {
		return true;
	}

	public CalendarDate start() {
		return (CalendarDate) lowerLimit();
	}

	public CalendarDate end() {
		return (CalendarDate) upperLimit();
	}

	public boolean equals(Object arg) {
		if (!(arg instanceof CalendarInterval)) return false;
		CalendarInterval other = (CalendarInterval) arg;
		return 
			upperLimit().equals(other.upperLimit()) && 
			lowerLimit().equals(other.lowerLimit());
	}

	public int hashCode() {
		return lowerLimit().hashCode();
	}

	public Duration length() {
		return Duration.days(lengthInDaysInt());
	}

	public Duration lengthInMonths() {
		return Duration.months(lengthInMonthsInt());
	}

	public int lengthInMonthsInt() {
		Calendar calStart = start().asJavaCalendarUniversalZoneMidnight();
		Calendar calEnd = end().plusDays(1).asJavaCalendarUniversalZoneMidnight();
		int yearDiff = calEnd.get(Calendar.YEAR) - calStart.get(Calendar.YEAR);
		int monthDiff = yearDiff * 12 + calEnd.get(Calendar.MONTH) - calStart.get(Calendar.MONTH);
		return monthDiff;
	}

	public int lengthInDaysInt() {
		Calendar calStart = start().asJavaCalendarUniversalZoneMidnight();
		Calendar calEnd = end().plusDays(1).asJavaCalendarUniversalZoneMidnight();
		long diffMillis = calEnd.getTimeInMillis()-calStart.getTimeInMillis();
		return (int)(diffMillis/TimeUnitConversionFactors.millisecondsPerDay);
	}

	public Iterator daysIterator() {
		final CalendarDate start = (CalendarDate) lowerLimit();
		final CalendarDate end = (CalendarDate) upperLimit();
		return new ImmutableIterator() {
			CalendarDate next = start;
			public boolean hasNext() {
				return !next.isAfter(end);
			}
			public Object next() {
				Object current = next;
				next = next.plusDays(1);
				return current;
			}
		};
	}

}