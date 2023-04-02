package demo.reservation.util;

import demo.reservation.util.reservation.TimeSet;
import demo.reservation.util.reservation.TimeSlot;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class TimeSetTest {

    @Test
    void empty() {
        TimeSet ts = TimeSet.empty();
        assertEquals(0, ts.getTimeSlots().size());
    }

    @Test
    void of() {
        TimeSlot timeSlot = TestUtils.betweenHours(0, 12);
        TimeSet timeSet = TimeSet.of(timeSlot);
        TimeSet timeSet1 = TimeSet.of(Collections.singletonList(timeSlot));
        assertEquals(1, timeSet.getTimeSlots().size());
        assertEquals(1, timeSet1.getTimeSlots().size());
        TimeSet timeSet2 = TimeSet.of(timeSet);
        timeSet.add(TestUtils.betweenHours(14, 15));
        assertEquals(2, timeSet.getTimeSlots().size());
        assertEquals(1, timeSet2.getTimeSlots().size());
    }

    @Test
    void add_timeSlot() {
        TimeSlot timeSlot = TestUtils.betweenHours(0, 5);
        TimeSlot timeSlot1 = TestUtils.betweenHours(4, 10);
        TimeSlot timeSlot2 = TestUtils.betweenHours(12, 18);
        TimeSet timeSet = TimeSet.of(timeSlot, timeSlot1, timeSlot2);
        assertEquals(2, timeSet.getTimeSlots().size());
        assertTrue(timeSet.getTimeSlots().get(0).equals(TestUtils.betweenHours(12, 18)));
        assertTrue(timeSet.getTimeSlots().get(1).equals(TestUtils.betweenHours(0, 10)));
    }

    @Test
    void add_list() {
        TimeSlot timeSlot = TestUtils.betweenHours(0, 5);
        TimeSlot timeSlot1 = TestUtils.betweenHours(8, 10);
        TimeSet timeSet = TimeSet.of(timeSlot, timeSlot1);
        TimeSlot timeSlot2 = TestUtils.betweenHours(9, 12);
        TimeSlot timeSlot3 = TestUtils.betweenHours(15, 22);
        TimeSet timeSet1 = TimeSet.of(timeSlot2, timeSlot3);
        TimeSlot timeSlot4 = TestUtils.betweenHours(20, 23);
        TimeSet timeSet2 = TimeSet.of(timeSlot4);
        timeSet.add(timeSet1, timeSet2);
        assertEquals(3, timeSet.getTimeSlots().size());
        assertTrue(timeSet.getTimeSlots().get(0).equals(TestUtils.betweenHours(15, 23)));
        assertTrue(timeSet.getTimeSlots().get(1).equals(TestUtils.betweenHours(8, 12)));
        assertTrue(timeSet.getTimeSlots().get(2).equals(TestUtils.betweenHours(0, 5)));
    }

    @Test
    void equals() {
        TimeSlot timeSlot = TestUtils.betweenHours(2, 5);
        TimeSlot timeSlot1 = TestUtils.betweenHours(12, 15);
        TimeSet timeSet = TimeSet.of(timeSlot, timeSlot1);
        TimeSlot timeSlot2 = TestUtils.betweenHours(12, 15);
        TimeSlot timeSlot3 = TestUtils.betweenHours(2, 5);
        TimeSlot timeSlot4 = TestUtils.betweenHours(3, 5);
        TimeSet timeSet1 = TimeSet.of(timeSlot2, timeSlot3, timeSlot4);
        assertTrue(timeSet.equals(timeSet1));
        TimeSlot timeSlot5 = TestUtils.betweenHours(13, 16);
        timeSet1.add(timeSlot5);
        assertFalse(timeSet.equals(timeSet1));
    }

}