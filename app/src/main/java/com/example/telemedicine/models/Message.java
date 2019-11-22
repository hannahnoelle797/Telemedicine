package com.example.telemedicine.models;
 import java.util.Date;
 import com.example.telemedicine.models.Chat;
public class Message {
    //creates a message table that connects to the chat table.
    //the message table holds the message content, read and delete status', and date sent.
    private String messageId, chatId, senderId, recipientId, messageContent;
    private boolean readStatus, mStatus;
    private Date dateSent;

    public Message(){
        this.readStatus = false;
        this.mStatus = true;
    }
    public Message(String messageID, String chatId, String senderId, String recipientId, String messageContent, Date dateSent){
        this.messageContent = messageContent;
        this.messageId = messageID;
        this.dateSent = dateSent;
        this.chatId = chatId;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.readStatus = false;
        this.mStatus = true;

        //add message to correct chatArrayList.
    }
    public void read(){
        this.readStatus = true;
    }
    public boolean readStatus() {return this.readStatus; }

    public boolean getMStatus() { return this.mStatus; }
    public void setMStatus(boolean b) { this.mStatus = b; }

    public Date getDate() {
        return dateSent;
    }
    public void setDate(Date mDate) {
        this.dateSent = mDate;
    }

    public String getMessageId() {
        return messageId;
    }
    public void setMessageId(String id) { this.messageId = id; }

    public String getMessage() {
        return messageContent;
    }
    public void setMessage(String message) {this.messageContent = message;}

    public String getSenderId() {
        return senderId;
    }
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }
}
