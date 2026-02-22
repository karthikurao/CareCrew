package com.societal.carecrew;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for Availability model
 */
public class AvailabilityTest {
    
    private Availability availability;
    
    @Before
    public void setUp() {
        availability = new Availability(true, false, true, false, true);
    }
    
    @Test
    public void testConstructorSetsCorrectValues() {
        assertTrue(availability.isWeekdays());
        assertFalse(availability.isWeekends());
        assertTrue(availability.isMornings());
        assertFalse(availability.isAfternoons());
        assertTrue(availability.isEvenings());
    }
    
    @Test
    public void testSettersAndGetters() {
        availability.setWeekdays(false);
        assertFalse(availability.isWeekdays());
        
        availability.setWeekends(true);
        assertTrue(availability.isWeekends());
        
        availability.setMornings(false);
        assertFalse(availability.isMornings());
        
        availability.setAfternoons(true);
        assertTrue(availability.isAfternoons());
        
        availability.setEvenings(false);
        assertFalse(availability.isEvenings());
    }
    
    @Test
    public void testAllAvailableSchedule() {
        Availability allAvailable = new Availability(true, true, true, true, true);
        assertTrue(allAvailable.isWeekdays());
        assertTrue(allAvailable.isWeekends());
        assertTrue(allAvailable.isMornings());
        assertTrue(allAvailable.isAfternoons());
        assertTrue(allAvailable.isEvenings());
    }
    
    @Test
    public void testNoAvailableSchedule() {
        Availability noAvailable = new Availability(false, false, false, false, false);
        assertFalse(noAvailable.isWeekdays());
        assertFalse(noAvailable.isWeekends());
        assertFalse(noAvailable.isMornings());
        assertFalse(noAvailable.isAfternoons());
        assertFalse(noAvailable.isEvenings());
    }
    
    @Test
    public void testWeekendMorningsOnly() {
        Availability weekendMornings = new Availability(false, true, true, false, false);
        assertFalse(weekendMornings.isWeekdays());
        assertTrue(weekendMornings.isWeekends());
        assertTrue(weekendMornings.isMornings());
        assertFalse(weekendMornings.isAfternoons());
        assertFalse(weekendMornings.isEvenings());
    }
    
    @Test
    public void testWeekdayEveningsOnly() {
        Availability weekdayEvenings = new Availability(true, false, false, false, true);
        assertTrue(weekdayEvenings.isWeekdays());
        assertFalse(weekdayEvenings.isWeekends());
        assertFalse(weekdayEvenings.isMornings());
        assertFalse(weekdayEvenings.isAfternoons());
        assertTrue(weekdayEvenings.isEvenings());
    }
}
