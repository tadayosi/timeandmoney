/*
 * Created on Nov 24, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package example.holidayCalendar;

import java.util.Iterator;

import com.domainlanguage.time.*;

import junit.framework.TestCase;


public class HolidayCalendarExample extends TestCase {
	public void testDeriveBirthday() {
		//Calculate Martin Luther King, Jr.'s birthday, January 15, for the year 2005:
		DateSpecification mlkBirthday = DateSpecification.fixed(1, 15);
		// Then you can do checks like
		CalendarDate jan15_2005 = CalendarDate.from(2005, 1, 15);
		assertTrue(mlkBirthday.isSatisfiedBy(jan15_2005));
		//Derive the date(s) for an interval
		CalendarDate mlk2005 = mlkBirthday.firstOccurrenceIn(CalendarInterval.year(2005));
		assertEquals(jan15_2005, mlk2005);
		// Calculate all the birthdays in his lifetime
		CalendarInterval mlkLifetime = CalendarInterval.inclusive(1929, 1, 15, 1968, 4, 4);
		Iterator mlkBirthdays = mlkBirthday.iterateOver(mlkLifetime);
		assertEquals(CalendarDate.from(1929, 1, 15), mlkBirthdays.next());
		assertEquals(CalendarDate.from(1930, 1, 15), mlkBirthdays.next());
		// etc.
		// By the way, to calculate how long MLK lived,
		assertEquals("asb", mlkLifetime.length().toString());
		// TODO That is too slow and the toString needs work.
	}
}
