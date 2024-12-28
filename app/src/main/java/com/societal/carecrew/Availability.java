// Availability.java
package com.societal.carecrew;

public class Availability {
    private boolean weekdays;
    private boolean weekends;
    private boolean mornings;
    private boolean afternoons;
    private boolean evenings;

    // Constructor
    public Availability(boolean weekdays, boolean weekends, boolean mornings, boolean afternoons, boolean evenings) {
        this.weekdays = weekdays;
        this.weekends = weekends;
        this.mornings = mornings;
        this.afternoons = afternoons;
        this.evenings = evenings;
    }

    // Getters and setters for all fields
    public boolean isWeekdays() {
        return weekdays;
    }

    public void setWeekdays(boolean weekdays) {
        this.weekdays = weekdays;
    }

    public boolean isWeekends() {
        return weekends;
    }

    public void setWeekends(boolean weekends) {
        this.weekends = weekends;
    }

    public boolean isMornings() {
        return mornings;
    }

    public void setMornings(boolean mornings) {
        this.mornings = mornings;
    }

    public boolean isAfternoons() {
        return afternoons;
    }

    public void setAfternoons(boolean afternoons) {
        this.afternoons = afternoons;
    }

    public boolean isEvenings() {
        return evenings;
    }

    public void setEvenings(boolean evenings) {
        this.evenings = evenings;
    }
}