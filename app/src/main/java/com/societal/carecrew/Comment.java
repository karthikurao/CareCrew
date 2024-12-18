package com.societal.carecrew;

public class Comment {
    private String userId;
    private String username;
    private String commentText;
    private Object timestamp;
    private String commentId;

    public Comment() {} // Empty constructor for Firebase

    public Comment(String commentId, String userId, String username, String commentText, Object timestamp) {
        this.userId = userId;
        this.username = username;
        this.commentText = commentText;
        this.timestamp = timestamp;
        this.commentId = commentId;

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
}