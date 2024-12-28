// Comment.java
package com.societal.carecrew;

public class Comment {
    private String commentId;
    private String userId;
    private String username;
    private String commentText;
    private Object timestamp;
    private String postId;
    private String profileImageUrl;// Add this field

    public Comment() {
        // Empty constructor for Firebase
    }

    // Updated constructor to include postId
    public Comment(String commentId, String userId, String username, String commentText, Object timestamp, String postId, String profileImageUrl) {
        this.commentId = commentId;
        this.userId = userId;
        this.username = username;
        this.commentText = commentText;
        this.timestamp = timestamp;
        this.postId = postId;
        this.profileImageUrl = profileImageUrl;

    }

    // Getters and setters for all fields (including the new getPostId() method)

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    // You might also want to add a getProfileImageUrl() method if you're displaying profile images for comments
}