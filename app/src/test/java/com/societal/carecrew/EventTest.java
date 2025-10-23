package com.societal.carecrew;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for Event model class
 */
public class EventTest {

    @Test
    public void testEventCreation() {
        String eventId = "event123";
        String title = "Blood Donation Drive";
        String description = "Help save lives by donating blood";
        String date = "2024-12-01";
        String location = "Community Center";
        String category = "Health";
        String creatorId = "user123";

        Event event = new Event(eventId, title, description, date, location, category, creatorId);

        assertEquals(eventId, event.getEventId());
        assertEquals(title, event.getTitle());
        assertEquals(description, event.getDescription());
        assertEquals(date, event.getDate());
        assertEquals(location, event.getLocation());
        assertEquals(category, event.getCategory());
        assertEquals(creatorId, event.getCreatorId());
        assertEquals(0, event.getParticipantCount());
        assertTrue(event.getTimestamp() > 0);
    }

    @Test
    public void testEventSetters() {
        Event event = new Event();
        
        event.setEventId("event456");
        event.setTitle("Flood Relief");
        event.setDescription("Volunteer for flood relief efforts");
        event.setDate("2024-12-15");
        event.setLocation("Riverside District");
        event.setCategory("Disaster Relief");
        event.setCreatorId("user456");
        event.setParticipantCount(25);
        event.setTimestamp(1234567890L);

        assertEquals("event456", event.getEventId());
        assertEquals("Flood Relief", event.getTitle());
        assertEquals("Volunteer for flood relief efforts", event.getDescription());
        assertEquals("2024-12-15", event.getDate());
        assertEquals("Riverside District", event.getLocation());
        assertEquals("Disaster Relief", event.getCategory());
        assertEquals("user456", event.getCreatorId());
        assertEquals(25, event.getParticipantCount());
        assertEquals(1234567890L, event.getTimestamp());
    }

    @Test
    public void testEventDefaultConstructor() {
        Event event = new Event();
        assertNotNull(event);
        // Default constructor should not set any values
        assertNull(event.getEventId());
        assertNull(event.getTitle());
    }
}
