// Group.java
package com.societal.carecrew;

import java.util.Map;

public class Group {
    private String groupId;
    private String name;
    private String description;
    private String createdBy;
    private Map<String, Object> members; // Add this field for members

    public Group() {} // Empty constructor for Firebase

    public Group(String groupId, String name, String description, String createdBy) {
        this.groupId = groupId;
        this.name = name;
        this.description = description;
        this.createdBy = createdBy;
    }

    // Getters and setters for all fields (including groupId, name, description, createdBy, and members)

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Map<String, Object> getMembers() {
        return members;
    }

    public void setMembers(Map<String, Object> members) {
        this.members = members;
    }
}