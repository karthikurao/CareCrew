package com.societal.carecrew;

public class Event {
    private String eventId;
    private String title;
    private String description;
    private String date;
    private String location;
    private String category;
    private String creatorId;
    private long timestamp;
    private int participantCount;

    public Event() {
        // Default constructor required for Firebase
    }

    public Event(String eventId, String title, String description, String date, String location, String category, String creatorId) {
        this.eventId = eventId;
        this.title = title;
        this.description = description;
        this.date = date;
        this.location = location;
        this.category = category;
        this.creatorId = creatorId;
        this.timestamp = System.currentTimeMillis();
        this.participantCount = 0;
    }

    // Getters
    public String getEventId() {
        return eventId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getCategory() {
        return category;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getParticipantCount() {
        return participantCount;
    }

    // Setters
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setParticipantCount(int participantCount) {
        this.participantCount = participantCount;
    }
}
