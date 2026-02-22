// ChatRoom.java
package com.societal.carecrew;

import java.util.Map;

public class ChatRoom {
    private String chatRoomId;
    private Map<String, Boolean> participants;
    private boolean isGroup;
    private String groupId;
    private String lastMessage;
    private long lastMessageTime;
    private String chatName;

    public ChatRoom() {
        // Empty constructor for Firebase
    }

    public ChatRoom(String chatRoomId, Map<String, Boolean> participants, boolean isGroup, String groupId, String lastMessage, long lastMessageTime, String chatName) {
        this.chatRoomId = chatRoomId;
        this.participants = participants;
        this.isGroup = isGroup;
        this.groupId = groupId;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
        this.chatName = chatName;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public Map<String, Boolean> getParticipants() {
        return participants;
    }

    public void setParticipants(Map<String, Boolean> participants) {
        this.participants = participants;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public long getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(long lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }
}
