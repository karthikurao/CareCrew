package com.societal.carecrew;

public class Post {

    private String uid; // User ID of the post creator
    private String username;
    private String caption;
    private String imageUrl;

    public Post() {
        // You can initialize fields to default values here if needed
    }

    // Constructor
    public Post(String uid, String username, String caption, String imageUrl) {
        this.uid = uid;
        this.username = username;
        this.caption = caption;
        this.imageUrl = imageUrl;
    }

    // Getters
    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getCaption() {
        return caption;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    // Setters (optional - you might need them for editing posts)
    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}