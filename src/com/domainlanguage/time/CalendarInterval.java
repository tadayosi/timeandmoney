/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

import java.io.*;
import java.util.*;

import com.domainlanguage.intervals.*;
import com.domainlanguage.util.*;

public class CalendarInterval implements Comparable, Serializable {
    Interval enclosedInterval;

    public static CalendarInterval inclusive(CalendarDate start,
            CalendarDate end) {
        return CalendarInterval.from(start, end);
    }

    public static CalendarInterval inclusive(int startYear, int startMonth,
            int startDay, int endYear, int endMonth, int endDay) {
        CalendarDate startDate = CalendarDate.from(startYear, startMonth,
                startDay);
        CalendarDate endDate = CalendarDate.from(endYear, endMonth, endDay);
        return CalendarInterval.from(startDate, endDate);
    }

    public static CalendarInterval month(int year, int month) {
        CalendarDate startDate = CalendarDate.date(year, month, 1);
        CalendarDate endDate = startDate.plusMonths(1).plusDays(-1);
        return inclusive(startDate, endDate);
    }

    public static CalendarInterval year(int year) {
        CalendarDate startDate = CalendarDate.date(year, 1, 1);
        CalendarDate endDate = CalendarDate.date(year + 1, 1, 1).plusDays(-1);
        return inclusive(startDate, endDate);
    }

    public static CalendarInterval startingFrom(CalendarDate start,
            Duration length) {
        // Uses the common default for calendar intervals, [start, end].
        return inclusive(start, start.plus(length).plusDays(-1));
    }

    protected static CalendarInterval from(CalendarDate start, CalendarDate end) {
        return new CalendarInterval(start, end);
    }

    CalendarInterval(CalendarDate start, CalendarDate end) {
        enclosedInterval = Interval.closed(start, end);
    }

    CalendarInterval(Interval anInterval) {
        Interval toUse = anInterval;
        if (!anInterval.includesUpperLimit()) {
            CalendarDate upper = (CalendarDate) anInterval.upperLimit();
            upper = upper.previousDay();
            toUse = new Interval(anInterval.lowerLimit(), anInterval
                    .includesLowerLimit(), upper, true);
        }
        enclosedInterval = toUse;
    }

    public CalendarDate start() {
        return (CalendarDate) enclosedInterval.lowerLimit();
    }

    public CalendarDate end() {
        return (CalendarDate) enclosedInterval.upperLimit();
    }

    public boolean equals(Object object) {
        // revisit: maybe use: Reflection.equalsOverClassAndNull(this, other)
        if (!(object instanceof CalendarInterval))
            return false;
        CalendarInterval other = (CalendarInterval) object;
        return enclosedInterval.equals(other.enclosedInterval);
    }

    public int hashCode() {
        return enclosedInterval.hashCode();
    }

    public Duration length() {
        return Duration.days(lengthInDaysInt());
    }

    public Duration lengthInMonths() {
        return Duration.months(lengthInMonthsInt());
    }

    public int lengthInMonthsInt() {
        Calendar calStart = start().asJavaCalendarUniversalZoneMidnight();
        Calendar calEnd = end().plusDays(1)
                .asJavaCalendarUniversalZoneMidnight();
        int yearDiff = calEnd.get(Calendar.YEAR) - calStart.get(Calendar.YEAR);
        int monthDiff = yearDiff * 12 + calEnd.get(Calendar.MONTH)
                - calStart.get(Calendar.MONTH);
        return monthDiff;
    }

    public int lengthInDaysInt() {
        Calendar calStart = start().asJavaCalendarUniversalZoneMidnight();
        Calendar calEnd = end().plusDays(1)
                .asJavaCalendarUniversalZoneMidnight();
        long diffMillis = calEnd.getTimeInMillis() - calStart.getTimeInMillis();
        return (int) (diffMillis / TimeUnitConversionFactors.millisecondsPerDay);
    }

    public Iterator subintervalIterator(Duration subintervalLength) {
        // assert TimeUnit.day.compareTo(subintervalLength.normalizedUnit()) <=
        // 0;
        if (TimeUnit.day.compareTo(subintervalLength.normalizedUnit()) > 0) {
            throw new IllegalArgumentException(
                    "CalendarIntervals must be a whole number of days or months.");
        }

        final Interval totalInterval = enclosedInterval;
        final Duration segmentLength = subintervalLength;
        return new ImmutableIterator() {
            CalendarInterval next = segmentLength.startingFrom(start());

            public boolean hasNext() {
                return totalInterval.covers(next.enclosedInterval);
            }

            public Object next() {
                if (!hasNext())
                    return null;
                CalendarInterval current = next;
                next = segmentLength.startingFrom(next.end().plusDays(1));
                return current;
            }
        };
    }

    public Iterator daysIterator() {
        final CalendarDate start = (CalendarDate) enclosedInterval.lowerLimit();
        final CalendarDate end = (CalendarDate) enclosedInterval.upperLimit();
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

    public int compareTo(Object other) {
        if (TypeCheck.is(other, CalendarInterval.class)) {
            final CalendarInterval otherInterval = (CalendarInterval) other;
            return enclosedInterval.compareTo(otherInterval.enclosedInterval);
        }
        return -1;
    }

    public boolean covers(CalendarInterval another) {
        return enclosedInterval.covers(another.enclosedInterval);
    }

    public boolean covers(CalendarDate aDate) {
        return covers(aDate.asCalendarInterval());
    }

    public List complementRelativeTo(CalendarInterval other) {
        final List intervals = enclosedInterval
                .complementRelativeTo(other.enclosedInterval);
        final List result = new ArrayList(intervals.size());
        Interval next;
        for (Iterator iter = intervals.iterator(); iter.hasNext();) {
            next = (Interval) iter.next();
            result.add(new CalendarInterval(next));
        }
        return result;
    }

    public boolean isClosed() {
        return enclosedInterval.isClosed();
    }

    public TimeInterval asTimeInterval(TimeZone zone) {
        TimePoint startPoint = start().asTimeInterval(zone).start();
        TimePoint endPoint = end().asTimeInterval(zone).end();
        return TimeInterval.over(startPoint, endPoint);
    }

}