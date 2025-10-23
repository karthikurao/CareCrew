// Volunteer.java
package com.societal.carecrew;

public class Volunteer {
    private String userId;
    private String name;
    private String username;

    public Volunteer() {
        // Empty constructor for Firebase
    }

    public Volunteer(String userId, String name, String username) {
        this.userId = userId;
        this.name = name;
        this.username = username;
    }

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
