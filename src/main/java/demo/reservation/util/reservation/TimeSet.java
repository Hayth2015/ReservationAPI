package demo.reservation.util.reservation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A {@link TimeSet} represents a set of potentially non continuous time.
 * TimeSets are not immutable, but care is taken to ensure the correct order of operations is followed when
 * performing operations on TimeSets.
 *
 */
public class TimeSet {

    /**
     * List of continuous sets of time that make up a time set.
     */
    private List<TimeSlot> timeSlots;

    /**
     * Creates an empty time set.
     *
     * @return a time set with no time slots
     */
    public static TimeSet empty() {
        return new TimeSet(new ArrayList<>());
    }

    /**
     * Creates a time set from a single time slot.
     *
     * @param timeSlot the single time slot
     * @return a time set with a single time slot
     */
    public static TimeSet of(TimeSlot timeSlot) {
        return of(Collections.singletonList(timeSlot));
    }

    /**
     * Clone a time set.
     *
     * @param timeSet the time set to clone
     * @return a new time set with the same time slots
     */
    public static TimeSet of(TimeSet timeSet) {
        return TimeSet.of(timeSet.timeSlots);
    }

    /**
     * Create a time set from a list of time slots.
     *
     * @param timeSlots list of time slots
     * @return the time set that represents the combination of all time slots
     */
    public static TimeSet of(List<TimeSlot> timeSlots) {
        return new TimeSet(timeSlots);
    }

    /**
     * Create a time set from a varargs array of time slots.
     *
     * @param timeSlots list of time slots
     * @return the time set that represents the combination of all time slots
     */
    public static TimeSet of(TimeSlot ...timeSlots) {
        return of(Arrays.asList(timeSlots));
    }

    /**
     * Time set constructor. Requires a list of time slots. Ensures that it contains no overlapping sets of time within it,
     * and constructs a non continuous set of time.
     *
     * @param timeSlots the list of time slots to construct the time set with
     */
    private TimeSet(List<TimeSlot> timeSlots) {
        this.timeSlots = new ArrayList<>();
        timeSlots.forEach(this::add);
    }

    /**
     * Return a separate list containing the time slots making up this time set.
     *
     * @return a list containing all time slots in this set
     */
    public List<TimeSlot> getTimeSlots() {
        return new ArrayList<>(this.timeSlots);
    }

    /**
     * Add a time slot to this set. Time slots should not overlap each other in the set, rather they should combine if they overlap.
     *
     * @param timeSlot the time slot to add to this set
     */
    public void add(TimeSlot timeSlot) {
        this.timeSlots = timeSlot.add(this.timeSlots);
    }

    /**
     * Add a list of time sets to this. Addition is the equivalent of a union of sets. If this is A and the others are B through N
     * then add would result in A union B union ... union N.
     *
     * @param others the time sets to add to this
     */
    public void add(List<TimeSet> others) {
        List<TimeSlot> otherTimeSlots = new ArrayList<>();
        others.forEach(other -> otherTimeSlots.addAll(other.timeSlots));
        otherTimeSlots.forEach(this::add);
    }

    /**
     * Convenience method for add with varargs. See add(List).
     *
     * @param others the time sets to add to this
     */
    public void add(TimeSet... others) {
        this.add(Arrays.asList(others));
    }

    public boolean equals(TimeSet other) {
        if (other.timeSlots.size() != this.timeSlots.size()) {
            return false;
        }
        for (TimeSlot timeSlot : this.timeSlots) {
            if (other.timeSlots.stream().noneMatch(otherTimeSlot -> otherTimeSlot.equals(timeSlot))) {
                return false;
            }
        }
        return true;
    }
}
