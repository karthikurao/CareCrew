// VolunteerExperience.java
package com.societal.carecrew;

public class VolunteerExperience {
    private String organization;
    private String role;
    private String duration;
    private String description;

    // Constructor
    public VolunteerExperience(String organization, String role, String duration, String description) {
        this.organization = organization;
        this.role = role;
        this.duration = duration;
        this.description = description;
    }

    // Getters
    public String getOrganization() {
        return organization;
    }

    public String getRole() {
        return role;
    }

    public String getDuration() {
        return duration;
    }

    public String getDescription() {
        return description;
    }

    // Setters (optional - you might need them for editing experiences)
    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}