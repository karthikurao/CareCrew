// LeaderboardItem.java
package com.societal.carecrew;

public class LeaderboardItem {
    private String userId;
    private String name;
    private String profileImageUrl;
    private int hoursVolunteered;
    private int opportunitiesParticipated;
    private int groupsJoined;
    private int rank;

    // Constructor
    public LeaderboardItem() {
        // No-argument constructor for Firebase
    }

    public LeaderboardItem(String userId, String name, String profileImageUrl,
                           int hoursVolunteered, int opportunitiesParticipated, int groupsJoined) {
        this.userId = userId;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.hoursVolunteered = hoursVolunteered;
        this.opportunitiesParticipated = opportunitiesParticipated;
        this.groupsJoined = groupsJoined;
    }

    // Calculate total contribution score
    public int getTotalScore() {
        return hoursVolunteered + opportunitiesParticipated + groupsJoined;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
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

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
