package demo.reservation.util;

import demo.reservation.util.reservation.TimeSlot;

import java.time.LocalDateTime;

public final class TestUtils {

    public static TimeSlot betweenHours(int h1, int h2) {
        return TimeSlot.of(dateAtHour(h1), dateAtHour(h2));
    }

    public static LocalDateTime dateAtHour(int hour) {
        return LocalDateTime.of(2020, 1, 1, hour, 0);
    }

}