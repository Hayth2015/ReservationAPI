package demo.reservation.util;

import demo.reservation.util.reservation.TimeSlot;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TimeSlotTest {

    @Test
    void of_dateTimes() {
        TimeSlot timeSlot = TimeSlot.of(TestUtils.dateAtHour(0), TestUtils.dateAtHour(1));
        assertNotNull(timeSlot);
    }

    @Test
    void of_startAndDuration() {
        TimeSlot timeSlot = TimeSlot.of(TestUtils.dateAtHour(0), 1, ChronoUnit.HOURS);
        assertNotNull(timeSlot);
    }

    @Test
    void of_date() {
        TimeSlot timeSlot = TimeSlot.of(LocalDate.of(2020, 1, 1));
        assertNotNull(timeSlot);
    }

    @Test
    void setStart() {
        TimeSlot timeSlot = TestUtils.betweenHours(0, 2);
        TimeSlot timeSlot1 = timeSlot.setStart(TestUtils.dateAtHour(1));
        assertEquals(timeSlot1.getStart(),  TestUtils.dateAtHour(1));
        assertEquals(timeSlot1.getEnd(),  TestUtils.dateAtHour(2));
    }

    @Test
    void setEnd() {
        TimeSlot timeSlot = TestUtils.betweenHours(0, 2);
        TimeSlot timeSlot1 = timeSlot.setEnd(TestUtils.dateAtHour(1));
        assertEquals(timeSlot1.getStart(),  TestUtils.dateAtHour(0));
        assertEquals(timeSlot1.getEnd(),  TestUtils.dateAtHour(1));
    }

    @Test
    void contains_date() {
        TimeSlot timeSlot = TestUtils.betweenHours(0, 3);
        LocalDateTime localDateTime = TestUtils.dateAtHour(1);
        assertTrue(timeSlot.contains(localDateTime));
        localDateTime = TestUtils.dateAtHour(3);
        assertFalse(timeSlot.contains(localDateTime));
        localDateTime = TestUtils.dateAtHour(0);
        assertFalse(timeSlot.contains(localDateTime));
    }

    @Test
    void compareTo_date() {
        TimeSlot timeSlot = TestUtils.betweenHours(1, 3);
        LocalDateTime localDateTime = TestUtils.dateAtHour(2);
        assertEquals(0, timeSlot.compareTo(localDateTime));
        localDateTime = TestUtils.dateAtHour(3);
        assertEquals(-1, timeSlot.compareTo(localDateTime));
        localDateTime = TestUtils.dateAtHour(1);
        assertEquals(1, timeSlot.compareTo(localDateTime));
    }

    @Test
    void length() {
        TimeSlot timeSlot = TestUtils.betweenHours(1, 3);
        assertEquals(2, timeSlot.length(ChronoUnit.HOURS));
    }

    @Test
    void equals() {
        TimeSlot timeSlot = TestUtils.betweenHours(1, 3);
        TimeSlot timeSlot1 = TestUtils.betweenHours(1, 3);
        assertTrue(timeSlot.equals(timeSlot1));
    }

    @Test
    void contains_timeSlot() {
        TimeSlot timeSlot = TestUtils.betweenHours(0, 3);
        TimeSlot timeSlot1 = TestUtils.betweenHours(1, 2);
        assertTrue(timeSlot.contains(timeSlot1));
        assertFalse(timeSlot1.contains(timeSlot));
        timeSlot = TestUtils.betweenHours(1, 3);
        timeSlot1 = TestUtils.betweenHours(1, 2);
        assertTrue(timeSlot.contains(timeSlot1));
        assertFalse(timeSlot1.contains(timeSlot));
        timeSlot = TestUtils.betweenHours(0, 2);
        timeSlot1 = TestUtils.betweenHours(1, 2);
        assertTrue(timeSlot.contains(timeSlot1));
        assertFalse(timeSlot1.contains(timeSlot));
        timeSlot = TestUtils.betweenHours(1, 2);
        timeSlot1 = TestUtils.betweenHours(1, 2);
        assertFalse(timeSlot.contains(timeSlot1));
        assertFalse(timeSlot1.contains(timeSlot));
    }

    @Test
    void overlaps() {
        TimeSlot timeSlot = TestUtils.betweenHours(1, 3);
        TimeSlot timeSlot1 = TestUtils.betweenHours(1, 3);
        assertTrue(timeSlot.overlaps(timeSlot1));
        timeSlot = TestUtils.betweenHours(1, 3);
        timeSlot1 = TestUtils.betweenHours(2, 4);
        assertTrue(timeSlot.overlaps(timeSlot1));
        timeSlot = TestUtils.betweenHours(1, 3);
        timeSlot1 = TestUtils.betweenHours(0, 3);
        assertTrue(timeSlot.overlaps(timeSlot1));
        timeSlot = TestUtils.betweenHours(1, 3);
        timeSlot1 = TestUtils.betweenHours(0, 1);
        assertFalse(timeSlot.overlaps(timeSlot1));
        timeSlot = TestUtils.betweenHours(1, 3);
        timeSlot1 = TestUtils.betweenHours(4, 5);
        assertFalse(timeSlot.overlaps(timeSlot1));
    }

    @Test
    void subtract() {
        TimeSlot timeSlot = TestUtils.betweenHours(1, 8);
        TimeSlot timeSlot1 = TestUtils.betweenHours(0, 2);
        TimeSlot timeSlot2 = TestUtils.betweenHours(3, 4);
        TimeSlot timeSlot3 = TestUtils.betweenHours(3, 4);
        TimeSlot timeSlot4 = TestUtils.betweenHours(6, 7);
        List<TimeSlot> subtraction = timeSlot.subtract(timeSlot1, timeSlot2, timeSlot3, timeSlot4);
        assertTrue(subtraction.get(0).equals(TestUtils.betweenHours(2, 3)));
        assertTrue(subtraction.get(1).equals(TestUtils.betweenHours(4, 6)));
        assertTrue(subtraction.get(2).equals(TestUtils.betweenHours(7, 8)));
        timeSlot = TestUtils.betweenHours(1, 7);
        timeSlot1 = TestUtils.betweenHours(1, 7);
        assertEquals(0, timeSlot.subtract(timeSlot1).size());
    }
}
