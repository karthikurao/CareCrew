// HelperClass.java
package com.societal.carecrew;

import java.util.List;
import java.util.Map;

public class HelperClass {
    private String name;
    private String email;
    private String username;
    private String password;
    private String profileImageUrl;
    private String coverImageUrl;
    private String bio;
    private int hoursVolunteered;
    private int opportunitiesParticipated;
    private int groupsJoined;
    private List<String> skills;
    private List<String> interests;
    private Availability availability;
    private List<String> causes;
    private String location;
    private String aboutMe;
    private List<VolunteerExperience> volunteerExperience;
    private Map<String, String> socialLinks; // Added for social links

    // Constructor
    public HelperClass(String name, String email, String username, String password) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.profileImageUrl = "";
        this.coverImageUrl = "";
        this.bio = "";
        this.hoursVolunteered = 0;
        this.opportunitiesParticipated = 0;
        this.groupsJoined = 0;
        this.location = "";
        this.aboutMe = "";
        // ... initialize other fields as needed
    }

    // Getters and setters for all fields

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getHoursVolunteered() {
        return hoursVolunteered;
    }

    public void setHoursVolunteered(int hoursVolunteered) {
        this.hoursVolunteered = hoursVolunteered;
    }

    public int getOpportunitiesParticipated() {
        return opportunitiesParticipated;
    }

    public void setOpportunitiesParticipated(int opportunitiesParticipated) {
        this.opportunitiesParticipated = opportunitiesParticipated;
    }

    public int getGroupsJoined() {
        return groupsJoined;
    }

    public void setGroupsJoined(int groupsJoined) {
        this.groupsJoined = groupsJoined;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public Availability getAvailability() {
        return availability;
    }

    public void setAvailability(Availability availability) {
        this.availability = availability;
    }

    public List<String> getCauses() {
        return causes;
    }

    public void setCauses(List<String> causes) {
        this.causes = causes;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public List<VolunteerExperience> getVolunteerExperience() {
        return volunteerExperience;
    }

    public void setVolunteerExperience(List<VolunteerExperience> volunteerExperience) {
        this.volunteerExperience = volunteerExperience;
    }

    public Map<String, String> getSocialLinks() {
        return socialLinks;
    }

    public void setSocialLinks(Map<String, String> socialLinks) {
        this.socialLinks = socialLinks;
    }

    // No-argument constructor for Firebase
    public HelperClass() {}
}